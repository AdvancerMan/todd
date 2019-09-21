package com.company.todd.game.objs.active_objs.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.active_objs.dangerous.Bullet;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

public abstract class CreatureWithHands extends Creature {
    private MyAnimation handsAnimation;
    private Vector2 handsPosition;  // position relative to the main sprite
    private Sprite handsSprite;

    public CreatureWithHands(ToddEthottGame game, MyAnimation animation,
                             MyAnimation handsAnimation, Rectangle handsRectangle,
                             float jumpPower, float runningSpeed,
                             float x, float y, float width, float height) {
        super(game, animation, jumpPower, runningSpeed, x, y, width, height);
        this.handsAnimation = handsAnimation;
        this.handsPosition = new Vector2(handsRectangle.x, handsRectangle.y);
        handsSprite = new Sprite();
        handsSprite.setBounds(handsRectangle.x, handsRectangle.y, handsRectangle.width, handsRectangle.height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        handsAnimation.update(delta);
    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        super.draw(batch, cameraRectangle);

        Vector2 spritePos = getSpritePosition();
        Vector2 spriteSize = getSpriteSize();

        if (isDirectedToRight()) {
            handsSprite.setPosition(handsPosition.x, handsPosition.y);
        } else {
            handsSprite.setPosition(-handsSprite.getWidth() - (handsPosition.x - spriteSize.x),
                    handsPosition.y);
        }

        if (!body.isFixedRotation()) {
            handsSprite.setOrigin(-handsSprite.getX() + spriteSize.x / 2,
                    -handsSprite.getY() + spriteSize.y / 2);
            handsSprite.setRotation(body.getAngle() * FloatCmp.DEGS_IN_RAD);
        }

        handsSprite.translate(spritePos.x, spritePos.y);

        if (handsSprite.getBoundingRectangle().overlaps(cameraRectangle)) {
            handsSprite.setRegion(handsAnimation.getFrame());
            if (!isDirectedToRight()) {
                handsSprite.flip(true, false);
            }
            handsSprite.draw(batch);
        }
    }


    @Override
    public void setPlayingAnimationName(MyAnimation.AnimationType animType, boolean changeEquals) {
        if (animType.equals(MyAnimation.AnimationType.SHOOT)) {
            handsAnimation.setPlayingAnimationType(MyAnimation.AnimationType.SHOOT, changeEquals);
        } else {
            super.setPlayingAnimationName(animType, changeEquals);
        }
    }

    private float lastMomentOfShoot = 0;  // TODO cool cooldown

    @Override
    public void shoot() {  // TODO shoot
        if (TimeUtils.nanoTime() - lastMomentOfShoot <= cooldown) return;
        lastMomentOfShoot = TimeUtils.nanoTime();

        setPlayingAnimationName(MyAnimation.AnimationType.SHOOT, true);

        Vector2 bulPos = new Vector2();
        if (isDirectedToRight()) {
            bulPos.set(handsPosition.x, handsPosition.y);
        } else {
            bulPos.set(-handsSprite.getWidth() - (handsPosition.x - 0),  // TODO - bullet.width
                    handsPosition.y);
        }
        bulPos.add(getBodyPosition());  // TODO with rotation
        // TODO proper position for bullets

        // TODO BulletType!!!
        ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo.TextureRegionGetter>> tmp = new ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo.TextureRegionGetter>>();
        tmp.put(MyAnimation.AnimationType.RUN, new Array<TextureRegionInfo.TextureRegionGetter>(new TextureRegionInfo.TextureRegionGetter[] {game.regionInfos.getRegionInfo("grassPlatformDown").getRegionGetter()}));
        tmp.put(MyAnimation.AnimationType.STAY, new Array<TextureRegionInfo.TextureRegionGetter>(new TextureRegionInfo.TextureRegionGetter[] {game.regionInfos.getRegionInfo("grassPlatformDown").getRegionGetter()}));

        ArrayMap<MyAnimation.AnimationType, Float> tmpp = new ArrayMap<MyAnimation.AnimationType, Float>();
        tmpp.put(MyAnimation.AnimationType.RUN, 0.1f);
        tmpp.put(MyAnimation.AnimationType.STAY, 0.1f);

        ArrayMap<MyAnimation.AnimationType, Animation.PlayMode> tmppp = new ArrayMap<MyAnimation.AnimationType, Animation.PlayMode>();
        tmppp.put(MyAnimation.AnimationType.RUN, Animation.PlayMode.LOOP);
        tmppp.put(MyAnimation.AnimationType.STAY, Animation.PlayMode.LOOP);

        Bullet bul = new Bullet(
                game, this,
                new MyAnimation(tmpp, tmppp, tmp),
                bulPos.x, bulPos.y, 100, 20, toRight
        );
        bul.setPlayingAnimationName(MyAnimation.AnimationType.RUN, true);
        gameProcess.addObject(bul);
    }
}

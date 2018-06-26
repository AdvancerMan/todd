package com.company.todd.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.font.FontHandler;

public class TextButton extends Button {
    private String text;
    private float textX, textY;
    private boolean outOfBounds;

    public TextButton(ButtonFunction func, ToddEthottGame game,
                      int x, int y, int width, int height) {
        super(func, game, x, y, width, height);
        text = "";
        updateTextConfigurations();
    }

    public TextButton(ButtonFunction func, ToddEthottGame game,
                      int x, int y, int width, int height, String text) {
        super(func, game, x, y, width, height);
        this.text = text;
        updateTextConfigurations();
    }

    private void updateTextConfigurations() {
        FontHandler.FontSize size = FontHandler.getTextSize(game.mainFont, text);
        float width = size.width, height = size.height;

        textY = spriteNotClicked.getY() + spriteNotClicked.getHeight() / 2;
        textY += height / 2;

        textX = spriteNotClicked.getX() + spriteNotClicked.getWidth() / 2;
        textX -= width / 2;

        outOfBounds = false;
        if (textX < spriteNotClicked.getX()) {
            outOfBounds = true; // TODO сделать больше строк при выходе за границы (!)если потребуется(!)
            textX = spriteNotClicked.getX() + 2;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        if (outOfBounds) {
            game.buttonsFont.draw(batch, text, textX, textY,
                    spriteNotClicked.getWidth() - 4,
                    Align.center, true);
        }
        else {
            game.buttonsFont.draw(batch, text, textX, textY);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateTextConfigurations();
    }
}

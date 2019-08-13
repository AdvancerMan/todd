package com.company.todd.texture;

import java.util.HashMap;
import java.util.Map;

public class TextureRegionInfos {
    Map<String, TextureRegionInfo> regions;

    public TextureRegionInfos(TextureManager mng) {  // TODO textures
        regions = new HashMap<String, TextureRegionInfo>();

        regions.put(
                "buttonClicked", // TODO button texture
                new TextureRegionInfo(mng, "white.png", 10, 10, 10, 10)
        );
        regions.put(
                "buttonNotClicked", // TODO button texture
                new TextureRegionInfo(mng, "friend.png", 0, 0, 118, 122)
        );
        regions.put(
                "player", // TODO player texture
                new TextureRegionInfo(mng, "friend.png", 0, 0, 118, 122)
        );
        regions.put(
                "grassPlatformUp", // TODO texture
                new TextureRegionInfo(mng, "badlogic.jpg", 0, 0, 100, 100)
        );
        regions.put(
                "grassPlatformDown", // TODO texture
                new TextureRegionInfo(mng, "friend.png", 0, 0, 100, 100)
        );

        regions.put(
                "player1",
                new TextureRegionInfo(mng, "GG2.png", 0, 0, 20, 51)
        );
        regions.put(
                "player2",
                new TextureRegionInfo(mng, "GG2.png", 20, 0, 20, 51)
        );
        regions.put(
                "player3",
                new TextureRegionInfo(mng, "GG2.png", 40, 0, 20, 51)
        );
        regions.put(
                "player4",
                new TextureRegionInfo(mng, "GG2.png", 60, 0, 20, 51)
        );
        regions.put(
                "player5",
                new TextureRegionInfo(mng, "GG2.png", 80, 0, 20, 51)
        );
        regions.put(
                "player6",
                new TextureRegionInfo(mng, "GG2.png", 100, 0, 20, 51)
        );
        regions.put(
                "player7",
                new TextureRegionInfo(mng, "GG2.png", 120, 0, 20, 51)
        );
        regions.put(
                "player8",
                new TextureRegionInfo(mng, "GG2.png", 140, 0, 20, 51)
        );
        regions.put(
                "player9",
                new TextureRegionInfo(mng, "GG2.png", 160, 0, 20, 51)
        );
        regions.put(
                "player10",
                new TextureRegionInfo(mng, "GG2.png", 180, 0, 20, 51)
        );
        regions.put(
                "player11",
                new TextureRegionInfo(mng, "GG2.png", 200, 0, 20, 51)
        );
        regions.put(
                "player12",
                new TextureRegionInfo(mng, "GG2.png", 220, 0, 20, 51)
        );
    }

    public TextureRegionInfo getRegionInfo(String regionName) {
        return regions.get(regionName);
    }

    public TextureRegionInfo.TextureRegionGetter getRegionGetter(String regionName) {
        return getRegionInfo(regionName).getRegionGetter();
    }
}


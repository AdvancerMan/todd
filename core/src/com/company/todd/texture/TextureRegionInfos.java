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
                "grassPlatformUp", // TODO texture
                new TextureRegionInfo(mng, "white.png", 10, 10, 10, 10)
        );
        regions.put(
                "grassPlatformDown", // TODO texture
                new TextureRegionInfo(mng, "friend.png", 0, 0, 118, 122)
        );
    }

    public TextureRegionInfo getRegionInfo(String regionName) {
        return regions.get(regionName);
    }
}

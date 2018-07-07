package com.company.todd.texture;

import java.util.HashMap;
import java.util.Map;

public class TextureRegionInfos {
    Map<String, TextureRegionInfo> regions;

    public TextureRegionInfos(TextureManager mng) {
        regions = new HashMap<String, TextureRegionInfo>();

        regions.put(
                "buttonClicked", // TODO button texture
                new TextureRegionInfo(mng, "white.png", 10, 10, 10, 10)
        );
        regions.put(
                "buttonNotClicked", // TODO button texture
                new TextureRegionInfo(mng, "white.png", 0, 0, 10, 10)
        );
    }

    public TextureRegionInfo getRegionInfo(String regionName) {
        return regions.get(regionName);
    }
}

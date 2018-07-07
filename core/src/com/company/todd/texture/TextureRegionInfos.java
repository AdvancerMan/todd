package com.company.todd.texture;

import java.util.HashMap;
import java.util.Map;

public class TextureRegionInfos {
    Map<String, TextureRegionInfo> regions;

    public TextureRegionInfos(TextureManager mng) {
        regions = new HashMap<String, TextureRegionInfo>();

        regions.put(
                "white.png/buttonClicked",
                new TextureRegionInfo(mng, "white.png", 10, 10, 10, 10)
        );
        regions.put(
                "white.png/buttonNotClicked",
                new TextureRegionInfo(mng, "white.png", 0, 0, 10, 10)
        );
    }

    public TextureRegionInfo getRegion(String regionName) {
        return regions.get(regionName);
    }
}

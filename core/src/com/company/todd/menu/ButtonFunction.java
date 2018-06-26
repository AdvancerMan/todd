package com.company.todd.menu;

import com.company.todd.launcher.ToddEthottGame;

@FunctionalInterface
public interface ButtonFunction { // Consumers aren't available on low Android API level :(
    public void click(ToddEthottGame game);
}

package com.company.todd.menu;

@FunctionalInterface
public interface ButtonFunction<T> { // Consumers aren't available on low Android API level :(
    public void click(T obj);
}

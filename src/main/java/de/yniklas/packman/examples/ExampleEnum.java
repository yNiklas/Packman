package de.yniklas.packman.examples;

import de.yniklas.packman.Include;
import de.yniklas.packman.Package;

/**
 * @since 1.0.3
 */
@Package
public enum ExampleEnum {
    @Include(key = "test")
    TEST("hello"),

    SOP("world");

    ExampleEnum(String aCoolString) {

    }
}

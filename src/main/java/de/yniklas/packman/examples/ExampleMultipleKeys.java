package de.yniklas.packman.examples;

import de.yniklas.packman.Include;
import de.yniklas.packman.Package;

/**
 * @since 1.0.1
 */
@Package
public class ExampleMultipleKeys {
    @Include(key = "cool")
    int myInt = 10;

    String cool = "huhu";
}

package de.yniklas.packman.examples;

import de.yniklas.packman.Include;
import de.yniklas.packman.Package;

/**
 * @since 1.0.3
 */
@Package
public class ExampleClassForEnum {
    private ExampleEnum enumConstant = ExampleEnum.TEST;

    @Include(key = "superSOP")
    private ExampleEnum enum2 = ExampleEnum.SOP;

    int randomInt = 7;
}

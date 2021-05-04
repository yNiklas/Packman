package de.yniklas.packman.examples;

import de.yniklas.packman.Include;

public class ExampleMultiScope {
    /**
     * Included in the productInfo scope. Since there is no @Package annotation of the class,
     * it's only included in packages with the productInfo scope.
     */
    @Include(scopes = "productInfo")
    String productName = "Minecraft";

    /**
     * Included in the productInfo scope. Since there is no @Package annotation of the class,
     * it's only included in packages with the productInfo scope.
     */
    @Include(scopes = "productInfo")
    double price = 25.99;

    /**
     * Included in the copyright scope. Since there is no @Package annotation of the class,
     * it's only included in packages with the copyright scope.
     */
    @Include(scopes = "copyright")
    ExampleUser creator = new ExampleUser();

    /**
     * Included in every package due to the @Include annotation.
     */
    @Include
    String uId = "0765765865";
}

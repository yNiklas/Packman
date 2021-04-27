package de.yniklas.packagerize.examples;

import de.yniklas.packagerize.Exclude;
import de.yniklas.packagerize.Include;
import de.yniklas.packagerize.Package;

@Package
public class ExamplePackage {
    /**
     * Private attributes are included.
     */
    private String foo = "bar";

    /**
     * Included by the @Package annotation from the class.
     */
    byte b = 19;

    /**
     * No package will contain the pin attribute.
     * @Exclude = @Exclude() = @Exclude (scopes = {}).
     *
     * No scopes are given (via scopes = {...}), no package scope will contain the attribute.
     */
    @Exclude
    int pin = 4056;

    /**
     * This attribute is only excluded on packaging operations calling the "John" scope.
     * In every other scopes package, the attribute will be present.
     */
    @Exclude(scopes = {"John"})
    String shameJon = "Jon is a N00b";

    /**
     * Included in every package.
     */
    String name = "Jack";

    /**
     * The attribute isn't fresh enough for the packages.
     * The key parameter of the @Include defines the key of
     * the attribute in the package.
     */
    @Include(key = "synonym")
    String myVeryCoolSynonym = "Poop";
}

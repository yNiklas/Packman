package de.yniklas.packman.examples;

import de.yniklas.packman.Exclude;
import de.yniklas.packman.Include;
import de.yniklas.packman.IncludeOnly;
import de.yniklas.packman.Package;

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

    /**
     * Include some user information in the display and auth scope.
     * Attention: The class of an attribute annotated with @Include should be annotated
     * as usual. Without @Package or @Include annotations, the object with the information
     * about the attribute will be empty.
     */
    @IncludeOnly(key = "userData", scopes = {"display", "auth"})
    ExampleUser exampleUser= new ExampleUser();
}

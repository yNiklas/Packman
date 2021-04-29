package de.yniklas.packman.examples;

import de.yniklas.packman.Exclude;
import de.yniklas.packman.Package;

@Package(key = "userData")
public class ExampleUser {
    private String username = "Jog";

    @Exclude(scopes = {"display"})
    private String password = "topSecret";
}

package de.yniklas.packagerize.examples;

import de.yniklas.packagerize.Exclude;
import de.yniklas.packagerize.Package;

@Package
public class ExampleUser {
    private String username = "Jog";

    @Exclude(scopes = {"display"})
    private String password = "topSecret";
}

package de.yniklas.packi.examples;

import de.yniklas.packi.Exclude;
import de.yniklas.packi.Package;

@Package
public class ExampleUser {
    private String username = "Jog";

    @Exclude(scopes = {"display"})
    private String password = "topSecret";
}

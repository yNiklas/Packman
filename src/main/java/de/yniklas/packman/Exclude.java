package de.yniklas.packman;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates a field being excluded of packages (so be not part of them).
 * Excludes overwrites packaging instructions from @Package and @Include and @IncludeOnly.
 *
 * Example:
 *  @Include(scopes = {"test", "xy"})
 *  @Exclude
 *  String myField = "hi";
 *
 * -> The myField attribute won't be part of any package.
 *
 * Example 2:
 *  @Include(scopes = {"test", "xy"})
 *  @Exclude(scopes = {"xy"})
 *  String myField = "hi";
 *
 * -> The myField attribute will be part of the package in scope "test", but won't be present
 *    in the package of scope "xy".
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {
    /**
     * Define the scopes where the field is excluded.
     * Default/{} means, that the field is excluded in every package, so won't be present
     * in any package of any scope.
     */
    String[] scopes() default {};
}

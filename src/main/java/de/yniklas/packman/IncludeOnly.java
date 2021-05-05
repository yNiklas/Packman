package de.yniklas.packman;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates, that the field is only included in packages with scope identifiers
 * listed in the {@code scopes} array.
 *
 * Overwrites the access level of @Package -> Won't be included in anything else than
 * the in the {@code scopes} defined scopes.
 *
 * Weaker than excludes.
 *
 * @IncludeOnly = @IncludeOnly() = @IncludeOnly(scopes = {}) is redundant but has an excluding effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IncludeOnly {
    /**
     * Specifies the fields key in the package.
     */
    String key() default "";

    /**
     * Specifies the onliest scopes where the field is included.
     */
    String[] scopes();
}

package de.yniklas.packman;

/**
 * Scope policies define how multiple scopes should be handled in the {@link Packman} packaging
 * with the scopes array.
 */
public enum ScopePolicy {
    /**
     * A field/object must only be included in one of the scopes of a scope array to be part of the
     * current packaging operation.
     * It doesn't matter in how much scopes the field is being packaged, just one at least.
     */
    OR,

    /**
     * A field/object must be included in all scopes of a scope array to be present in the
     * current package.
     */
    AND
}

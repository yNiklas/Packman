package de.yniklas.packman;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Packman packs objects into meaningful org.json.JSON objects.
 *
 * @author yNiklas (https://github.com/yNiklas)
 * @version 1.0
 * @apiNote https://github.com/yNiklas/Packman
 */
public class Packman {
    private static final String DIR_SPLIT = "/";

    /**
     * Packs (a) given object(s) regarding the objects Packman annotations.
     * See https://github.com/yNiklas/Packman for more information and documentation.
     *
     * @param scope the scope to pack after.
     * @param packObject the object to be packed.
     * @param morePackageObjects further objects to be packed in all the same object ({@param packObject} will be
     *                           in the same package).
     * @return the packed object as a org.json.JSONObject.
     */
    public static JSONObject pack(String scope, Object packObject, Object... morePackageObjects) {
        // Single objects aren't in a sub-object inside the package.
        // -> Separate this case
        if (morePackageObjects.length == 0) {
            try {
                return packSingleObject(packObject, scope);
            } catch (IllegalAccessException e) {
                // Return an empty object in case of access errors
                return new JSONObject();
            }
        }

        List<Object> toPackObjects = new ArrayList<>(Arrays.asList(morePackageObjects));
        toPackObjects.add(packObject);
        JSONObject fullPack = new JSONObject();

        try {
            for (Object toPackObject : toPackObjects) {
                JSONObject objectAsJSON = packSingleObject(toPackObject, scope);

                // Give every object the key defined in the class Package annotation.
                // For default, use the class name.
                if (toPackObject.getClass().getAnnotation(Package.class) != null
                        && !toPackObject.getClass().getAnnotation(Package.class).key().equals("")) {

                    createSubDirectoriesAndPut(toPackObject.getClass().getAnnotation(Package.class).key(),
                            toPackObject.getClass().getName(), fullPack, objectAsJSON);

                } else {
                    fullPack.put(createJSONKey(toPackObject.getClass().getName(), fullPack), objectAsJSON);
                }
            }
        } catch (IllegalAccessException e) {
            return fullPack;
        }
        return fullPack;
    }

    private static JSONObject packSingleObject(Object packObject, String scope) throws IllegalAccessException {
        JSONObject pack = new JSONObject();

        for (Field field : packObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (isIncluded(field, scope, packObject.getClass())) {
                // Either field is explicitly @Include annotated or inherit by the @Package annotation from the class.
                // Otherwise there is an @IncludeOnly annotation
                Include include = field.getAnnotation(Include.class);
                IncludeOnly includeOnly = field.getAnnotation(IncludeOnly.class);

                String dir = "";
                String key = field.getName();

                if (includeOnly != null && !includeOnly.key().equals("")) {
                    // IncludeOnly field, ignore all other cases
                    dir = includeOnly.key();
                    key = includeOnly.key();
                } else if (include != null && !include.key().equals("")) {
                    // Explicitly included by @Include annotation
                    // Package call to the scope
                    dir = include.key();
                    key = include.key();
                }

                if (isJSONPrimitive(field.getType()) || field.getType().equals(List.class)
                        || field.getType().isArray()) {
                    // Packaging for primitive types, lists and arrays
                    packTo(pack, dir, field.getName(), field.get(packObject), scope);
                } else {
                    // Custom class packaging (e.g. annotated custom classes)
                    pack.put(createJSONKey(key, pack), pack(scope, field.get(packObject)));
                }
            }
        }
        return pack;
    }

    private static boolean isIncluded(Field field, String scope, Class<?> objectClass) {
        Exclude exclude = field.getAnnotation(Exclude.class);
        if (exclude != null && (exclude.scopes().length == 0 || Arrays.stream(exclude.scopes()).toList().contains(scope))) {
            return false;
        }

        IncludeOnly includeOnly = field.getAnnotation(IncludeOnly.class);
        if (includeOnly != null) {
            return Arrays.asList(includeOnly.scopes()).contains(scope);
        }

        Package packaging = objectClass.getAnnotation(Package.class);
        if (packaging != null && (packaging.scopes().length == 0 || Arrays.stream(packaging.scopes()).toList().contains(scope))) {
            return true;
        }

        Include include = field.getAnnotation(Include.class);
        return include != null &&
                (include.scopes().length == 0 || Arrays.stream(include.scopes()).toList().contains(scope));
    }

    private static boolean isJSONPrimitive(Class<?> type) {
        return type.isPrimitive() || type.equals(Integer.class) || type.equals(Long.class)
                || type.equals(Float.class) || type.equals(Byte.class) || type.equals(Double.class)
                || type.equals(Character.class) || type.equals(String.class) || type.equals(Short.class);
    }

    private static void packTo(JSONObject origin, String targetDir, String defaultFieldName, Object value,
                               String scope) {
        if (targetDir.equals("")) {
            origin.put(createJSONKey(defaultFieldName, origin), parse(value, scope));
            return;
        }

        JSONObject fulfill = origin;
        List<String> dirs = new ArrayList<>(Arrays.asList(targetDir.split(DIR_SPLIT)));
        if (targetDir.endsWith(DIR_SPLIT)) {
            dirs.add(DIR_SPLIT);
        }

        for (String dir : dirs) {
            if (dir.equals("")) {
                continue;
            }

            if (dirs.indexOf(dir) == dirs.size() - 1) {
                // Deepest directory reached
                String dirName = dir;
                if (dir.equals(DIR_SPLIT)) {
                    // Scope identifier ends with / -> use attribute name as key
                    dirName = defaultFieldName;
                }

                fulfill.put(createJSONKey(dirName, fulfill), parse(value, scope));
            } else if (!fulfill.has(dir)) {
                // Create sub-directory in the key-hierarchy
                fulfill.put(dir, new JSONObject());
                fulfill = fulfill.getJSONObject(dir);
            } else if (fulfill.has(dir)) {
                // If sub-directory exists, update just the object reference
                fulfill = fulfill.getJSONObject(dir);
            }
        }
    }

    private static Object parse(Object value, String scope) {
        if (value instanceof List) {
            JSONArray list = new JSONArray();
            ((List) value).forEach(item -> {
                if (isJSONPrimitive(item.getClass())) {
                    list.put(item);
                } else {
                    list.put(parse(item, scope));
                }
            });
            value = list;
        } else if (value.getClass().isArray()) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < Array.getLength(value); i++) {
                if (isJSONPrimitive(Array.get(value, i).getClass())) {
                    array.put(Array.get(value, i));
                } else {
                    array.put(parse(Array.get(value, i), scope));
                }
            }
            value = array;
        } else if (!isJSONPrimitive(value.getClass())) {
            value = pack(scope, value);
        }
        return value;
    }

    private static void createSubDirectoriesAndPut(String directoryDefinition, String defaultKey, JSONObject toFulfill,
                                                   Object packedObject) {
        List<String> dirs = new ArrayList<>(Arrays.asList(directoryDefinition.split(DIR_SPLIT)));
        if (directoryDefinition.endsWith(DIR_SPLIT)) {
            dirs.add(DIR_SPLIT);
        }

        JSONObject temporaryFulfill = toFulfill;
        for (String dir : dirs) {
            if (dir.equals("")) {
                continue;
            }

            if (dirs.indexOf(dir) == dirs.size() - 1) {
                // Deepest directory reached
                String dirName = dir;
                if (dir.equals(DIR_SPLIT)) {
                    // Scope identifier ends with / -> use attribute name as key
                    dirName = defaultKey;
                }

                temporaryFulfill.put(createJSONKey(dirName, temporaryFulfill), packedObject);
            } else if (!temporaryFulfill.has(dir)) {
                // Create sub-directory in the key-hierarchy
                temporaryFulfill.put(dir, new JSONObject());
                temporaryFulfill = temporaryFulfill.getJSONObject(dir);
            } else if (temporaryFulfill.has(dir)) {
                // If sub-directory exists, update just the object reference
                temporaryFulfill = temporaryFulfill.getJSONObject(dir);
            }
        }
    }

    /**
     * Checks for multiple equal JSON keys.
     * Attach numbers behind an equal key to make it unique.
     *
     * @param intendedKey the should-be JSON key.
     * @return the validated JSON key.
     * @since 1.0.1
     */
    private static String createJSONKey(String intendedKey, JSONObject packaging) {
        if (packaging.has(intendedKey)) {
            int value = 0;
            int digits = 0;
            for (int i = intendedKey.toCharArray().length - 1; i >= 0; i--) {
                if (Character.isDigit(intendedKey.toCharArray()[i])) {
                    value += Integer.parseInt(String.valueOf(intendedKey.toCharArray()[i]));
                    digits++;
                } else {
                    break;
                }
            }
            return value == 0 ? intendedKey + 1 : intendedKey.substring(0, intendedKey.length() - digits) + value;
        } else {
            return intendedKey;
        }
    }
}

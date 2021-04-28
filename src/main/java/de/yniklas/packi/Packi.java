package de.yniklas.packi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Packi {
    private static final String DIR_SPLIT = "/";

    public static void main(String[] args) { }

    public static JSONObject pack(String scope, Object packObject, Object... morePackageObjects) throws IllegalAccessException {
        // Single objects aren't in a sub-object inside the package.
        // -> Separate this case
        if (morePackageObjects.length == 0) {
            return packSingleObject(packObject, scope);
        }

        List<Object> toPackObjects = new ArrayList<>(Arrays.asList(morePackageObjects));
        toPackObjects.add(packObject);
        JSONObject fullPack = new JSONObject();

        for (Object toPackObject : toPackObjects) {
            JSONObject objectAsJSON = packSingleObject(toPackObject, scope);

            // Give every object the key defined in the class Package annotation.
            // For default, use the class name.
            if (toPackObject.getClass().getAnnotation(Package.class) != null
                    && !toPackObject.getClass().getAnnotation(Package.class).key().equals("")) {
                fullPack.put(toPackObject.getClass().getAnnotation(Package.class).key(), objectAsJSON);
            } else {
                fullPack.put(toPackObject.getClass().getName(), objectAsJSON);
            }
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

                if (includeOnly != null) {
                    // IncludeOnly field, ignore all other cases
                    dir = includeOnly.key();
                    key = includeOnly.key().equals("") ? field.getName() : includeOnly.key();
                } else if (include != null) {
                    // Explicitly included by @Include annotation
                    // Package call to the scope
                    dir = include.key();
                    key = include.key().equals("") ? field.getName() : include.key();
                }

                if (isJSONPrimitive(field.getType()) || field.getType().equals(List.class)
                        || field.getType().isArray()) {
                    // Packaging for primitive types, lists and arrays
                    packTo(pack, dir, field.getName(), field.get(packObject), scope);
                } else {
                    // Custom class packaging (e.g. annotated custom classes)
                    pack.put(key, pack(scope, field.get(packObject)));
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
        Include include = field.getAnnotation(Include.class);
        return packaging != null || (include != null &&
                (include.scopes().length == 0 || Arrays.stream(include.scopes()).toList().contains(scope)));
    }

    private static boolean isJSONPrimitive(Class<?> type) {
        return type.isPrimitive() || type.equals(Integer.class) || type.equals(Long.class)
                || type.equals(Float.class) || type.equals(Byte.class)
                || type.equals(String.class);
    }

    private static void packTo(JSONObject origin, String targetDir, String defaultFieldName, Object value,
                               String scope) {
        if (targetDir.equals("")) {
            origin.put(defaultFieldName, parse(value, scope));
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

                fulfill.put(dirName, parse(value, scope));
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
            try {
                value = pack(scope, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}

package de.yniklas.packagerize;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Packi {
    private static final String DIR_SPLIT = "/";

    public static void main(String[] args) { }

    public static JSONObject pack(String scope, Object packObject, Object... morePackageObjects) throws IllegalAccessException {
        JSONObject pack = new JSONObject();
        for (Field field : packObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (isIncluded(field, scope, packObject.getClass())) {
                // Either field is explicitly @Include annotated or inherit by the @Package annotation from the class
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

                if (isJSONPrimitive(field.getType())) {
                    packTo(pack, dir, field.getName(), field.get(packObject));
                } else {
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

    private static boolean isPartOfScope(String scope, String[] packageScopes) {
        System.out.println(scope);
        for (String packageScope : packageScopes) {
            String[] dirs = packageScope.split(DIR_SPLIT);
            for (String dir : dirs) {
                if (dir.equals(scope)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void packTo(JSONObject origin, String targetDir, String defaultFieldName, Object value) {
        if (targetDir.equals("")) {
            origin.put(defaultFieldName, value);
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
                if (dir.equals(DIR_SPLIT)) {
                    // Scope identifier ends with / -> use attribute name as key
                    fulfill.put(defaultFieldName, value);
                } else {
                    fulfill.put(dir, value);
                }
            } else if (!fulfill.has(dir)) {
                fulfill.put(dir, new JSONObject());
                fulfill = fulfill.getJSONObject(dir);
            } else if (fulfill.has(dir)) {
                fulfill = fulfill.getJSONObject(dir);
            }
        }
    }
}

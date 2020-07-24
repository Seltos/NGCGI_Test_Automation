package web_selenium.serialization.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.reflect.Field;

/**
 * Avoids serialization errors when duplicate fields exist in the inheritance
 * hierarchy. GSON will serialize all fields, including private fields. If the
 * same private field exists in more than one class along the inheritance hierarchy
 * of an object, this will result in a serialization error "CLASSNAME declares
 * multiple JSON fields named FIELDNAME".
 */
public class DuplicateFieldExclusionStrategy implements ExclusionStrategy {

    private boolean isFieldInSuperclass(Class<?> subclass, String fieldName) {
        Class<?> superclass = subclass.getSuperclass();
        Field field;

        while (superclass != null) {
            field = getField(superclass, fieldName);

            if (field != null) {
                return true;
            }

            superclass = superclass.getSuperclass();
        }

        return false;
    }

    private Field getField(Class<?> theClass, String fieldName) {
        try {
            return theClass.getDeclaredField(fieldName);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        String fieldName = fieldAttributes.getName();
        Class<?> theClass = fieldAttributes.getDeclaringClass();

        return isFieldInSuperclass(theClass, fieldName);
    }
}

package com.crpt.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * An object mapper which loads objects from JSON resource files. Useful for snapshot-testing.
 *
 * @param <T> The type of the object to load.
 */
public class JsonResourceObjectMapper<T> {

    private final TypeToken<T> typeToken;

    public JsonResourceObjectMapper(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    public T loadTestJson(String fileName) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException(
                    String.format("Resource '%s' not found on classpath!", fileName));
        }
        return new GsonBuilder()
                .create()
                .fromJson(new InputStreamReader(inputStream), typeToken.getType());
    }
}
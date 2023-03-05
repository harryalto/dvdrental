package com.reactive.demo.dvdrental;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

public interface DictionarySupport<T extends Enum<T>> {

    @SuppressWarnings("unchecked")
    Map<Class<?>, Map<String, Object>> byCodeMap = LazyMap.lazyMap(new HashMap(), (Factory) HashMap::new);

    @SuppressWarnings("unchecked")
    Map<Class<?>, Map<Object, String>> byEnumMap = LazyMap.lazyMap(new HashMap(), (Factory) HashMap::new);


    default void init(String code) {
        byCodeMap.get(this.getClass()).put(code, this);
        byEnumMap.get(this.getClass()).put(this, code);
    }

    static <T extends Enum<T>> T getByCode(Class<T> clazz, String code) {
        clazz.getEnumConstants();
        return (T) byCodeMap.get(clazz).get(code);
    }

    default <T extends Enum<T>> String getCode() {
        return byEnumMap.get(this.getClass()).get(this);
    }
}
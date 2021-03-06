package com.azure.common.implementation.serializer.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.azure.common.implementation.serializer.SerializerEncoding;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JacksonAdapterTests {
    @Test
    public void emptyMap() throws IOException {
        final Map<String,String> map = new HashMap<>();
        final JacksonAdapter serializer = new JacksonAdapter();
        assertEquals("{}", serializer.serialize(map, SerializerEncoding.JSON));
    }

    @Test
    public void mapWithNullKey() throws IOException {
        final Map<String,String> map = new HashMap<>();
        map.put(null, null);
        final JacksonAdapter serializer = new JacksonAdapter();
        assertEquals("{}", serializer.serialize(map, SerializerEncoding.JSON));
    }

    @Test
    public void mapWithEmptyKeyAndNullValue() throws IOException {
        final MapHolder mapHolder = new MapHolder();
        mapHolder.map = new HashMap<>();
        mapHolder.map.put("", null);

        final JacksonAdapter serializer = new JacksonAdapter();
        assertEquals("{\"map\":{\"\":null}}", serializer.serialize(mapHolder, SerializerEncoding.JSON));
    }

    @Test
    public void mapWithEmptyKeyAndEmptyValue() throws IOException {
        final MapHolder mapHolder = new MapHolder();
        mapHolder.map = new HashMap<>();
        mapHolder.map.put("", "");
        final JacksonAdapter serializer = new JacksonAdapter();
        assertEquals("{\"map\":{\"\":\"\"}}", serializer.serialize(mapHolder, SerializerEncoding.JSON));
    }

    @Test
    public void mapWithEmptyKeyAndNonEmptyValue() throws IOException {
        final Map<String,String> map = new HashMap<>();
        map.put("", "test");
        final JacksonAdapter serializer = new JacksonAdapter();
        assertEquals("{\"\":\"test\"}", serializer.serialize(map, SerializerEncoding.JSON));
    }

    private static class MapHolder {
        @JsonInclude(content = JsonInclude.Include.ALWAYS)
        public Map<String,String> map = new HashMap<>();
    }
}

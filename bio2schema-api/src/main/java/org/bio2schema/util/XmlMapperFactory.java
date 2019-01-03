package org.bio2schema.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/*
 * A custom deserializer to construct a JSON array from multi-elements in XML. Original code was
 * posted at https://gist.github.com/joaovarandas/1543e792ed6204f0cf5fe860cb7d58ed
 */
public final class XmlMapperFactory {

  @SuppressWarnings({"deprecation", "serial", "unchecked", "rawtypes"})
  public static XmlMapper createMapper() {
    SimpleModule module =
        new SimpleModule().addDeserializer(Object.class, new UntypedObjectDeserializer() {
          @Override
          protected Object mapObject(JsonParser p, DeserializationContext ctxt) throws IOException {
            String firstKey;
            JsonToken t = p.getCurrentToken();
            if (t == JsonToken.START_OBJECT) {
              firstKey = p.nextFieldName();
            } else if (t == JsonToken.FIELD_NAME) {
              firstKey = p.getCurrentName();
            } else {
              if (t != JsonToken.END_OBJECT) {
                throw ctxt.mappingException(handledType(), p.getCurrentToken());
              }
              firstKey = null;
            }
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>(2);
            if (firstKey == null) {
              return resultMap;
            }

            p.nextToken();
            resultMap.put(firstKey, deserialize(p, ctxt));

            Set<String> listKeys = new LinkedHashSet<>();
            String nextKey;
            while ((nextKey = p.nextFieldName()) != null) {
              p.nextToken();
              if (resultMap.containsKey(nextKey)) {
                Object listObject = resultMap.get(nextKey);
                if (!(listObject instanceof List)) {
                  listObject = new ArrayList<>();
                  ((List) listObject).add(resultMap.get(nextKey));
                  resultMap.put(nextKey, listObject);
                }
                ((List) listObject).add(deserialize(p, ctxt));
                listKeys.add(nextKey);
              } else {
                resultMap.put(nextKey, deserialize(p, ctxt));
              }
            }
            return resultMap;
          }
        });
    return (XmlMapper) new XmlMapper().registerModule(module);
  }
}

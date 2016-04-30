package org.aprestos.labs.ee.ws.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

public class BaseSerializer implements Serializer {
    
    /* (non-Javadoc)
     * @see org.aprestos.labs.ee.ws.serializer.Serializer#deserialize(byte[])
     */
    @Override
    public JsonObject deserialize(byte[] content) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content);
             JsonReader reader = Json.createReader(bais)) {
            return reader.readObject();
        }
    }

  /* (non-Javadoc)
 * @see org.aprestos.labs.ee.ws.serializer.Serializer#serialize(javax.json.JsonObject)
 */
@Override
public byte[] serialize(JsonObject object) throws IOException {
        try (ByteArrayOutputStream oos = new ByteArrayOutputStream(); 
		     JsonWriter writer = Json.createWriter(oos)) {
            writer.writeObject(object);
            writer.close();
            oos.flush();
            return oos.toByteArray();
        }
    }
  
}

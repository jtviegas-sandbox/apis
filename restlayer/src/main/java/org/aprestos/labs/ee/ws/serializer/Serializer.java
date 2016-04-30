package org.aprestos.labs.ee.ws.serializer;

import java.io.IOException;

import javax.json.JsonObject;

public interface Serializer {

    JsonObject deserialize(byte[] content) throws IOException;

    byte[] serialize(JsonObject object) throws IOException;

}
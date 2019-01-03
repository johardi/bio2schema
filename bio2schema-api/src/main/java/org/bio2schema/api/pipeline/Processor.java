package org.bio2schema.api.pipeline;

import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;

public interface Processor {

  JsonNode process(JsonNode input) throws IOException;
}

package org.bio2schema;

import java.nio.file.Path;
import com.fasterxml.jackson.databind.JsonNode;

interface ResultObject {

  Path getInputLocation();

  JsonNode getContent() throws Exception;

}
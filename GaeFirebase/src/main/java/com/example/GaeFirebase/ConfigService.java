package com.example.GaeFirebase;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigService {

  public static List<RouteSpec> GetRouteSpecs(File file, String env)
      throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    List<EnvSpec> envSpecs =
        (List<EnvSpec>) mapper.readValue(file, new TypeReference<List<EnvSpec>>() {});
    return GetRouteSpecsForEnv(envSpecs, env);
  }

  public static List<RouteSpec> GetRouteSpecs(String jsonString, String env)
      throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    List<EnvSpec> envSpecs =
        (List<EnvSpec>) mapper.readValue(jsonString, new TypeReference<List<EnvSpec>>() {});
    return GetRouteSpecsForEnv(envSpecs, env);
  }

  private static List<RouteSpec> GetRouteSpecsForEnv(List<EnvSpec> envSpecs, String env) {
    for (EnvSpec envSpec : envSpecs) {
      if (envSpec.env.equals(env)) {
        return envSpec.routes;
      }
    }
    return null;
  }
}

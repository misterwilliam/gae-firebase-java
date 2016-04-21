package com.example.GaeFirebase;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigService {

  public static class DestSpec {
    public String url;
    public String snapshotParam;

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (obj == this) {
        return true;
      }
      if (obj.getClass() != getClass()) {
        return false;
      }
      DestSpec rhs = (DestSpec) obj;
      return new EqualsBuilder().append(url, rhs.url).append(snapshotParam, rhs.snapshotParam)
          .isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(url).append(snapshotParam).toHashCode();
    }
  }

  public static class RouteSpec {
    public enum FirebaseEventType {
      value, child_added, child_removed, child_changed, child_moved
    }

    public FirebaseEventType event;
    public String src;
    public DestSpec dest;

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (obj == this) {
        return true;
      }
      if (obj.getClass() != getClass()) {
        return false;
      }
      RouteSpec rhs = (RouteSpec) obj;
      return new EqualsBuilder().append(event, rhs.event).append(src, rhs.src)
          .append(dest, rhs.dest).isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(event).append(src).append(dest).toHashCode();
    }

    public Route getRoute() throws MalformedURLException {
      return new Route(this.event, new URL(this.src), new URL(this.dest.url));
    }
  }

  public static class EnvSpec {
    public String env;
    public List<RouteSpec> routes;

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (obj == this) {
        return true;
      }
      if (obj.getClass() != getClass()) {
        return false;
      }
      EnvSpec rhs = (EnvSpec) obj;
      return new EqualsBuilder().append(env, rhs.env).append(routes, rhs.routes).isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(env).append(routes).toHashCode();
    }
  }

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

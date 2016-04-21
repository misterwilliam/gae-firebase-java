package com.example.GaeFirebase;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class EnvSpec {
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
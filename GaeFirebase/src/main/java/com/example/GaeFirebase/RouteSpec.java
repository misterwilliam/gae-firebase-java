package com.example.GaeFirebase;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RouteSpec {
  public enum FirebaseEventType {
    value, child_added, child_removed, child_changed, child_moved
  }

  public RouteSpec.FirebaseEventType event;
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
    return new EqualsBuilder().append(event, rhs.event).append(src, rhs.src).append(dest, rhs.dest)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(event).append(src).append(dest).toHashCode();
  }

  public static Route MakeRoute(RouteSpec r, HttpURLConnectionAuthenticator httpAuthenticator)
      throws MalformedURLException {
    return new Route(r.event, new URL(r.src), new URL(r.dest.url), r.dest, httpAuthenticator);
  }
}

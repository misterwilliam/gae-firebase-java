package com.example.GaeFirebase;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DestSpec {
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
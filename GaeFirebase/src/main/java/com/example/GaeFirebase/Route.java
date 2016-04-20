package com.example.GaeFirebase;

import java.net.URL;

public class Route {

  public enum FirebaseEventType {
    VALUE, CHILD_ADDED, CHILD_CHANGED, CHILD_REMOVED, CHILD_MOVED
  }

  private FirebaseEventType event;
  private URL src;
  private URL dest;

  public Route(FirebaseEventType event, URL src, URL dest) {
    this.event = event;
    this.src = src;
    this.dest = dest;
  }

  public URL getSrc() {
    return this.src;
  }

  public URL getDest() {
    return this.dest;
  }

  public FirebaseEventType getEvent() {
    return this.event;
  }

}

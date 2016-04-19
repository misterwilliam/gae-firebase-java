package com.example.GaeFirebase;

import java.net.URL;
import java.util.ArrayList;

public class Route {
  
  public enum FirebaseEventType {
    VALUE, CHILD_ADDED, CHILD_CHANGED, CHILD_REMOVED, CHILD_MOVED
  }

  private ArrayList<FirebaseEventType> events;
  private URL src;
  private URL dest;
  
  public Route(Iterable<FirebaseEventType> events, URL src, URL dest) {
    this.events = new ArrayList<FirebaseEventType>();
    for (FirebaseEventType event : events) {
      this.events.add(event);
    }
    this.src = src;
    this.dest = dest;
  }
  
  public URL getSrc() {
    return this.src;
  }
  
  public URL getDest() {
    return this.dest;
  }
  
  public ArrayList<FirebaseEventType> getEvents() {
    return this.events;
  }
  
}

package com.example.GaeFirebase;

import java.net.URL;

import com.example.GaeFirebase.RouteSpec.FirebaseEventType;

public class Route {

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

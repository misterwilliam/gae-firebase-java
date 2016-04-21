package com.example.GaeFirebase;

import java.net.URL;

import com.example.GaeFirebase.RouteSpec.FirebaseEventType;
import com.firebase.client.Firebase;

public class Route {

  private FirebaseEventType event;
  private URL src;
  private URL dest;
  private DestSpec destSpec;

  public Route(FirebaseEventType event, URL src, URL dest, DestSpec destSpec) {
    this.event = event;
    this.src = src;
    this.dest = dest;
    this.destSpec = destSpec;
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

  public void listen(Firebase src, Forwarder forwarder) {
    switch (this.event) {
      case value:
        src.addValueEventListener(new ForwardingValueEventListener(forwarder));
        break;
      default:
        src.addChildEventListener(new ForwardingChildEventListener(forwarder, this.event));
        break;
    }
  }

}

package com.example.GaeFirebase;

import java.net.URL;

import com.example.GaeFirebase.RouteSpec.FirebaseEventType;
import com.firebase.client.Firebase;

public class Route {

  private FirebaseEventType event;
  private URL src;
  private URL dest;
  private DestSpec destSpec;
  private HttpURLConnectionAuthenticator httpAuthenticator;

  public Route(FirebaseEventType event, URL src, URL dest, DestSpec destSpec,
      HttpURLConnectionAuthenticator httpAuthenticator) {
    this.event = event;
    this.src = src;
    this.dest = dest;
    this.destSpec = destSpec;
    this.httpAuthenticator = httpAuthenticator;
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

  public void listen(Firebase src) {
    Forwarder forwarder = new Forwarder(this.dest, this.httpAuthenticator,
        this.destSpec.snapshotParam, this.destSpec.previousChildNameParam);
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

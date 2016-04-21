package com.example.GaeFirebase;

import java.util.logging.Logger;

import com.example.GaeFirebase.ConfigService.RouteSpec.FirebaseEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

public class ForwardingChildEventListener implements ChildEventListener {

  private static final Logger log = Logger.getLogger(ForwardingChildEventListener.class.getName());

  Forwarder forwarder;
  FirebaseEventType eventType;

  public ForwardingChildEventListener(Forwarder forwarder, FirebaseEventType eventType) {
    this.forwarder = forwarder;
    this.eventType = eventType;
  }

  @Override
  public void onCancelled(FirebaseError error) {
    log.warning("The read failed: " + error.getMessage());
  }

  @Override
  public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
    if (this.eventType != FirebaseEventType.child_added) {
      return;
    }
    if (snapshot.exists()) {
      try {
        String json = new ObjectMapper().writeValueAsString(snapshot.getValue());
        log.info("Forwarding child added: " + json);
        this.forwarder.forward(json);
      } catch (JsonProcessingException e) {
        log.warning("Invalid JSON:" + e.getMessage());
      }
    }
  }

  @Override
  public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
    if (this.eventType != FirebaseEventType.child_changed) {
      return;
    }
    if (snapshot.exists()) {
      try {
        String json = new ObjectMapper().writeValueAsString(snapshot.getValue());
        log.info("Forwarding child changed: " + json);
        this.forwarder.forward(json);
      } catch (JsonProcessingException e) {
        log.warning("Invalid JSON:" + e.getMessage());
      }
    }
  }

  @Override
  public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
    if (this.eventType != FirebaseEventType.child_moved) {
      return;
    }
    if (snapshot.exists()) {
      try {
        String json = new ObjectMapper().writeValueAsString(snapshot.getValue());
        log.info("Forwarding child moved: " + json);
        this.forwarder.forward(json);
      } catch (JsonProcessingException e) {
        log.warning("Invalid JSON:" + e.getMessage());
      }
    }
  }

  @Override
  public void onChildRemoved(DataSnapshot snapshot) {
    if (this.eventType != FirebaseEventType.child_removed) {
      return;
    }
    if (snapshot.exists()) {
      try {
        String json = new ObjectMapper().writeValueAsString(snapshot.getValue());
        log.info("Forwarding child removed: " + json);
        this.forwarder.forward(json);
      } catch (JsonProcessingException e) {
        log.warning("Invalid JSON:" + e.getMessage());
      }
    }
  }

}

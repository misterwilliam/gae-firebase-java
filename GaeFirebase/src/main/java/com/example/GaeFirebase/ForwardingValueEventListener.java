package com.example.GaeFirebase;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ForwardingValueEventListener implements ValueEventListener {

  private static final Logger log = Logger.getLogger(ForwardingValueEventListener.class.getName());

  Forwarder forwarder;

  public ForwardingValueEventListener(Forwarder forwarder) {
    this.forwarder = forwarder;
  }

  @Override
  public void onDataChange(DataSnapshot snapshot) {
    if (snapshot.exists()) {
      try {
        String json = new ObjectMapper().writeValueAsString(snapshot.getValue());
        log.info("Forwarding: " + json);
        this.forwarder.forward(json);
      } catch (JsonProcessingException e) {
        log.warning("Invalid JSON:" + e.getMessage());
      }
    }
  }

  @Override
  public void onCancelled(FirebaseError firebaseError) {
    log.warning("The read failed: " + firebaseError.getMessage());
  }
}

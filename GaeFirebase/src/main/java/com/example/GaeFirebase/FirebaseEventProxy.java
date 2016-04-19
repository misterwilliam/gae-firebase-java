package com.example.GaeFirebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.security.token.TokenGenerator;

public class FirebaseEventProxy {

  private HttpURLConnectionAuthenticator connectionAuthenticator;
  private String firebaseAuthToken;
  private ArrayList<Route> routes;
  
  public FirebaseEventProxy(Iterable<Route> routes) {
    this.routes = new ArrayList<Route>();
    for (Route route : routes) {
      this.routes.add(route);
    }
    
    this.connectionAuthenticator =
        HttpURLConnectionAuthenticator.getDefaultConnectionAuthenticator();
    Properties props = this.getConfigProperties("secrets.properties");
    this.firebaseAuthToken = this.getFirebaseAuthToken(props.getProperty("firebaseSecret"));
  }

  public void subscribe() {
    for (Route route : this.routes) {
      this.subscribeToRoute(route);
    }
  }

  private void subscribeToRoute(Route route) {
    final FirebaseEventProxy self = this;
    final URL dest = route.getDest();
    Firebase src =
        this.getAuthenticatedFirebaseClient(route.getSrc().toString(), this.firebaseAuthToken);
    for (Route.FirebaseEventType eventType : route.getEvents()) {
      switch (eventType) {
        case VALUE:
          src.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
              if (snapshot.exists()) {
                try {
                  String json = new ObjectMapper().writeValueAsString(snapshot.getValue());
                  System.out.println("Forwarding: " + json);
                  self.forwardToDest(json, dest);
                } catch (JsonProcessingException e) {
                  System.out.println("Invalid JSON:" + e.getMessage());
                }
              }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
              System.out.println("The read failed: " + firebaseError.getMessage());
            }
          });
      }
    }
  }

  private void forwardToDest(String message, URL dest) {
    try {
      System.out.println("Endpoint:" + dest.toString());
      HttpURLConnection connection = (HttpURLConnection) dest.openConnection();
      connection.setRequestMethod("POST");
      this.connectionAuthenticator.authenticate(connection);
      Map<String, Object> params = new HashMap<>();
      params.put("data", message);
      connection.setDoOutput(true);
      OutputStream output = connection.getOutputStream();
      output.write(this.encodeParams(params));
      int responseCode = connection.getResponseCode();
      if (responseCode != 200) {
        System.out.println("Forwarding failed");
      }
    } catch (MalformedURLException e) {
      System.out.println("Malformed URL: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }

  private byte[] encodeParams(Map<String, Object> params) throws UnsupportedEncodingException {
    StringBuilder postData = new StringBuilder();
    for (Map.Entry<String, Object> param : params.entrySet()) {
      if (postData.length() != 0)
        postData.append('&');
      postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
      postData.append('=');
      postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
    }
    return postData.toString().getBytes("UTF-8");
  }

  private String getFirebaseAuthToken(String secret) {
    Map<String, Object> authPayload = new HashMap<String, Object>();
    authPayload.put("uid", "gae-instance");
    authPayload.put("provider", "gae");
    TokenGenerator tokenGenerator = new TokenGenerator(secret);
    return tokenGenerator.createToken(authPayload);
  }

  private Firebase getAuthenticatedFirebaseClient(String endpointUrl, String token) {
    Firebase firebase = new Firebase(endpointUrl);
    firebase.authWithCustomToken(token, new Firebase.AuthResultHandler() {

      @Override
      public void onAuthenticationError(FirebaseError arg0) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onAuthenticated(AuthData arg0) {
        // TODO Auto-generated method stub
      }
    });
    return firebase;
  }

  private Properties getConfigProperties(String path) {
    Properties props = new Properties();
    try {
      InputStream inputStream = new FileInputStream(path);
      props.load(inputStream);
    } catch (java.net.MalformedURLException e) {
      throw new RuntimeException("Invalid path: " + path);
    } catch (IOException e) {
      throw new RuntimeException(
          "Error reading file (likely under root /GaeFirebase/src/webapp/): " + path);
    }
    return props;
  }
}

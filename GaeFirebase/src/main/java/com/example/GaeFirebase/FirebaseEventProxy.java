package com.example.GaeFirebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.security.token.TokenGenerator;

public class FirebaseEventProxy {
  
  private Firebase fbRef;
  private HttpURLConnectionAuthenticator connectionAuthenticator;
  private ArrayList<URL> forwardEndpoints;

  public FirebaseEventProxy(ServletContext context, Iterable<String> forwardEndpoints) throws MalformedURLException {
    this.forwardEndpoints = new ArrayList<URL>();
    for (String url : forwardEndpoints) {
      this.forwardEndpoints.add(new URL(url));
    }
    this.connectionAuthenticator = HttpURLConnectionAuthenticator.getDefaultConnectionAuthenticator();
    Properties props = this.getConfigProperties(context, "/secrets.properties");
    String authToken = this.getFirebaseAuthToken(props.getProperty("firebaseSecret"));
    this.fbRef = this.getAuthenticatedFirebaseClient("fb-channel", authToken);
  }
  
  public void subscribe() {
    final FirebaseEventProxy self = this;

    this.fbRef.child("clients").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        System.out.println(snapshot.getValue());
        self.sendMessageToGaeApp("hi");
      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {
        System.out.println("The read failed: " + firebaseError.getMessage());
      }
    });
  }
  
  private void sendMessageToGaeApp(String message) {
    try {
      for (URL endpoint : this.forwardEndpoints) {
        HttpURLConnection connection = (HttpURLConnection)endpoint.openConnection();
        this.connectionAuthenticator.authenticate(connection);
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = reader.readLine()) != null) {
          // ...
        }
        reader.close();
      }
    } catch (MalformedURLException e) {
      System.out.println("Malformed URL");
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
  
  private String getFirebaseAuthToken(String secret) {
    Map<String, Object> authPayload = new HashMap<String, Object>();
    authPayload.put("uid", "gae-instance");
    authPayload.put("provider", "gae");
    TokenGenerator tokenGenerator = new TokenGenerator(secret);
    return tokenGenerator.createToken(authPayload);
  }
  
  private Firebase getAuthenticatedFirebaseClient(String firebaseProjectId, String token) {
    Firebase firebase = new Firebase("https://" + firebaseProjectId + ".firebaseio.com/");
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
  
  private Properties getConfigProperties(ServletContext context, String path) {
    Properties props = new Properties();
    try {
      InputStream inputStream = context.getResourceAsStream(path);
      if (inputStream == null) {
        throw new RuntimeException("Missing file /GaeFirebase/src/webapp/" + path);
      }
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

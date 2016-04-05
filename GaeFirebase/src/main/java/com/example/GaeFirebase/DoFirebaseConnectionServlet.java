package com.example.GaeFirebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.security.token.TokenGenerator;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class DoFirebaseConnectionServlet extends HttpServlet {

  Firebase fbRef;
  GoogleCredential credential;

  @Override
  public void init() throws ServletException {
    this.credential = this.getGoogleCredential();
    Properties props = this.getConfigProperties("/secrets.properties");
    String authToken = this.getFirebaseAuthToken(props.getProperty("firebaseSecret"));
    this.fbRef = this.getAuthenticatedFirebaseClient("fb-channel", authToken);
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    resp.getWriter().println("Hello, this is a testing servlet. \n\n");
    
    final DoFirebaseConnectionServlet self = this;

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
      URL url = new URL("http://localhost:9000/api/_presence/gae");
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      System.out.println("Token: " + this.credential.getAccessToken());
      connection.setRequestProperty("Authorization", "Bearer " + this.credential.getAccessToken());
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;

      while ((line = reader.readLine()) != null) {
        // ...
      }
      reader.close();

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

  private Properties getConfigProperties(String path) {
    Properties props = new Properties();
    try {
      ServletContext context = this.getServletContext();
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
  
  private GoogleCredential getGoogleCredential() {
    // Get authentication for service account associated with this app.
    try{
      GoogleCredential credential = GoogleCredential.getApplicationDefault();
      credential = credential.createScoped(
          Collections.singletonList("https://www.googleapis.com/auth/userinfo.email"));
      credential.refreshToken();
      return credential;
    } catch (IOException e) {
      System.err.println(
          "Unable to retrieve service account credentials. Expecting path to service account " +
          "credentials in the environment variable GOOGLE_APPLICATION_CREDENTIALS: " + e.getMessage());
    }
    return null;
  }
}

package com.example.GaeFirebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class AuthenticatedHttpURLConnection {
  
  private final static String CREDENTIAL_FILENAME = "application_default_credentials.json";
  private GoogleCredential credential;

  public AuthenticatedHttpURLConnection() {
    // Authenticate
    try {
      this.credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIAL_FILENAME));
      this.credential = credential.createScoped(
        Collections.singletonList("https://www.googleapis.com/auth/userinfo.email"));
      this.credential.refreshToken();
    } catch (IOException e) {
      System.err.println(
          "Unable to retrieve service account credentials. Expecting them to be in " +
          "GaeFirebase/src/main/webapp/application_default_credentials.json: " + e.getMessage());
    }
  }
  
  public InputStream get(URL url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    String token = this.credential.getAccessToken();
    connection.setRequestProperty("Authorization", "Bearer " + token);
    return connection.getInputStream();
  }
}

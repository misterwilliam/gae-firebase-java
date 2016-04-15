package com.example.GaeFirebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;

public class HttpURLConnectionAuthenticator {
  
  private final static String CREDENTIAL_FILENAME = "application_default_credentials.json";
  private GoogleCredential credential;

  public static HttpURLConnectionAuthenticator getDefaultConnectionAuthenticator() {
    try {
      return new HttpURLConnectionAuthenticator(new FileInputStream(CREDENTIAL_FILENAME));
    } catch (IOException e) {
      System.err.println(
          "Unable to retrieve service account credentials. Expecting them to be in " +
          "GaeFirebase/src/main/webapp/application_default_credentials.json: " + e.getMessage());
    }
    return null;
  }
  
  public HttpURLConnectionAuthenticator(FileInputStream fileInputStream) throws IOException {
    this(GoogleCredential.fromStream(fileInputStream));
  }
  
  public HttpURLConnectionAuthenticator(GoogleCredential googleCredential) {
    this.credential = googleCredential.createScoped(
        Collections.singletonList("https://www.googleapis.com/auth/userinfo.email"));
    try {
      this.credential.refreshToken();
    } catch (IOException e) {
      System.err.println("Unable to refresh token");
    }
  }
 
  public void authenticate(HttpURLConnection connection) {
    String token = this.credential.getAccessToken();
    connection.setRequestProperty("Authorization", "Bearer " + token);
  }
}

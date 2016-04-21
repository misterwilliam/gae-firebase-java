package com.example.GaeFirebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.security.token.TokenGenerator;

public class FirebaseEventProxy {

  private String firebaseAuthToken;
  private ArrayList<Route> routes;

  public FirebaseEventProxy(Iterable<RouteSpec> routeSpecs) throws MalformedURLException {
    HttpURLConnectionAuthenticator connectionAuthenticator =
        HttpURLConnectionAuthenticator.getDefaultConnectionAuthenticator();
    Properties props = this.getConfigProperties("secrets.properties");
    this.firebaseAuthToken = this.getFirebaseAuthToken(props.getProperty("firebaseSecret"));

    this.routes = new ArrayList<Route>();
    for (RouteSpec routeSpec : routeSpecs) {
      routes.add(RouteSpec.MakeRoute(routeSpec, connectionAuthenticator));
    }
  }

  public void subscribe() {
    for (Route route : this.routes) {
      this.subscribeToRoute(route);
    }
  }

  private void subscribeToRoute(Route route) {
    Firebase src =
        this.getAuthenticatedFirebaseClient(route.getSrc().toString(), this.firebaseAuthToken);
    route.listen(src);
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

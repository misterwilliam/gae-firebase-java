package com.example.GaeFirebase;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.security.token.TokenGenerator;

public class DoFirebaseConnectionServlet extends HttpServlet {

  private Properties props;

  @Override
  public void init() throws ServletException {
    this.props = this.getConfigProperties("/secrets.properties");
    String authToken = this.getFirebaseAuthToken(this.props.getProperty("firebaseSecret"));
    System.out.println(authToken);
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    resp.getWriter().println("Hello, this is a testing servlet. \n\n");

    Firebase fbRef = new Firebase("https://fb-channel.firebaseio.com/");
    fbRef.child("clients").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        System.out.println(snapshot.getValue());
      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {
        System.out.println("The read failed: " + firebaseError.getMessage());
      }
    });
  }

  private String getFirebaseAuthToken(String secret) {
    Map<String, Object> authPayload = new HashMap<String, Object>();
    authPayload.put("uid", "gae-java-presence-listener");
    authPayload.put("provider", "gae");
    TokenGenerator tokenGenerator = new TokenGenerator(secret);
    return tokenGenerator.createToken(authPayload);
  }

  private Firebase getAuthenticatedFirebaseClient(String firebaseProjectId) {
    Firebase firebase = new Firebase("https://" + firebaseProjectId + ".firebaseio.com/");
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
}

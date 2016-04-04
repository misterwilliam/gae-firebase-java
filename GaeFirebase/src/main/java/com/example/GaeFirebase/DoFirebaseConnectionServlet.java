package com.example.GaeFirebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoFirebaseConnectionServlet extends HttpServlet {

  @Override
  public void init() throws ServletException {
    Properties props = this.getConfigProperties("/secrets.properties");
    System.out.println(props.getProperty("firebaseSecret"));
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
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
      throw new RuntimeException("Error reading file (likely under root /GaeFirebase/src/webapp/): " + path);
    }
    return props;
  }
}

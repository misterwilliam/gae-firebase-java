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
    ServletContext context = this.getServletContext();
    try {
      InputStream inputStream = context.getResourceAsStream(
        "/secrets.properties");
      if (inputStream == null) {
        System.out.println("No file there");
      } else {
        Properties props = new Properties();
        props.load(inputStream);
        System.out.println(props.getProperty("firebaseSecret"));
      }
    } catch (java.net.MalformedURLException e) {
      System.out.println("Invalid path to secrets: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("Input stream error: " + e.getMessage());
    }
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
}

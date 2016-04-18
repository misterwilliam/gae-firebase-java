package com.example.GaeFirebase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoFirebaseConnectionServlet extends HttpServlet {

  FirebaseEventProxy firebaseEventProxy;

  @Override
  public void init() throws ServletException {
    try {
      this.firebaseEventProxy = new FirebaseEventProxy(
          Collections.singletonList(System.getProperty("GaeFireabseProxy.watch.endpoint")),
          Collections.singletonList("http://gae-firebase-c4d3c.appspot.com/api/_presence/gae"));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    resp.getWriter().println("Hello, this is a testing servlet. \n\n");
    
    this.firebaseEventProxy.subscribe();
  }
 
}

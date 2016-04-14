package com.example.GaeFirebase;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoFirebaseConnectionServlet extends HttpServlet {

  FirebaseEventProxy firebaseEventProxy;

  @Override
  public void init() throws ServletException {
    this.firebaseEventProxy = new FirebaseEventProxy(this.getServletContext());
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    resp.getWriter().println("Hello, this is a testing servlet. \n\n");
    
    this.firebaseEventProxy.subscribe();
  }
 
}

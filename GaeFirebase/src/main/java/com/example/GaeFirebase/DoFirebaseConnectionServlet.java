package com.example.GaeFirebase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.utils.SystemProperty;

public class DoFirebaseConnectionServlet extends HttpServlet {

  FirebaseEventProxy firebaseEventProxy;

  @Override
  public void init() throws ServletException {
    try {
      Iterable<String> srcUrls;
      Iterable<String> destUrls;
      if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
        srcUrls = this.getSpaceSeparatedArgs(
            System.getProperty("GaeFireabseProxy.prod.src.urls", ""));
        destUrls = this.getSpaceSeparatedArgs(
            System.getProperty("GaeFireabseProxy.prod.dest.urls", ""));
      } else {
        srcUrls = this.getSpaceSeparatedArgs(
            System.getProperty("GaeFireabseProxy.dev.src.urls", ""));
        destUrls = this.getSpaceSeparatedArgs(
            System.getProperty("GaeFireabseProxy.dev.dest.urls", ""));
      }

      this.firebaseEventProxy = new FirebaseEventProxy(srcUrls, destUrls);
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
  
  private Iterable<String> getSpaceSeparatedArgs(String args) {
    return Arrays.asList(args.split(" "));
  }
 
}

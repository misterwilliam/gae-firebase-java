package com.example.GaeFirebase;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoFirebaseConnectionServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    resp.setContentType("text/plain");
    resp.getWriter().println("Hello, this is a testing servlet. \n\n");
    Properties p = System.getProperties();
    p.list(resp.getWriter());
  }
}

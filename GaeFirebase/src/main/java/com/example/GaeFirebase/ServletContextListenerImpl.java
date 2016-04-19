package com.example.GaeFirebase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.appengine.api.utils.SystemProperty;

public class ServletContextListenerImpl implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      Iterable<String> srcUrls;
      Iterable<String> destUrls;
      if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
        srcUrls =
            this.getSpaceSeparatedArgs(System.getProperty("GaeFirebaseProxy.prod.src.urls", ""));
        destUrls =
            this.getSpaceSeparatedArgs(System.getProperty("GaeFirebaseProxy.prod.dest.urls", ""));
      } else {
        srcUrls =
            this.getSpaceSeparatedArgs(System.getProperty("GaeFirebaseProxy.dev.src.urls", ""));
        destUrls =
            this.getSpaceSeparatedArgs(System.getProperty("GaeFirebaseProxy.dev.dest.urls", ""));
      }

      ArrayList<Route> routes = new ArrayList<Route>();
      for (String src : srcUrls) {
        for (String dest : destUrls) {
          routes.add(new Route(Collections.singletonList(Route.FirebaseEventType.VALUE),
              new URL(src), new URL(dest)));
        }
      }

      FirebaseEventProxy firebaseEventProxy = new FirebaseEventProxy(routes);
      firebaseEventProxy.subscribe();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    // App Engine does not currently invoke this method.
  }

  private Iterable<String> getSpaceSeparatedArgs(String args) {
    return Arrays.asList(args.split(" "));
  }
}

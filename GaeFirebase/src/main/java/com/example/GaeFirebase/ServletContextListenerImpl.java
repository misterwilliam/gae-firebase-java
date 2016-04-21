package com.example.GaeFirebase;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.appengine.api.utils.SystemProperty;

public class ServletContextListenerImpl implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      String env;
      if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
        env = "prod";
      } else {
        env = "dev";
      }

      List<RouteSpec> routeSpecs =
          ConfigService.GetRouteSpecs(new File("firebase_event_proxy_config.json"), env);
      FirebaseEventProxy firebaseEventProxy = new FirebaseEventProxy(routeSpecs);
      firebaseEventProxy.subscribe();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    // App Engine does not currently invoke this method.
  }
}

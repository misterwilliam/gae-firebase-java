package com.example.GaeFirebase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LoadConfigServiceTest {

  @Test
  public void testConfigServiceLoad() {
    try {
      // @formatter:off
      List<RouteSpec> routeSpecs = ConfigService.GetRouteSpecs(
          "[" 
        + " {"
        + "   \"env\": \"dev\","
        + "   \"routes\": "
        + "   ["
        + "       {"
        + "           \"event\": \"child_added\","
        + "           \"src\": \"https://dev.src\","
        + "           \"dest\": "
        + "           {"
        + "               \"url\": \"http://dev.dest\","
        + "               \"snapshotParam\": \"from\""
        + "           }"
        + "       },"
        + "       {"
        + "           \"event\": \"child_removed\","
        + "           \"src\": \"https://dev.src\","
        + "           \"dest\": "
        + "           {"
        + "               \"url\": \"http://dev.dest\","
        + "               \"snapshotParam\": \"from\""
        + "           }"
        + "       }"
        + "   ]"
        + "},"
        + "{"
        + "   \"env\": \"prod\","
        + "   \"routes\": "
        + "   ["
        + "       {"
        + "           \"event\": \"value\","
        + "           \"src\": \"https://prod.src\","
        + "           \"dest\": "
        + "           {"
        + "               \"url\": \"https://prod.dest\","
        + "               \"snapshotParam\": \"from\""
        + "           }"
        + "       }"
        + "   ]"
        + "}"
        + "]", "prod");
      // @formatter:on
      List<RouteSpec> expectedRouteSpecs = new ArrayList<RouteSpec>();
      RouteSpec firstRoute = new RouteSpec();
      firstRoute.event = RouteSpec.FirebaseEventType.value;
      firstRoute.src = "https://prod.src";
      firstRoute.dest = new DestSpec();
      firstRoute.dest.url = "https://prod.dest";
      firstRoute.dest.snapshotParam = "from";
      expectedRouteSpecs.add(firstRoute);

      assertNotNull(routeSpecs);
      assertEquals(routeSpecs, expectedRouteSpecs);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

}


package com.example.GaeFirebase;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Forwarder {

  private static final Logger log = Logger.getLogger(Forwarder.class.getName());

  URL dest;
  HttpURLConnectionAuthenticator authenticator;

  public Forwarder(URL dest, HttpURLConnectionAuthenticator authenticator) {
    this.dest = dest;
    this.authenticator = authenticator;
  }

  public void forward(String message) {
    try {
      HttpURLConnection connection = (HttpURLConnection) dest.openConnection();
      connection.setRequestMethod("POST");
      this.authenticator.authenticate(connection);

      Map<String, Object> params = new HashMap<>();
      params.put("data", message);
      connection.setDoOutput(true);
      OutputStream output = connection.getOutputStream();
      output.write(this.encodeParams(params));
      int responseCode = connection.getResponseCode();
      if (responseCode != 200) {
        log.warning("Forwarding failed");
      }
    } catch (MalformedURLException e) {
      log.warning("Malformed URL: " + e.getMessage());
    } catch (IOException e) {
      log.warning("IOException: " + e.getMessage());
    }
  }

  private byte[] encodeParams(Map<String, Object> params) throws UnsupportedEncodingException {
    StringBuilder postData = new StringBuilder();
    for (Map.Entry<String, Object> param : params.entrySet()) {
      if (postData.length() != 0)
        postData.append('&');
      postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
      postData.append('=');
      postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
    }
    return postData.toString().getBytes("UTF-8");
  }
}

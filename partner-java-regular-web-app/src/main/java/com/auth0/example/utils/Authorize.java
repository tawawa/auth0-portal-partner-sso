package com.auth0.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Authorize {

  private static final Logger logger = LogManager.getLogger(Authorize.class);

  public static String buildUrl(final String domain, final String clientId, final String callbackUrl,
      final String scope, final String state, final boolean silentAuth) {
      final StringBuilder builder = new StringBuilder("https://").append(domain).append("/authorize?response_type=code&client_id=")
        .append(clientId).append("&redirect_uri=").append(callbackUrl).append("&state=").append(state).append("&scope=").append(scope);
      if (silentAuth) {
        builder.append("&prompt=none");
      }
      final String url = builder.toString();
      logger.debug(url);
      return url;
  }

}

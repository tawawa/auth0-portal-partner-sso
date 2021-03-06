package com.auth0.example;

import com.auth0.example.utils.Authorize;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.Auth0User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.auth0.NonceUtils;
import com.auth0.SessionUtils;

public class CheckController extends BaseController {

  private static final long serialVersionUID = 1L;

  private static final Logger logger = LogManager.getLogger(CheckController.class);

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
  }

  protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
    logger.info("CheckController page");
    NonceUtils.addNonceToStorage(req);
    final String state = SessionUtils.getState(req);
    final String url = Authorize.buildUrl(this.domain, this.clientId, this.callbackUrl, this.scope, state, true);
    res.sendRedirect(url);
  }

}

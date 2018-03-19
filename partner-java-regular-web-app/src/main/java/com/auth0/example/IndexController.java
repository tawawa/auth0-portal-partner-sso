package com.auth0.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IndexController extends BaseController {

  private static final Logger logger = LogManager.getLogger(IndexController.class);

  protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
    logger.info("IndexController page");
    this.setupRequest(req);
    logger.debug("loggedIn: " + req.getAttribute("loggedIn"));
    logger.debug("partnerLoginEndpoint: " + req.getAttribute("partnerLoginEndpoint"));
    req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, res);
  }

}

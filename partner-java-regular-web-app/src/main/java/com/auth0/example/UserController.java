package com.auth0.example;

import com.auth0.AuthRequestWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserController extends BaseController {

  private static final Logger logger = LogManager.getLogger(UserController.class);

  protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
    logger.info("UserController page");
    final AuthRequestWrapper requestWrapper = (AuthRequestWrapper) req;
    if (! requestWrapper.isAuthenticated()) {
      res.sendRedirect("/");
    } else {
      setupRequest(req);
      req.getRequestDispatcher("/WEB-INF/jsp/user.jsp").forward(req, res);
    }
  }


}

package com.auth0.example;

import com.auth0.Auth0User;
import com.auth0.SessionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginController extends BaseController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {

      logger.debug("LoginController");

      final Auth0User user = SessionUtils.getAuth0User(req);
      final boolean loggedIn = user != null;

      if (loggedIn) {
        res.sendRedirect("/");
      } else {
        if (req.getSession() != null) {
            req.getSession().invalidate();
        }
        final String loginUrl = this.portalLoginUrl + "?externalReturnUrl=" + this.externalReturnUrl;
        logger.debug(loginUrl);
        res.sendRedirect(loginUrl);
      }
   }
}

package com.auth0;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.auth.UserInfo;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static java.util.Arrays.asList;

/**
 * The Servlet endpoint used as the callback handler in the Oauth2
 * authorization code grant flow. This servlet is called back via a
 * redirect from Auth0 (IdP) post authentication supplying an authorization code
 */
public class Auth0CallbackHandler extends HttpServlet {

    protected Properties properties = new Properties();
    protected String redirectOnSuccess;
    protected String redirectOnFail;
    protected AuthAPI auth0Client;
    protected String audience;
    protected String scope;

    /**
     * Initialize this servlet with required configuration
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        redirectOnSuccess = readParameter("auth0.redirect_on_success", config);
        redirectOnFail = readParameter("auth0.redirect_on_error", config);
        for (String param : asList("auth0.client_id", "auth0.client_secret", "auth0.exchange_domain", "auth0.audience", "auth0.scope")) {
            properties.put(param, readParameter(param, config));
        }
        final String clientId = (String) properties.get("auth0.client_id");
        final String clientSecret = (String) properties.get("auth0.client_secret");
        final String domain = (String) properties.get("auth0.exchange_domain");
        this.audience = (String) properties.get("auth0.audience");
        this.scope = (String) properties.get("auth0.scope");
        this.auth0Client = new AuthAPI(domain, clientId, clientSecret);
    }

    /**
     * Entrypoint for http request
     *
     * 1). Responsible for validating the request and ensuring
     * the nonce value in session storage matches the nonce value passed to this endpoint.
     * 2). Exchanging the authorization code received with this http request for tokens
     * 3). Getting user profile information using id token
     * 4). Storing both tokens and user profile information into session storage
     * 5). Clearing the stored nonce value out of state storage
     * 6). Handling success and any failure outcomes
     */
    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse res)
            throws IOException, ServletException {
        try {
            if (isValidRequest(req)) {
                final TokenHolder tokenHolder = fetchTokens(req);
                final String idToken = tokenHolder.getIdToken();
                final String accessToken = tokenHolder.getAccessToken();
                final Tokens tokens = new Tokens(idToken, accessToken, null, null);
                final UserInfo userInfo = getUserInfo(accessToken);
                final Map<String, Object> attributes = userInfo.getValues();
                final Auth0User auth0User = new Auth0User(attributes);
                store(tokens, auth0User, req);
                NonceUtils.removeNonceFromStorage(req);
                onSuccess(req, res);
            } else {
                onFailure(req, res, new IllegalStateException("Invalid state or error"));
            }
        } catch (RuntimeException ex) {
            onFailure(req, res, ex);
        }
    }

    /**
     * Actions / navigation to take when a request is deemed successful by this callback handler
     */
    protected void onSuccess(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + redirectOnSuccess);
    }

    /**
     * Actions / navigation to take when a request is deemed unsuccessful by this callback handler
     */
    protected void onFailure(final HttpServletRequest req, final HttpServletResponse res,
                             Exception ex) throws ServletException, IOException {
        ex.printStackTrace();
        final String redirectOnFailLocation = req.getContextPath() + redirectOnFail;
        res.sendRedirect(redirectOnFailLocation);
    }

    /**
     * Store tokens and auth0User
     *
     * @param tokens the tokens
     * @param user the user profile
     * @param req the http servlet request
     */
    protected void store(final Tokens tokens, final Auth0User user, final HttpServletRequest req) {
        SessionUtils.setTokens(req, tokens);
        SessionUtils.setAuth0User(req, user);
    }

    /**
     * Get tokens for this request
     *
     * @param req the http servlet request
     * @return the tokens associated with the authentication request
     * @throws IOException
     */
    protected TokenHolder fetchTokens(final HttpServletRequest req) throws IOException {
        final String authorizationCode = req.getParameter("code");
        final String redirectUri = req.getRequestURL().toString();
        return this.exchangeCode(authorizationCode, redirectUri);
    }

    /**
     * Indicates whether the request is deemed valid
     *
     * @param req the http servlet request
     * @return boolean whether this request is deemed valid
     * @throws IOException
     */
    protected boolean isValidRequest(final HttpServletRequest req) throws IOException {
        return !hasError(req) && isValidState(req);
    }

    /**
     * Checks for the presence of an error in the http servlet request params
     *
     * @param req the http servlet request
     * @return boolean whether this http servlet request indicates an error was present
     */
    protected boolean hasError(final HttpServletRequest req) {
        return req.getParameter("error") != null;
    }

    /**
     * Indicates whether the nonce value in storage matches the nonce value passed
     * with the http servlet request
     *
     * @param req the http servlet request
     * @return boolean whether nonce value in storage matches the nonce value in the http request
     */
    protected boolean isValidState(final HttpServletRequest req) {
        final String stateFromRequest = req.getParameter("state");
        return NonceUtils.matchesNonceInStorage(req, stateFromRequest);
    }

    /**
     * Attempts to get the parameter (property) from (servlet) context
     *
     * @param parameter the parameter name to lookup
     * @param config the servlet config to search
     * @return the paramter value
     */
    protected String readParameter(final String parameter, final ServletConfig config) {
        final String initParam = config.getInitParameter(parameter);
        if (StringUtils.isNotEmpty(initParam)) {
            return initParam;
        }
        final String servletContextInitParam = config.getServletContext().getInitParameter(parameter);
        if (StringUtils.isNotEmpty(servletContextInitParam)) {
            return servletContextInitParam;
        }
        throw new IllegalArgumentException(parameter + " needs to be defined");
    }


    protected TokenHolder exchangeCode(final String code, final String callback) {
      AuthRequest request = auth0Client.exchangeCode(code, callback)
          .setAudience(this.audience)
          .setScope(this.scope);
      try {
          final TokenHolder holder = request.execute();
          return holder;
      } catch (APIException ex) {
          throw new RuntimeException(ex);
      } catch (Auth0Exception ex) {
          throw new RuntimeException(ex);
      }
    }

    protected UserInfo getUserInfo(String accessToken) {
      final Request<UserInfo> request = auth0Client.userInfo(accessToken);
      try {
          UserInfo info = request.execute();
          return info;
      } catch (APIException ex) {
          throw new RuntimeException(ex);
      } catch (Auth0Exception ex) {
          throw new RuntimeException(ex);
      }
    }

}

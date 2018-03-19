package com.auth0.example;

import com.auth0.Auth0User;
import com.auth0.SessionUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * BaseController that pulls in required attributes from web.xml
 */
public class BaseController extends HttpServlet {

    protected String domain;
    protected String issuer;
    protected String clientId;
    protected String clientSecret;
    protected String audience;
    protected String connection;
    protected String scope;
    protected String portalLoginUrl;
    protected String partnerLoginEndpoint;
    protected String externalReturnUrl;
    protected String callbackUrl;
    protected String logoutReturnUrl;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.domain = config.getServletContext().getInitParameter("auth0.domain");
        this.issuer = config.getServletContext().getInitParameter("auth0.issuer");
        this.clientId = config.getServletContext().getInitParameter("auth0.client_id");
        this.clientSecret = config.getServletContext().getInitParameter("auth0.client_secret");
        this.audience = config.getServletContext().getInitParameter("auth0.audience");
        this.connection = config.getServletContext().getInitParameter("auth0.connection");
        this.scope = config.getServletContext().getInitParameter("auth0.scope");
        this.portalLoginUrl = config.getServletContext().getInitParameter("portal_login_url");
        this.partnerLoginEndpoint = config.getServletContext().getInitParameter("partner_login_endpoint");
        this.externalReturnUrl = config.getServletContext().getInitParameter("external_return_url");
        this.callbackUrl = config.getServletContext().getInitParameter("callback_url");
        this.logoutReturnUrl = config.getServletContext().getInitParameter("logout_return_url");
    }

    /**
     * required attributes needed in request for view presentation
     */
    protected void setupRequest(final HttpServletRequest req) {
        // null if no user exists..
        final Auth0User user = SessionUtils.getAuth0User(req);
        req.setAttribute("loggedIn", user != null);
        req.setAttribute("user", user);
        req.setAttribute("domain", domain);
        req.setAttribute("issuer", issuer);
        req.setAttribute("clientId", clientId);
        req.setAttribute("audience", audience);
        req.setAttribute("scope", scope);
        req.setAttribute("portalLoginUrl", portalLoginUrl);
        req.setAttribute("partnerLoginEndpoint", partnerLoginEndpoint);
        req.setAttribute("externalReturnUrl", externalReturnUrl);
        req.setAttribute("callbackUrl", callbackUrl);
        req.setAttribute("logoutReturnUrl", logoutReturnUrl);
    }

}

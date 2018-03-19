package com.auth0;

import static com.auth0.jwt.pem.PemReader.readPublicKey;

import java.io.IOException;
import java.security.PublicKey;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTVerifier;

/**
 * Handles interception on a secured endpoint and does JWT Verification
 * Ensures only valid JWTs are permitted on secured endpoints
 * Success and Failure navigation options are configurable
 */
public class AuthFilter implements Filter {

    private JWTVerifier jwtVerifier;


    /**
     * Called by the web container to indicate to a filter that it is
     * being placed into service. Initialises configuration setup for this filter
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        final String issuer = filterConfig.getServletContext().getInitParameter("auth0.issuer");
        Validate.notNull(issuer);
        final String clientId = filterConfig.getServletContext().getInitParameter("auth0.client_id");
        Validate.notNull(clientId);
        final String signingAlgorithmStr = filterConfig.getServletContext().getInitParameter("auth0.signing_algorithm");
        // default to HS256 for backwards compatibility
        final Algorithm signingAlgorithm = (signingAlgorithmStr != null) ? Algorithm.valueOf(signingAlgorithmStr) : Algorithm.HS256;
        switch (signingAlgorithm) {
            case HS256:
            case HS384:
            case HS512:
                final String clientSecret = filterConfig.getServletContext().getInitParameter("auth0.client_secret");
                Validate.notNull(clientSecret);
                final String isSecretEncodedValue = filterConfig.getServletContext().getInitParameter("auth0.base64_encoded_secret");
                if (isSecretEncodedValue == null || Boolean.valueOf(isSecretEncodedValue)) {
                    final Base64 base64 = new Base64(true);
                    jwtVerifier = new JWTVerifier(base64.decode(clientSecret), clientId, issuer);
                } else {
                    jwtVerifier = new JWTVerifier(clientSecret, clientId, issuer);
                }
                return;
            case RS256:
            case RS384:
            case RS512:
                final String publicKeyPath = filterConfig.getServletContext().getInitParameter("auth0.public_key_path");
                Validate.notNull(publicKeyPath);
                try {
                    final ServletContext context = filterConfig.getServletContext();
                    final String publicKeyRealPath = context.getRealPath(publicKeyPath);
                    final PublicKey publicKey = readPublicKey(publicKeyRealPath);
                    Validate.notNull(publicKey);
                    jwtVerifier = new JWTVerifier(publicKey, clientId, issuer);
                    return;
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e.getCause());
                }
            default:
                throw new IllegalStateException("Unsupported signing method: " + signingAlgorithm.getValue());
        }
    }

    /**
     * Navigation to take when a request is successful by this filter
     */
    protected void onSuccess(final ServletRequest req, final ServletResponse res, final FilterChain next, final Auth0User auth0User)
            throws IOException, ServletException {
        final AuthRequestWrapper requestWrapper = new AuthRequestWrapper((HttpServletRequest) req, auth0User, true);
        next.doFilter(requestWrapper, res);
    }

    /**
     * Navigation to take when a request is rejected by this filter
     */
    protected void onReject(final ServletRequest req, final ServletResponse res, final FilterChain next, final Auth0User auth0User)
            throws IOException, ServletException {
        final AuthRequestWrapper requestWrapper = new AuthRequestWrapper((HttpServletRequest) req, null, false);
        next.doFilter(requestWrapper, res);
    }

    /**
     * Check for existence of id token and access token
     *
     * @param tokens the tokens object
     * @return boolean whether both id token and access token exist
     */
    protected boolean tokensExist(final Tokens tokens) {
        if (tokens == null) {
            return false;
        }
        return tokens.getIdToken() != null && tokens.getAccessToken() != null;
    }

    /**
     * Perform filter check on this request - verify tokens exist and verify
     * the id token is valid
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain next) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        final Tokens tokens = SessionUtils.getTokens(req);
        if (!tokensExist(tokens)) {
            onReject(req, res, next, null);
            return;
        }
        try {
            jwtVerifier.verify(tokens.getIdToken());
            final Auth0User auth0User = SessionUtils.getAuth0User(req);
            onSuccess(req, res, next, auth0User);
        } catch (Exception e) {
            onReject(req, res, next, null);
        }
    }

    @Override
    public void destroy() {
    }
}


const express = require('express');
const passport = require('passport');
const uuidv4 = require('uuid/v4');
const router = express.Router();
const authorize = require('../utils/authorize');
const ensureLoggedIn = require('connect-ensure-login').ensureLoggedIn;
const ensureTokenValid = require('../utils/ensureTokenValid');

const env = {
  AUTH0_CLIENT_ID: process.env.AUTH0_CLIENT_ID,
  AUTH0_DOMAIN: process.env.AUTH0_DOMAIN,
  AUTH0_CALLBACK_URL: process.env.AUTH0_CALLBACK_URL || 'http://app2.com:3001/callback'
};

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Express', env: env });
});

router.get('/login', function (req, res) {
  if (req.user) {
    res.redirect('/');
  } else {
    req.logout();
    const redirectUri = `${process.env.PORTAL_LOGIN_URL}?externalReturnUrl=${process.env.EXTERNAL_RETURN_URL}`;
    res.redirect(redirectUri);
  }
});

router.get('/session', ensureLoggedIn('/auth'), function (req, res, next) {
  authorize(req, res, true);
});

router.get('/auth', function (req, res) {
  if (req.user) {
    res.redirect('/user');
  } else {
    // check if SSO session exists..
    authorize(req, res, true);
  }
});

router.get('/logout', function (req, res) {
  req.logout();
  const callbackUrl = process.env.AUTH0_CALLBACK_URL;
  const returnToUrl = callbackUrl.substr(0, callbackUrl.lastIndexOf('/'));
  res.redirect(`https://${process.env.AUTH0_DOMAIN}/v2/logout?returnTo=${returnToUrl}`);
});

router.get('/callback',
  passport.authenticate('auth0', { failureRedirect: '/logout' }),
  function (req, res) {
    res.redirect(req.session.returnTo || '/user');
  });

router.get('/failure', function (req, res) {
  var error = req.flash('error');
  var errorDescription = req.flash('error_description');
  req.logout();
  res.render('failure', {
    error: error[0],
    error_description: errorDescription[0]
  });
});

module.exports = router;

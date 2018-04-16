const express = require('express');
const ensureLoggedIn = require('connect-ensure-login').ensureLoggedIn;
const ensureSSOSession = require('../utils/ensureSSOSession');
const router = express.Router();

router.get('/', ensureSSOSession, ensureLoggedIn('/auth'), function (req, res, next) {
  const env = {
    SESSION_URL: process.env.SESSION_URL
  };
  res.render('user', { env: env, user: req.user });
});

module.exports = router;

const express = require('express');
const ensureLoggedIn = require('connect-ensure-login').ensureLoggedIn;
const handleExternalReturnUrl = require('../middleware/eruhandler');
const router = express.Router();

/* GET user profile. */
router.get('/', ensureLoggedIn('/auth'), handleExternalReturnUrl, function (req, res, next) {
  const env = {
    SESSION_URL: process.env.SESSION_URL
  };
  res.render('user', { env: env, user: req.user });
});

module.exports = router;

const express = require('express');
const ensureLoggedIn = require('connect-ensure-login').ensureLoggedIn;
const ensureSSOSession = require('../utils/ensureSSOSession');
const router = express.Router();

router.get('/', ensureSSOSession, ensureLoggedIn('/auth'), function (req, res, next) {
  res.render('other', { user: req.user });
});

module.exports = router;

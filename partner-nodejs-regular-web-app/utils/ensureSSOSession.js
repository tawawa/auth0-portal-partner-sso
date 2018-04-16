const authorize = require('../utils/authorize');

/* middleware to ensure access token is valid - refresh if expired */
function ensureSSOSession (req, res, next) {
  const ssoChecked = req.session.ssoChecked;
  if (ssoChecked === true) {
    req.session.ssoChecked = false;
    return next();
  } else {
    req.session.returnTo = req.originalUrl;
    authorize(req, res, true);
  }
}
module.exports = ensureSSOSession;

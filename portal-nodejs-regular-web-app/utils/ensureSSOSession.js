const authorize = require('./authorize');

function ensureSSOSession (req, res, next) {
  const ssoChecked = req.session.ssoChecked;
  req.session.returnTo = req.originalUrl;
  if (ssoChecked === true) {
    req.session.ssoChecked = false;
    return next();
  } else {
    authorize(req, res, true);
  }
}
module.exports = ensureSSOSession;

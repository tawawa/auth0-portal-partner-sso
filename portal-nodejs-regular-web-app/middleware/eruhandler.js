
const handleExternalReturnUrl = function (req, res, next) {
  const externalReturnUrl = req.session.externalReturnUrl;
  if (externalReturnUrl) {
    req.session.externalReturnUrl = null;
    res.redirect(externalReturnUrl);
  } else {
    next();
  }
};

module.exports = handleExternalReturnUrl;

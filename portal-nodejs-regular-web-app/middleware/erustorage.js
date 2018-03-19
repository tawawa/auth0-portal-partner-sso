
const storeExternalReturnUrl = function (req, res, next) {
  const externalReturnUrl = req.query.externalReturnUrl;
  if (externalReturnUrl) {
    req.session.externalReturnUrl = req.query.externalReturnUrl;
  }
  next();
};

module.exports = storeExternalReturnUrl;

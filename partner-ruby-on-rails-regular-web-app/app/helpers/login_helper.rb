# frozen_string_literal: true
module LoginHelper
  def portal_login_url
    portal_login_url = Rails.application.secrets.portal_login_url
    external_return_url = Rails.application.secrets.external_return_url
    "#{portal_login_url}?externalReturnUrl=#{external_return_url}"
  end
end

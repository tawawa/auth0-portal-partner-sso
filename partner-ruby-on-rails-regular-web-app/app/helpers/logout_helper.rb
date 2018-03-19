# frozen_string_literal: true
module LogoutHelper
  def logout_url
    auth0_domain = Rails.application.secrets.auth0_domain
    client_id = Rails.application.secrets.auth0_client_id
    logout_return_url = Rails.application.secrets.logout_return_url
    request_params = {
      returnTo: logout_return_url,
      client_id: client_id
    }
    URI::HTTPS.build(host: auth0_domain, path: '/logout', query: to_query(request_params))
  end

  private

  def to_query(hash)
    hash.map { |k, v| "#{k}=#{URI.escape(v)}" unless v.nil? }.reject(&:nil?).join('&')
  end
end

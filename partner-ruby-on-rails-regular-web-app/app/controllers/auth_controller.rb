class AuthController < ApplicationController
  def auth
    if session[:userinfo].present?
      redirect_to '/user'
    else
      # check if SSO session exists..
      redirect_to '/auth/auth0'
    end
  end
end

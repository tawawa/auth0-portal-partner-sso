class LoginController < ApplicationController
  include LoginHelper
  def login
    if session[:userinfo].present?
      redirect_to '/show'
    else
      redirect_to portal_login_url
    end
  end
end


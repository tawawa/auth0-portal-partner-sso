class CheckController < ApplicationController
  include Secured
  def check
    # check if SSO session exists..
    redirect_to '/auth/auth0'
  end
end

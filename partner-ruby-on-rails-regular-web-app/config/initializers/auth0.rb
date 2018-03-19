Rails.application.config.middleware.use OmniAuth::Builder do

  OmniAuth.config.on_failure = Proc.new do |env|
    Auth0Controller.action(:failure).call(env)
  end

  provider(
    :auth0,
    ENV['AUTH0_CLIENT_ID'],
    ENV['AUTH0_CLIENT_SECRET'],
    ENV['AUTH0_DOMAIN'],
    callback_path: '/auth/auth0/callback',
    authorize_params: {
        prompt: 'none',
        scope: ENV['SCOPE'],
        audience: ENV['AUTH0_AUDIENCE']
    }
  )
end

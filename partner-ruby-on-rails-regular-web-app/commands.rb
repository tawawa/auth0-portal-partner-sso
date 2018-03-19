
export PORTAL_LOGIN_URL='http://app1.demonstration.site:3000/auth'
export EXTERNAL_RETURN_URL='http://app2.demonstration.site:3001/auth'

puts ENV['PORTAL_LOGIN_URL']
puts ENV['EXTERNAL_RETURN_URL']
login_url = ENV['PORTAL_LOGIN_URL']
puts login_url
eru = ENV['EXTERNAL_RETURN_URL']
puts eru
redirectUri = "#{login_url}?externalReturnUrl=#{eru}"
puts redirectUri


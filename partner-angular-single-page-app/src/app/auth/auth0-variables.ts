interface AuthConfig {
  clientID: string;
  domain: string;
  audience: string;
  callbackUrl: string;
  portalLoginUrl: string;
  logoutReturnUrl: string;
  externalReturnUrl: string;
  scope: string;
}

export const AUTH_CONFIG: AuthConfig = {
  clientID: 'ZngYGVNyUvPcLNMfuBhQyEU6pptqlulN',
  domain: 'auth.demonstration.site',
  audience: 'organise',
  callbackUrl: 'http://app2.demonstration.site:3001/callback',
  portalLoginUrl: 'http://app1.demonstration.site:3000/auth',
  logoutReturnUrl: 'http://app2.demonstration.site:3001/',
  externalReturnUrl: 'http://app2.demonstration.site:3001/auth',
  scope: 'openid profile email'
};




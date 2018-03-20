import { Injectable } from '@angular/core';
import { AUTH_CONFIG } from './auth0-variables';
import { Router } from '@angular/router';
import * as auth0 from 'auth0-js';

@Injectable()
export class AuthService {

  auth0 = new auth0.WebAuth({
    clientID: AUTH_CONFIG.clientID,
    domain: AUTH_CONFIG.domain,
    responseType: 'token id_token',
    audience: AUTH_CONFIG.audience,
    redirectUri: AUTH_CONFIG.callbackUrl,
    scope: 'openid profile email'
  });

  auth0Authentication = new auth0.Authentication(auth0, {
    domain: AUTH_CONFIG.domain,
    clientID: AUTH_CONFIG.clientID
  });

  constructor(public router: Router) {}

  public login(): void {
    // this.auth0.authorize();
    this.logout();
    const redirectUri = `${AUTH_CONFIG.portalLoginUrl}?externalReturnUrl=${AUTH_CONFIG.externalReturnUrl}`;
    console.log(redirectUri);
    window.location.href = redirectUri;
  }

  public userInfo(cb): void {
    const accessToken = localStorage.getItem('access_token');
    if (! accessToken) {
      this.router.navigate(['/']);
      cb("No access token");
    } else {
      this.auth0Authentication.userInfo(accessToken, (err, userInfo) => {
        if (err) {
          this.router.navigate(['/']);
          console.log(err);
          alert(`Error: ${err.error}. Check the console for further details.`);
          cb(err);
        } else {
          cb(null, userInfo);
        }
      })
    }
  }

  public checkSession(): void {
    this.auth0.checkSession({
      responseType: 'token id_token',
      timeout: 5000,
      usePostMessage: true
    }, (err, authResult) => {
      if (authResult && authResult.accessToken && authResult.idToken) {
        this.setSession(authResult);
        this.router.navigate(['/user']);
      } else if (err) {
        this.logout();
        // Go back to the index route
        this.router.navigate(['/']);
        console.log(err);
        // alert(`Error: ${err.error}. Check the console for further details.`);
      }
    });
  }

  public handleAuthentication(): void {
    this.auth0.parseHash((err, authResult) => {
      if (authResult && authResult.accessToken && authResult.idToken) {
        this.setSession(authResult);
        this.router.navigate(['/user']);
      } else if (err) {
        this.router.navigate(['/']);
        console.log(err);
        alert(`Error: ${err.error}. Check the console for further details.`);
      }
    });
  }

  private setSession(authResult): void {
    // Set the time that the access token will expire at
    const expiresAt = JSON.stringify((authResult.expiresIn * 1000) + new Date().getTime());
    localStorage.setItem('access_token', authResult.accessToken);
    localStorage.setItem('id_token', authResult.idToken);
    localStorage.setItem('expires_at', expiresAt);
  }

  public federatedLogout(): void {
    this.logout();
    const domain = AUTH_CONFIG.domain;
    const callbackUrl = AUTH_CONFIG.callbackUrl;
    const returnToUrl = callbackUrl.substr(0, callbackUrl.lastIndexOf('/'));
    const url = `https://${domain}/v2/logout?returnTo=${returnToUrl}`;
    window.location.href = url;
  }

  public logout(): void {
    // Remove tokens and expiry time from localStorage
    localStorage.removeItem('access_token');
    localStorage.removeItem('id_token');
    localStorage.removeItem('expires_at');
  }

  public isAuthenticated(): boolean {
    // Check whether the current time is past the
    // access token's expiry time
    const expiresAt = JSON.parse(localStorage.getItem('expires_at'));
    return new Date().getTime() < expiresAt;
  }

}

## What is this? 

Illustrates Portal - Partner SSO Pattern in which the Portal has an embedded login page.

The partner is redirected to the Portal for login.

All participating sites enjoy Auth0 SSO (Single Sign-On) Sessions, and also Single Signout.

Finally, each sample also illustrates how to do a server side “check session”, and take appropriate action.

See respective READMEs in subprojects.

Add the following to your local hosts file:

- 127.0.0.1 app1.demonstration.site
- 127.0.0.1 app2.demonstration.site

The portal is app1, the partner is app2.

Configured to run at:

- http://app1.demonstration.site:3000
- http://app2.demonstration.site:3001


Solutions for the Partner technologies include regular web application implementations using:

- Node.js
- Ruby On Rails
- Java Servlets

Single Page App framework implementation using:

- Angular 2






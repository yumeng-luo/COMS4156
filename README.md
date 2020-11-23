# COMS4156
[![Build Status](https://travis-ci.org/yumeng-luo/COMS4156.svg?branch=main)](https://travis-ci.org/yumeng-luo/COMS4156)
[![Codecov](https://img.shields.io/codecov/c/gh/yumeng-luo/COMS4156)](https://codecov.io/gh/yumeng-luo/COMS4156)

## Steps to run:
0. find and put in valid API keys (for security reasons, they are not posted on Github)
1. maven install (skipping unit tests is fine)
2. run src/main/java/savings/tracker/util/Application.java
3. go to localhost http://localhost:8080/ to view frontend.
4. be sure to login to Google before searching.

## API Keys
We use API keys for oauth, google maps, sendgrid, target, and wegman.
You can find each of these API keys in the following files:
1. Google auth: src/main/resources/application.yml (Google... client-secret:{api_key_here})
2. Google Maps: src/main/resources/index.html (look for "<script async defer src="https://maps.googleapis.com/maps/api/js?key={API_KEY_HERE}&libraries=places&callback=initMap">")
3. SendGrid: src/main/java/savings/tracker/util/SendGridEmailer.java (look for System.getenv("SENDGRID_API_KEY"). ie "send(dynamicTemplate, System.getenv("SENDGRID_API_KEY"))")
4. Target: src/main/java/savings/tracker/util/SendGridEmailer.java (look for System.getenv("RAPID_API_KEY"). ie "header("x-rapidapi-key", System.getenv("RAPID_API_KEY"))")
(wegman is public so it's already included in code)


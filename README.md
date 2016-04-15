# gae-firebase-java
Sample project to connect a GAE app to Firebase.

Requires billing enabled (for Sockets).
Requires manual instance scaling (for background threads).

# Configuration
Store firebase secrets in GaeFirebase/src/webapp/secrets.properties
```
firebaseSecret=<Firebase Secret>
```
Put Google service account secret in
GaeFirebase/src/main/webapp/application_default_credentials.json

# Launch Dev Server
Assumes that there is a file named gae-service-account.json located in src/main/webapp/
```
$ cd GaeFirebase
$ mvn appengine:devserver
```

# Deploy
```
$ cd GaeFirebase
$ mvn appengine:update
```

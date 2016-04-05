# gae-firebase-java
Sample project to connect a GAE app to Firebase.

# Configuration
Store firebase secrets in /GaeFirebase/src/webapp/secrets.properties
```
firebaseSecret=<Firebase Secret>
```

# Launch Dev Server
Assumes that there is a file named gae-service-account.json located in src/main/webapp/
```
$ cd GaeFirebase
$ export GOOGLE_APPLICATION_CREDENTIALS="gae-service-account.json"
$ mvn appengine:devserver
```

# Deploy
```
$ cd GaeFirebase
$ mvn appengine:update
```

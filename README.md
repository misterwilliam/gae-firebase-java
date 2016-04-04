# gae-firebase-java
Sample project to connect a GAE app to Firebase.

# Configuration
Store firebase secrets in /GaeFirebase/src/webapp/secrets.properties
```
firebaseSecret=<Firebase Secret>
```

# Launch Dev Server
```
$ cd GaeFirebase
$ mvn appengine:devserver
```

# Deploy
```
$ cd GaeFirebase
$ mvn appengine:update
```

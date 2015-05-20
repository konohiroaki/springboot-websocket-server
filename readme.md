### springboot-websocket-server

This is a very simple demo app for showing how to implement a websocket server with spring-boot. The websocket endpoint will be

```
ws://localhost:8080/echo
```

### Usage
Same as usual.

```
mvn spring-boot:run
```

A websocket server will startup, but you will see nothing if you don't have any websocket client.

You can use [springboot-javafx-websocket-client](https://github.com/konohiroaki/springboot-javafx-websocket-client) as a websocket client.

#### Create .jar
When you need a jar file, run

```
mvn clean package
```

and then run

```
java -jar the-jar-file.jar
```

### Fork me!
* For creating a new server using websocket
* For testing your websocket client
* For something awesome!

### Requires
* jdk1.8

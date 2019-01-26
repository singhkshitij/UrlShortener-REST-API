# UrlShortener REST API

A Java based API Service built with Spring Boot, and Redis and deployed on Heroku.

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)
[![Heroku App Status](https://heroku-shields.herokuapp.com/chootu-rest-api)](https://www.chtu.ml)

### How to run locally  !
- Prerequisites : 
    - A local redis instance instance should be up and running before running our service. 
- Import the maven project in eclipse
- Navigate to URLRepository class and replace 
```java
reJedis = new SpringConfig().getJedisPool().getResource();
```
with 
```java
reJedis = new redis();
```
- Now go to application.java and run as java application
- Your server is up and running now.
- By default the Server will run on localhost:8080/shortener
- To test, send POST Request to localhost:8080/shortener with a body of type   application/json with body { 'url' : '' }

### To Package as a Spring Boot jar !
- Build the project with mvn clean package
- Goto target folder you get a packaged jar to deploy whereever you want. :)

### How to deploy on Heroku !

- Create a Heroku account.
- Install the [Heroku CLI](https://devcenter.heroku.com/articles/heroku-command-line).
- Set up Heroku locally using your 
```sh 
Heroku login. 
```
- Once it is done, the steps are straightforward
```sh
git init
git add .
git commit -m "initial commit"
 
# creates a heroku application with random name (and Domain)
# creates a git repository at heroku
# registers the remote repository by the name heroku
heroku create
 
#Pushes the source code to the heroku git repo, so heroku can build and deploy it
git push heroku master
 
#opens our app in a browser 
heroku open
```
- Now our application is up and running but we have not deployed any redis instance for our application
- Search for Heroku redis in add on section from dashboard.
- click on install.
- Now verify if redis is up and running by typing 
```sh
$ heroku config
#You should get the similar output
REDIS_URL: redis://h:asdfqwer1234asdf@ec2-111-1-1-1.compute-1.amazonaws.com:111
```
- Note : Now the most important step is to disable connection pool timout for redis instance otherwise our pool get terminated by default after 5 min. 
- on console type 
```sh
heroku redis:timeout redis-elliptical-39032 --seconds 0
```
- Here redis-elliptical-39032 is redis instance name.
- If you get the following output. Voila your REST API is up and running with full functionality enabled. You can try hitting the api endpoints now. 
##### How Heroku Works ?

> After pushing code to the remote repository named Heroku, the build process is triggered. Given that the project has a pom.xml, Heroku recognizes it as a Java application. Spring Boot has an embedded Tomcat. Therefore, it can be started up as a JAR file and will work as a web server.

# UrlShortener

A Java Service built with Spring Boot, and Redis.

## To run:
- Import the maven project in eclipse
- Go to application.java and run as java application
- Your server is up and running now.
- By default the Server will run on localhost:8080/shortener 
- To test, send POST Request to localhost:8080/shortener with a body of type application/json with body
  {
    'url' : '<INSERT URL>'
  }
  
 ## How to Deploy / Production ready
  
- Build the project with mvn clean package
- Goto target folder you get a packaged jar to deploy whereever you want. :)

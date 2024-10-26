Prerequisite:
        java 8+, maven, docker

Steps to Run Project:
1. Clone this Repo and cd  upto  main pom.xml
2. mvn clean install  
3. docker-compose up --build


Rest API's-
1. For start Streaming- (POST) http://localhost:8080/api/streaming/start
2. For Stop Streaming-  (POST) http://localhost:8080/api/streaming/stop
3. To get Status- (GET) http://localhost:8080/api/streaming/status
4. To load data to source - (POST)   http://localhost:8080/api/streaming/data
            dummy json-
                {
   "name": "pramod",
   "email": "john.doe@example.com",
   "phone": "+1234567890",
   "address": "123 Main Street, Anytown, USA"
   }
5. To retrieve data from target- (GET)  http://localhost:8080/api/streaming/data

#  JSON-based RESTful API - Geocoding

The goal is to expose a JSON-based RESTful API, that will accept address as a single string and use this address parameter to query Google API, that should be consumed using XML format. The JSON response should contain the formatted address, as well as latitude and longitude of the address.

To run test cases use following:
 
 ```
 mvn test
```
 
To build and run the code use following:
  
```
mvn install spring-boot:run
```

To call the service use following url:
 
```
 curl http://localhost:8080/api/geocode/Bogota
```
 
 
 

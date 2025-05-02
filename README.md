# The mybank application in Marco Behler's Spring Professional course

Part of [Marco Behler's Spring Professional course](https://www.marcobehler.com/courses/spring-professional)
is the creation of a "mybank" web application. My solutions to the "mybank" exercises can be found here.

All credits go to [Marco Behler](https://www.marcobehler.com/) for the creation of this course.

## The non-Spring plain servlet implementation (without database) of the mybank application

### Starting the application with embedded Tomcat server

Build the application with the following simple Maven command:

```shell
mvn clean package
```

Then start the application with embedded server (listening on port 8090) like so:

```shell
java -Dserver.port=8090 -jar target/mybank-1.0-SNAPSHOT.jar
```

### HTTP API

To get all transactions, the following curl command can be used:

```shell
curl http://localhost:8090/transactions
```

Query string parameters "id" or "reference" can be used to filter the result on "id" or "reference",
respectively.

To create a transaction, the following example curl command can be used as "template":

```shell
curl -X POST \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Accept: application/json" \
  -d "amount=50&reference=eating+out" \
  http://localhost:8090/transactions
```

## The plain Spring IOC servlet-based implementation (without database) of the mybank application

See above for some curl commands to "get" or "post" transactions.

## The Spring Web-MVC REST implementation (without database) of the mybank application

The POST request now becomes:

```shell
curl -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{ "amount": 50, "reference": "eating out" }' \
  http://localhost:8090/transactions
```

Try out validation errors with a POST request such as:

```shell
curl -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{ "amount": 0, "reference": "" }' \
  http://localhost:8090/transactions
```

## The Spring Web-MVC implementation using Thymeleaf (without database) of the mybank application

Note that a "receivingUserId" has been added to the Transaction model entity. So the POST request
above becomes (for user Chris):

```shell
curl -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{ "amount": 50, "reference": "eating out", "receivingUserId": "Chris" }' \
  http://localhost:8090/transactions
```

## The Spring JDBC and Web-MVC implementation using Thymeleaf of the mybank application

This time the transaction data is persistent, so survives restarts of the application.

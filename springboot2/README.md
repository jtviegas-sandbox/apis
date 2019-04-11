# spring boot 2 microservices

The purpose of this project is to test springboot2 while developing a microservices architecture.
We will test x microservices:
- api frontend/gateway [stateless] - all the requests will be sent here;
- worker [stateless] - this service will process some kind of asynchronous lengthy operation;
- store [stateful] - this will be a service for persistence purposes;
- batch - still have to figure out what kind of integration to test here;


## swagger
`http://localhost:3001/swagger-ui.html`


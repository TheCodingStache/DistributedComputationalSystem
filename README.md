# DistributedComputationalSystem

This application uses an HTTP server implementation which communicates with a client consumer. 
The http server works in concurrent way that creates a pool of threads where we can setup multiple workers to do a computation through the network.
The client can send an http request with a use of a header to check the time that the workers take to compute the calculation.

If you want to run and test this application, I suggest to split this project into two pieces inside your IDE.
Also, it is prefferable to use the mvn clean package command to create .jar files inside the project's directory.
Use your computer terminal to run multiple server instances and then run the client application to check and validate the results.

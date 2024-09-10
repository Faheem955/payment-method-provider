# payment-method-provider
This service will allow the user to create, update and get all the payment methods provided.

There are no data base configurations required, as the application uses H2 database which is set to configure itself automatically as application is started.
All requests are exactly as provided in document.

Application runs by using springBoot and is secured by using basic authentication given below with all the requests:

USERNAME: Faheem
PASSWORD: Faheem123

Application runs on jdk 14, as I wasn't able to get enough time to configure java on my personal machine. I will update the code with java 8 first thing in the morning if I get some time.

Remember to run mvn clean install before running application.

Things required to run the application:
maven,
java (jdk 14)

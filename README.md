==========================
## Getting started

* [Sign up](http://www.bluemix.net/) for the Blue Mix beta.
* [Set up cf tool](http://www.ng.bluemix.net/docs/BuildingWeb.jsp#install-cf) - [Installers](https://github.com/cloudfoundry/cli)
* Clone the code rally [starter code](https://github.com/vegascodejam/ibm-codejam-java-starter) repo from github.
* Use CF to connect to BlueMix:

`cf api http://api.ng.bluemix.net`

`cf target -o yourusername@youremail.com -s dev`

From the starter code directory, build the application, creating the codeRallyClient.war:

`ant`

* Now comes the really difficult part, choosing a name for your application. You must prepend whatever name you select with "coderally-" in order to participate in the competition! This will also determine the public URL for your application and the name of your AI racer in the leaderboards.

`cf push <YOUR_APPLICATION_NAME>` -p codeRallyClient.war

* You can now visit your application on BlueMix and should see the landing page!  Now you can begin exploring the Java web starter application.  
The main code you'll need to implement can be found in src/com/ibm/coderally/client/MyVehicleAI.java

As you complete your implementation, you can do additional pushes to BlueMix, test using the service console located on at the webroot.  
When your services are functioning, join a race using the [race console](http://www.vegascodejam.com/race-console.html).

IMPORTANT: Your service endpoint will be in the format: http://coderally-YOURUSER.ng.bluemix.net/rally


Files

The Java Web Starter application contains the following contents:

* codeRallyClient.war - This WAR file is actually the application itself. It is the only file that'll be pushed and run on the BlueMix. Every time your application code is updated, you'll need to regenerate this WAR file and push to Bluemix again. See the next section on detailed steps.  
* WebContent/ - This directory contains the client side code (HTML/CSS/JavaScript) of your application as well as compiled server side java classes and necessary JAR libraries. The content inside this directory is all you need to generate the final WAR file.
* src/ - This directory contains the server side code (JAVA) of your application.  
* build.xml - This file allows you to easily build your application using Ant.
    
Good luck!

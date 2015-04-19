README

This readme is for HomeworkFourPartOne*.

To run the client and server classes of this project we need to 
import the project in Eclipse.

To import in Eclipse:
1. Open Eclipse.
2. In Package Explorer, right click and select import.
3. Select the Existing projects into Workspace option and click next.
4. Select the Select Archive file option and browse for the HomeworkFourPartOne*.zip file then click next.

To run the server:
1. In package explorer navigate to HomeworkFourPartOneServer->HomeworkFourPartOneServer->HomeworkFourPartOneServer.java.
2. Right click on HomeworkFourPartOneServer.java and select run as-> Java Application.

To run the Client class, we need to give some command line options. 
Get the name of the machine, probably localhost.

To run the Client:
First we set up the command line options.
1. Navigate to the HomeworkFourPartOneClient project in PackageExplorer.
2. Right click on the project, select Properties.
3. In the Run/Debug settings menu select Client and click Edit.
4. Select the "(x) = Arguments tab and in the Program arguments type: "<host name> 5001", where <host name> is the host name for the machine running the server.
5. Click ok.

Now you should be able to run the Client class.
1. In package explorer navigate to HomeworkFourPartOneClient-HomeworkFourPartOneClient-HomeworkFourPartOneClient.java. 
2. Right click on HomeworkFourPartOneClient.java and select run as-> Java Application.

Alternatively you can run the included .jar files in the provided folders that contain the requisite xml files:

To run the HomeworkFourPartOneServer.jar, just click on it.

To run the HomeworkFourPartOneClient.jar file:
1. Open command line or terminal and cd to the HomeworkFourPartOne/Client executable folder containing the HomeworkFourPartOneClient.jar.
2. Use java -jar HomeworkFourPartOneClient.jar <host name> <port>, where <host name>=the host name (probably "localhost") and <port>=5001.

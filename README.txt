---------------------------
Description of the archive.
---------------------------
The archive is constituted of the following content:
- REDAME.txt: the current file.
- doc/report.pdf: pdf containing the project report.
- project/src/: directory containing the source code of the project.
- project/Project.jar: runnable jar containing a precompiled version of the
  application.
- project/build.xml: xml file use for compiling the project via ant.

For the content of directory project/src we suggest to read the project report
to know the content of the different packages.

-------------------------
Compilation instructions.
-------------------------
The compilation process has been automatized via the project/build.xml file.
Prerequisite to compile easily the project is to have the application 
'apache ant' installed in the system.
To compile the project it is necessary to navigate to project/ directory in a
terminal emulator and just type the command

ant

which will automatically produce the project/bin/ directory in which there will
be all class file needed for executing the different applications.

---------------------
Running instructions.
---------------------
In order to run the applications contained in the project it is necessary to 
know that there are two different java class with a main method. These are:
- server.Server: which is the rmi server.
- gui.MainWindow: which is the main window of the simulator client.

To run both of them it is necessary to do the following:
1. Add directory project/bin/ to the CLASSPATH environment variable (consult
system documentation to determine how to do that).
2. Start rmiregistry application in order to allow the server to be run.
3. Start rmi server with the command
	java server.Server <path_to_file>
Where <path_to_file> represents the path to the file which will be used to put
statstical data collected by clients in.
4. Start the client window with the command
	java gui.MainWindow

If the directory '.' is already in CLASSPATH variable is also possible to run
the application via navigating through directory project/bin and 
executing every step from 2 to 4 in that directory.

Since it is a distributed application note that steps 1,2,3 could be executed
in a different machine then the one in which steps 1,4 are executed. Obviously
in order to make system working it is necessary to start the rmi server before
a simulation is started.

If user wants just to execute the application using the jar file in project/
directory the following steps must be executed in a terminal emulator:
(0.) Navigate to project/ directory.
1. Run
	java -jar (project/)Project.jar
	
Obviously it is not necessary to execute step 0 if user wants to specify the
absolute path to the jar file.

Note also that since it is possible to bypass the rmi server in the client
simulator following the instructions present in project/report.pdf, the client
is completely stand-alone and it could be run using no server.
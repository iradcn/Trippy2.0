# Databases Project, 2015b

###Prerequisites:
Maven,Git

###Instructions:

#####0. Clone Project:
  1. *`git clone https://github.com/amirbar/Databases15bFinalProj.git`*

#####1. Compilation:
  From within Repository:
  1. *`git pull origin master`*
  2. *`mvn clean install`*

#####2. Build DB:
  Start your MySQL Server
  1. Run /build/add_db_user.sql on mysql client
  2. Run /build/build_schema.sql on mysql client
  3. Run /build/fill_categories.sql on mysql client
  
#####3. Yago Dependencies
  1. Make sure you have all the following Yago dependencies under /Resources:
	1.1 yagoGeonamesOnlyData.tsv
	1.2 yagoGeonamesTypes.tsv
	 
#####4.Run
  From within project root:
  1. *`java -jar target/final-project-0.0.1-SNAPSHOT.jar`*
  2. open browser, navigate to *`http://localhost:8080`*, server should be running

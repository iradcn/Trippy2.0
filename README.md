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
  2. Run /build/build_schema-local.sql on mysql client
  3. Run /build/fill_categories-local.sql on mysql client
  4. Run /build/fill_properties-local.sql on mysql client

  ---or---

  1. Create an account on the school's MySQL server (user, db and pass will be sent to your email):
    - run on nova:
      - *`create_mysql_user`*
  2. run:
    - *`mysql -u <username> -h mysqlsrv.cs.tau.ac.il <dbname> -p < src/main/build/build_schema.sql`*
    - *`mysql -u <username> -h mysqlsrv.cs.tau.ac.il <dbname> -p < src/main/build/fill-categories.sql`*
    - *`mysql -u <username> -h mysqlsrv.cs.tau.ac.il <dbname> -p < src/main/build/fill-properties.sql`*
  
#####3. Yago Dependencies
  1. Make sure you have all the following Yago dependencies under /Resources:
	1.1 yagoGeonamesOnlyData.tsv
	1.2 yagoGeonamesTypes.tsv

  2. In order to populate the DB, set up a tunnel from your PC to mysqlsrv.cs.tau.ac.il (instructions at http://courses.cs.tau.ac.il/databases/databases201415/slides/moreinfo/connection-guide.htm) and populate via the webapp.
	 
#####3.5. Config file
  1. running locally (local mysql): keep as is.
  2. runnig locally (tunneled mysql):
    - hostname: localhost
    - port: 3305 (or whatever else you configured)
    - dbName, username, password: as supplied by TAU system.
  3. running on nova:
    - hostname: mysqlsrv.cs.tau.ac.il
    - port: 3306
    - dbName, username, password: as supplied by TAU system.
   
#####4.Run
  From within project root:
  1. *`java -jar target/final-project-0.0.1-SNAPSHOT.jar`*
  2. open browser, navigate to *`http://localhost:8080`*, server should be running

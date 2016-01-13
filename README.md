###Prerequisites:
Maven,Git

###Instructions:

#####0. Clone Project

#####1. Compilation:
  From within Repository:
  1. *`git pull origin master`*
  2. *`mvn clean install`*

#####2. Build DB:
  Start your MySQL Server
  1. Run /build/add_db_user.sql on mysql client
  2. Run /build/build_schema_v2.sql on mysql client

#####3.Run
  From within project root:
  1. *`java -jar target/<output jar name>`*
  2. open browser, navigate to *`http://localhost:8080`*, server should be running

#####4. Load Places From Google
  1. Enter localhost:8080/app/import

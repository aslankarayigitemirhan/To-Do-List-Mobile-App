# To-Do-List-Mobile-App

## There are two points to should pay attention during build Spring Boot Application:

* After program is run once in "spring.jpa.hibernate.ddl-auto=create" mode, the database will be created (tables and relations between tables, also). **To avoid loosing information in your database, you should change condition with 'update' instead 'create'**. Unless it makes sense, all the information in db whatever you used in terms of db technologies are removed, and created the tables with no data one more time...
* Also, you have to provide **your own postgress database password** in application.properties file.

## API DOCUMENTATION IS AVAILABLE IN REPOSITORY: 
- In controller layer in Spring Boot Application, there are some API's to manage the functionality of methods. You can reach the documentation with their descrioption from the repository.

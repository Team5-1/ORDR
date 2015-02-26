# ORDR

### Summary
ORDR is a shopping application written in Java. Think of it as something simliar to the Amazon mobile app, for desktop. It's being written for a group coursework assignement by a group of University of Westminster students (*scroll down to "The Team"*).

### Road map
The completed implementation is due for the 30th of March 2015. The team has completed their first phase of implementation and is begining to integrate the features they have developed individually, with each other.

### The Team
- [Danyal Aboobakar](https://github.com/daboobakar) (*Group Leader*)
- [Kyle McAlpine](http://kylejm.io) (*Lead Programmer*)
- [Ali Younas](https://github.com/aliyounas) (*Chief Architect*)
- [Maywand Amin](https://github.com/MaywandAmin) (*GUI Designer*)
- James Buchanan (*Software Engineer*)
- Fariha Ahmad (*Database Designer*)

The roles of the team members were elected by the team internally, apart from Kyle and Ali's roles which were selected by the faculty.

### Cloning and running the project

_**WARNING**_: the following database connection information is subject to change. Please check back here after pulls if you're having any SQLExceptions that seem weird. We're also going to add more data to the SQL file provided to initialise your database.

ORDR has been written with the [Intellij Idea IDE](https://www.jetbrains.com/idea/). If you'd like to clone and run the project locally you'll need to make a class called `DatabaseCredentials.java` and fill it with the following code: 

```java
public class DatabaseCredentials {
    static final String localURL = "<your_local_mysql_url>";
    static final String localUsername = "<your_local_mysql_password>";
    static final String localPassword = "<your_local_mysql_username>";
}
```

You could put in the credentials on any MySQL server, but we recommened doing it locally using something like [MAMP](http://www.mamp.info/en/).

To initialse ORDR tables and data: 

1. Create a new databse on your MySQL server 
2. Download and then import the ORDR.sql file in phpMyAdmin (*note*: you will first have to create a new database) 

### Useful links

- Implementation phasses (coming soon - to be added to the wiki)

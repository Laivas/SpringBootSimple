# SpringBootSimple

Simple spring boot project to try out spring boot, spring security, hibernate.

Requirements java version 17+, Maven, Mysql.

In mysql cli run these commands.

CREATE DATABASE databasename1;

CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'password';

grant all privileges on databasename1.* to springuser@'localhost';


To build war file run: mvn package


To start run: java -jar SpringBootSimple-0.0.1-SNAPSHOT.war


localhost:8080/index

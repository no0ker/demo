CREATE TABLE person
(
    id       identity,
    name     varchar(250),
    lastName varchar(250),
    age      int
);

INSERT INTO person
    (name, lastName, age)
VALUES ('name1', 'lastName1', 1)
     , ('name1', 'lastName1', 2)
     , ('name2', 'lastName2', 2)
     , ('name3', 'lastName3', 3)
     , ('name3', 'last name 3', 3)
;

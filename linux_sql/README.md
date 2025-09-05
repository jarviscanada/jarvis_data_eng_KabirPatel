# Introduction
Utilized best practices for SQL and RDBMS. Strengthened understanding of SQL queries, executions, normalisation, table schemas and data integrity.

# SQL Queries

###### Table Setup (DDL)

CREATE TABLE cd.members (
  memid integer NOT NULL, 
  surname varchar(200) NOT NULL, 
  firstname varchar(200) NOT NULL, 
  address varchar(300) NOT NULL, 
  zipcode integer NOT NULL, 
  telephone varchar(20) NOT NULL, 
  recommendedby integer, 
  joindate timestamp NOT NULL, 
  CONSTRAINT members_pk PRIMARY KEY (memid), 
  CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid) ON DELETE 
  SET 
    NULL
);


CREATE TABLE cd.facilities (
  facid integer NOT NULL, 
  name varchar(100) NOT NULL, 
  membercost numeric NOT NULL, 
  guestcost numeric NOT NULL, 
  initialoutlay numeric NOT NULL, 
  monthlymaintenance numeric NOT NULL, 
  CONSTRAINT facilities_pk PRIMARY KEY (facid)
);

CREATE TABLE cd.bookings (
  bookid integer NOT NULL, 
  facid integer NOT NULL, 
  memid integer NOT NULL, 
  starttime timestamp NOT NULL, 
  slots integer NOT NULL, 
  CONSTRAINT bookings_pk PRIMARY KEY (bookid), 
  CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid), 
  CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
);

###### Question 1: Show all members 

###### **Question 1:** Insert a new member  
[Exercise Link](https://pgexercises.com/questions/updates/insert.html)

```sql
INSERT INTO cd.members
  (memid, surname, firstname, address, zipcode, telephone, recommendedby, joindate)
VALUES
  (21, 'Smith', 'John', '1 Main Street', 12345, '555-555-5555', NULL, '2025-09-02');
```

###### **Question 2:** Insert using SELECT  
[Exercise Link](https://pgexercises.com/questions/updates/insert3.html)

```sql
INSERT INTO cd.members (memid, surname, firstname, address, zipcode, telephone, recommendedby, joindate)
SELECT
    (SELECT MAX(memid) + 1 FROM cd.members),
    'Brown', 'Charlie', '10 Hill Road', 11111, '555-123-4567', NULL, CURRENT_DATE;
```

###### **Question 3:** Update telephone number  
[Exercise Link](https://pgexercises.com/questions/updates/update.html)

```sql
UPDATE cd.members
SET telephone = '555-222-3333'
WHERE surname = 'Smith' AND firstname = 'John';
```

###### **Question 4:** Update fees with calculation  
[Exercise Link](https://pgexercises.com/questions/updates/updatecalculated.html)

```sql
UPDATE cd.members
SET joindate = joindate + INTERVAL '1 month'
WHERE memid = 1;
```

###### **Question 5:** Delete all bookings  
[Exercise Link](https://pgexercises.com/questions/updates/delete.html)

```sql
DELETE FROM cd.bookings;
```

###### **Question 6:** Delete conditional  
[Exercise Link](https://pgexercises.com/questions/updates/deletewh.html)

```sql
DELETE FROM cd.members
WHERE joindate < '2012-01-01';
```

---

# **Basics**

###### **Question 7:** Members with firstname starting with J  
[Exercise Link](https://pgexercises.com/questions/basic/where2.html)

```sql
SELECT *
FROM cd.members
WHERE firstname LIKE 'J%';
```

###### **Question 8:** Members who joined after 2012  
[Exercise Link](https://pgexercises.com/questions/basic/where3.html)

```sql
SELECT firstname, surname, joindate
FROM cd.members
WHERE joindate >= '2012-01-01'
ORDER BY joindate ASC;
```

###### **Question 9:** Members recommended by others  
[Exercise Link](https://pgexercises.com/questions/basic/where4.html)

```sql
SELECT memid, firstname, surname
FROM cd.members
WHERE recommendedby IS NOT NULL;
```

###### **Question 10:** Members who joined on specific date  
[Exercise Link](https://pgexercises.com/questions/basic/date.html)

```sql
SELECT firstname, surname, joindate
FROM cd.members
WHERE joindate = '2012-09-01';
```

###### **Question 11:** Combine members and bookings using UNION  
[Exercise Link](https://pgexercises.com/questions/basic/union.html)

```sql
SELECT memid AS id FROM cd.members
UNION
SELECT facid AS id FROM cd.bookings;
```

---

# **Joins**

###### **Question 12:** Simple join on members and bookings  
[Exercise Link](https://pgexercises.com/questions/joins/simplejoin.html)

```sql
SELECT m.firstname, m.surname, b.starttime
FROM cd.members m
JOIN cd.bookings b ON m.memid = b.memid;
```

###### **Question 13:** Join facilities and bookings  
[Exercise Link](https://pgexercises.com/questions/joins/simplejoin2.html)

```sql
SELECT f.name, COUNT(b.bookid) AS total_bookings
FROM cd.facilities f
JOIN cd.bookings b ON f.facid = b.facid
GROUP BY f.name;
```

###### **Question 14:** Self-join on members  
[Exercise Link](https://pgexercises.com/questions/joins/self2.html)

```sql
SELECT m1.firstname || ' ' || m1.surname AS member,
       m2.firstname || ' ' || m2.surname AS recommender
FROM cd.members m1
LEFT JOIN cd.members m2
ON m1.recommendedby = m2.memid;
```

###### **Question 15:** Three joins example  
[Exercise Link](https://pgexercises.com/questions/joins/self.html)

```sql
SELECT m.firstname, m.surname, f.name
FROM cd.members m
JOIN cd.bookings b ON m.memid = b.memid
JOIN cd.facilities f ON b.facid = f.facid;
```

###### **Question 16:** Subquery + Join  
[Exercise Link](https://pgexercises.com/questions/joins/sub.html)

```sql
SELECT m.firstname, m.surname
FROM cd.members m
WHERE m.memid IN (
    SELECT b.memid
    FROM cd.bookings b
    JOIN cd.facilities f ON b.facid = f.facid
    WHERE f.name = 'Tennis Court 1'
);
```

---

# **Aggregation**

###### **Question 17:** Count bookings per member  
[Exercise Link](https://pgexercises.com/questions/aggregates/count3.html)

```sql
SELECT memid, COUNT(*) AS total_bookings
FROM cd.bookings
GROUP BY memid
ORDER BY total_bookings DESC;
```

###### **Question 18:** Sum facility usage hours  
[Exercise Link](https://pgexercises.com/questions/aggregates/fachours.html)

```sql
SELECT facid, SUM(slots) AS total_slots
FROM cd.bookings
GROUP BY facid
ORDER BY total_slots DESC;
```

###### **Question 19:** Monthly facility usage  
[Exercise Link](https://pgexercises.com/questions/aggregates/fachoursbymonth.html)

```sql
SELECT facid, DATE_TRUNC('month', starttime) AS month, SUM(slots) AS total_slots
FROM cd.bookings
GROUP BY facid, month
ORDER BY facid, month;
```

###### **Question 20:** Multi-column group by  
[Exercise Link](https://pgexercises.com/questions/aggregates/fachoursbymonth2.html)

```sql
SELECT facid, memid, DATE_TRUNC('month', starttime) AS month, SUM(slots) AS total_slots
FROM cd.bookings
GROUP BY facid, memid, month
ORDER BY facid, memid, month;
```

###### **Question 21:** Count distinct members  
[Exercise Link](https://pgexercises.com/questions/aggregates/members1.html)

```sql
SELECT COUNT(DISTINCT memid) AS total_members
FROM cd.members;
```

---

# **String Functions**

###### **Question 22:** Concatenate member names  
[Exercise Link](https://pgexercises.com/questions/string/concat.html)

```sql
SELECT firstname || ' ' || surname AS fullname
FROM cd.members;
```

###### **Question 23:** Filter names by string function  
[Exercise Link](https://pgexercises.com/questions/string/reg.html)

```sql
SELECT *
FROM cd.members
WHERE firstname ~ '^[A-Z]';
```

###### **Question 24:** Substring and group  
[Exercise Link](https://pgexercises.com/questions/string/substr.html)

```sql
SELECT SUBSTRING(firstname, 1, 1) AS initial, COUNT(*) AS total
FROM cd.members
GROUP BY initial
ORDER BY total DESC;
```


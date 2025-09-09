
-- Question 1: Insert a new member
INSERT INTO cd.members
  (memid, surname, firstname, address, zipcode, telephone, recommendedby, joindate)
VALUES
  (21, 'Smith', 'John', '1 Main Street', 12345, '555-555-5555', NULL, '2025-09-02');

-- Question 2: Insert using SELECT
INSERT INTO cd.members (memid, surname, firstname, address, zipcode, telephone, recommendedby, joindate)
SELECT
    (SELECT MAX(memid) + 1 FROM cd.members),
    'Brown', 'Charlie', '10 Hill Road', 11111, '555-123-4567', NULL, CURRENT_DATE;

-- Question 3: Update telephone number
UPDATE cd.members
SET telephone = '555-222-3333'
WHERE surname = 'Smith' AND firstname = 'John';

-- Question 4: Update fees with calculation
UPDATE cd.members
SET joindate = joindate + INTERVAL '1 month'
WHERE memid = 1;

-- Question 5: Delete all bookings
DELETE FROM cd.bookings;

-- Question 6: Delete conditional
DELETE FROM cd.members
WHERE joindate < '2012-01-01';

-- Question 7: Members with firstname starting with J
SELECT *
FROM cd.members
WHERE firstname LIKE 'J%';

-- Question 8: Members who joined after 2012
SELECT firstname, surname, joindate
FROM cd.members
WHERE joindate >= '2012-01-01'
ORDER BY joindate ASC;

-- Question 9: Members recommended by others
SELECT memid, firstname, surname
FROM cd.members
WHERE recommendedby IS NOT NULL;

-- Question 10: Members who joined on specific date
SELECT firstname, surname, joindate
FROM cd.members
WHERE joindate = '2012-09-01';

-- Question 11: Combine members and bookings using UNION
SELECT memid AS id FROM cd.members
UNION
SELECT facid AS id FROM cd.bookings;

-- Question 12: Simple join on members and bookings
SELECT m.firstname, m.surname, b.starttime
FROM cd.members m
JOIN cd.bookings b ON m.memid = b.memid;

-- Question 13: Join facilities and bookings
SELECT f.name, COUNT(b.bookid) AS total_bookings
FROM cd.facilities f
JOIN cd.bookings b ON f.facid = b.facid
GROUP BY f.name;

-- Question 14: Self-join on members
SELECT m1.firstname || ' ' || m1.surname AS member,
       m2.firstname || ' ' || m2.surname AS recommender
FROM cd.members m1
LEFT JOIN cd.members m2
ON m1.recommendedby = m2.memid;

-- Question 15: Three joins example
SELECT m.firstname, m.surname, f.name
FROM cd.members m
JOIN cd.bookings b ON m.memid = b.memid
JOIN cd.facilities f ON b.facid = f.facid;

-- Question 16: Subquery + Join
SELECT m.firstname, m.surname
FROM cd.members m
WHERE m.memid IN (
    SELECT b.memid
    FROM cd.bookings b
    JOIN cd.facilities f ON b.facid = f.facid
    WHERE f.name = 'Tennis Court 1'
);

-- Question 17: Count bookings per member
SELECT memid, COUNT(*) AS total_bookings
FROM cd.bookings
GROUP BY memid
ORDER BY total_bookings DESC;

-- Question 18: Sum facility usage hours
SELECT facid, SUM(slots) AS total_slots
FROM cd.bookings
GROUP BY facid
ORDER BY total_slots DESC;

-- Question 19: Monthly facility usage
SELECT facid, DATE_TRUNC('month', starttime) AS month, SUM(slots) AS total_slots
FROM cd.bookings
GROUP BY facid, month
ORDER BY facid, month;

-- Question 20: Multi-column group by
SELECT facid, memid, DATE_TRUNC('month', starttime) AS month, SUM(slots) AS total_slots
FROM cd.bookings
GROUP BY facid, memid, month
ORDER BY facid, memid, month;

-- Question 21: Count distinct members
SELECT COUNT(DISTINCT memid) AS total_members
FROM cd.members;

-- Question 22: Concatenate member names
SELECT firstname || ' ' || surname AS fullname
FROM cd.members;

-- Question 23: Filter names by string function
SELECT *
FROM cd.members
WHERE firstname ~ '^[A-Z]';

-- Question 24: Substring and group
SELECT SUBSTRING(firstname, 1, 1) AS initial, COUNT(*) AS total
FROM cd.members
GROUP BY initial
ORDER BY total DESC;


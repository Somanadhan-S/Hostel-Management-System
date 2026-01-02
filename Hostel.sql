CREATE DATABASE hostel_db;
USE hostel_db;

Drop table rooms;
CREATE TABLE rooms (
    room_number INT PRIMARY KEY,
    capacity INT,
    occupied INT DEFAULT 0
);

Drop table students;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    room_number INT,
    FOREIGN KEY (room_number) REFERENCES rooms(room_number)
);


INSERT INTO rooms (room_number, capacity) VALUES
(101,2),(102,2),(103,3);
ALTER TABLE students ADD status VARCHAR(20) DEFAULT 'ACTIVE';

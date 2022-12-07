DROP TABLE IF EXISTS PARTICIPANT_ANSWER;
DROP TABLE IF EXISTS QUESTION;
DROP TABLE IF EXISTS PARTICIPANT;
DROP TABLE IF EXISTS EXAM;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS INSTITUTION;
DROP TABLE IF EXISTS ADMIN;



CREATE TABLE ADMIN (
	admin_id INT AUTO_INCREMENT UNIQUE NOT NULL,
	admin_name VARCHAR(45) UNIQUE NOT NULL,
	admin_pass VARCHAR(45) NOT NULL,
	CONSTRAINT ADMIN_PK PRIMARY KEY (admin_id)
);

CREATE TABLE INSTITUTION (
	institution_id INT AUTO_INCREMENT UNIQUE NOT NULL,
	institution_name VARCHAR(45) UNIQUE NOT NULL,
	institution_pass VARCHAR(45) NOT NULL,
	admin_id INT,
	CONSTRAINT PK_INSTITUTION PRIMARY KEY (institution_id),
	CONSTRAINT FK_INSTITUTION FOREIGN KEY (admin_id) REFERENCES ADMIN(admin_id)
);   

CREATE TABLE USER (
	user_id INT AUTO_INCREMENT UNIQUE NOT NULL,
    user_name VARCHAR(45) UNIQUE NOT NULL,
    user_pass VARCHAR(250) NOT NULL,
    user_email VARCHAR(45),
    user_isreg BOOLEAN,
    admin_id INT,
    institution_id INT NOT NULL,
    CONSTRAINT USER_PK PRIMARY KEY (user_id),
    CONSTRAINT FK_USER_1 FOREIGN KEY (admin_id) REFERENCES ADMIN(admin_id),
    CONSTRAINT FK_USER_2 FOREIGN KEY (institution_id) REFERENCES INSTITUTION(institution_id)
);

CREATE TABLE EXAM (
	exam_id INT AUTO_INCREMENT UNIQUE,
	exam_name VARCHAR(45) UNIQUE NOT NULL,
	institution_id INT NOT NULL,
    exam_start DATE NOT NULL,
    exam_end DATE NOT NULL,
	CONSTRAINT FK_EXAM_1 FOREIGN KEY (institution_id) REFERENCES INSTITUTION(institution_id),
    CONSTRAINT EXAM_PK PRIMARY KEY (exam_id)
);



CREATE TABLE PARTICIPANT (
user_id INT NOT NULL,
exam_id INT NOT NULL,
participant_isreg BOOLEAN NOT NULL,
participant_complete BOOLEAN NOT NULL,
CONSTRAINT FK_PARTICIPANT_1 FOREIGN KEY (user_id) REFERENCES USER(user_id),
CONSTRAINT FK_PARTICIPANT_2 FOREIGN KEY (exam_id) REFERENCES EXAM(exam_id),
CONSTRAINT PARTICIPANT_PK PRIMARY KEY (user_id, exam_id)
);


CREATE TABLE QUESTION (
question_id INT,
exam_id INT NOT NULL,
question_q VARCHAR(250) NOT NULL,
question_a VARCHAR(250) NOT NULL,
question_grade INT NOT NULL,
CONSTRAINT FK_QUESTION FOREIGN KEY (exam_id) REFERENCES EXAM(exam_id),
CONSTRAINT QUESTION_PK PRIMARY KEY (question_id, exam_id)
);


CREATE TABLE PARTICIPANT_ANSWER (
user_id INT NOT NULL,
exam_id INT NOT NULL,
question_id INT NOT NULL,
participant_answer_a VARCHAR(250) NOT NULL,
participant_answer_grade DOUBLE,
CONSTRAINT FK_PA_1 FOREIGN KEY (user_id) REFERENCES PARTICIPANT(user_id),
CONSTRAINT FK_PA_2 FOREIGN KEY (exam_id) REFERENCES PARTICIPANT(exam_id),
CONSTRAINT PARTICIPANT_ANSWER_PK PRIMARY KEY (user_id, exam_id, question_id)
);

DELIMITER $$
CREATE TRIGGER AUTOQUESTION BEFORE INSERT ON QUESTION
FOR EACH ROW
BEGIN
set new.question_id = (select count(*) from QUESTION where question.exam_id = new.exam_id)+1;
END
$$
DELIMITER ;

###ADMIN###
INSERT INTO ADMIN VALUES (default, 'OGADMIN', 'testing123');

###INSTITUTIONS###
INSERT INTO INSTITUTION VALUES (default, 'UNBSJ', 'password','1');
INSERT INTO INSTITUTION VALUES (default, 'UNBF', 'password','1');
INSERT INTO INSTITUTION VALUES (default, 'STFX', 'password','1');
INSERT INTO INSTITUTION VALUES (default, 'SMU', 'password','1');
INSERT INTO INSTITUTION VALUES (default, 'STU', 'password','1');

###USERS###
INSERT INTO USER VALUES (default, 'Bhris', 'password', 'Bhris@gmail.com', true, 1, 1);
INSERT INTO USER VALUES (default, 'Bobber', 'password', 'Bobber@gmail.com', true, 1, 1);
INSERT INTO USER VALUES (default, 'botWatson', 'password', 'botWatson@gmail.com', true, 1, 1);
INSERT INTO USER VALUES (default, 'Thomas', 'password', 'tommyboi@gmail.com', true, 1, 2);
INSERT INTO USER VALUES (default, 'Gigatron', 'password', 'giggy@gmail.com', false, null, 1);
INSERT INTO USER VALUES (default, 'cheekymonkey', 'password', 'cheekymonkey@gmail.com', false, null, 3);
INSERT INTO USER VALUES (default, 'f', 'f', 'f', true, 1, 1);
INSERT INTO USER VALUES (default, 'Dogon Pepper', 'password', 'schneiders@hotmail.com', false, null, 1);

###UNBSJ EXAMS START###

INSERT INTO EXAM VALUES (default, 'Water Colours Exam', 1, '2022-11-05', '2022-12-01');
INSERT INTO QUESTION VALUES (1, 1, 'What is water colours?', 'A type of painting', 10);
INSERT INTO QUESTION VALUES (1, 1, 'What colour is blue water colours?', 'Blue', 10);
INSERT INTO QUESTION VALUES (1, 1, 'What colour is yellow water colours?', 'yellow', 10);
INSERT INTO QUESTION VALUES (1, 1, 'How do you make green water colours?', 'Mix blue and yellow', 10);

INSERT INTO EXAM VALUES (default, 'Mechanics Test', 1, '2022-11-05', '2022-12-01');
INSERT INTO QUESTION VALUES (1, 2, 'What is mechanics?', 'Mechanics', 10);
INSERT INTO QUESTION VALUES (1, 2, 'What is a car?', 'A car', 10);
INSERT INTO QUESTION VALUES (1, 2, 'Is banana farming considered mechanics?', 'maybe', 10);
INSERT INTO QUESTION VALUES (1, 2, 'Metal?', 'metal', 10);

INSERT INTO EXAM VALUES (default, 'Gardening Test', 1, '2022-11-05', '2022-12-12');
INSERT INTO QUESTION VALUES (1, 3, 'What tool is used for loosening soil and chopping weeds?', 'A hoe', 10);
INSERT INTO QUESTION VALUES (1, 3, 'Is it okay to jump on crops?', 'No', 10);
INSERT INTO QUESTION VALUES (1, 3, 'What seasons are best for gardening in New Brunswick', 'Im not sure', 10);
INSERT INTO QUESTION VALUES (1, 3, 'What is irrigation?', 'Bring water to crops via pipes or trenches', 10);

INSERT INTO EXAM VALUES (default, 'Farm Animals Test', 1, '2022-11-05', '2022-12-08');
INSERT INTO QUESTION VALUES (1, 4, 'What sound does a cow make?', 'Moo', 10);
INSERT INTO QUESTION VALUES (1, 4, 'Do jaguars live on farms?', 'Only at jaguar farms maybe', 10);
INSERT INTO QUESTION VALUES (1, 4, 'What animal is used to make a McChicken', 'Chicken', 10);
INSERT INTO QUESTION VALUES (1, 4, 'What sound does a pig make', 'Oink', 10);

INSERT INTO EXAM VALUES (default, 'UNBSJ Test', 1, '2022-11-05', '2022-12-07');
INSERT INTO QUESTION VALUES (1, 5, 'Who is the best professor?', 'Dr. Mahanti', 10);
INSERT INTO QUESTION VALUES (1, 5, 'What kind of trees are on the campus?', 'Pine trees', 10);
INSERT INTO QUESTION VALUES (1, 5, 'How long does it take to walk from your car to class', 'too long', 10);
INSERT INTO QUESTION VALUES (1, 5, 'Is UNBSJ good?', 'maybe', 10);

###UNBSJ EXAMS END###

###UNBSJ PARTICIPANTS START###

###BHRIS WATERSCOLOURS
INSERT INTO PARTICIPANT VALUES (1, 1, 1, 0);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 1, 1, 'Old greggs passtime', 7.5);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 1, 2, 'Blue', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 1, 3, 'Yellow', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 1, 4, 'Mix blue and yellow', 10);
UPDATE PARTICIPANT SET participant_complete = true WHERE user_id = 1 AND exam_id = 1;

###BHRIS MECHANICS
INSERT INTO PARTICIPANT VALUES (1, 2, 1, 0);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 2, 1, 'A branch of applied mathematics', 4.5);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 2, 2, 'A method of transportation', 2.5);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 2, 3, 'maybe', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 2, 4, 'metal', 10);
UPDATE PARTICIPANT SET participant_complete = true WHERE user_id = 1 AND exam_id = 2;

###BHRIS GARDENING
INSERT INTO PARTICIPANT VALUES (1, 3, 1, 0);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 3, 1, 'A rake', null);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 3, 2, 'No', null);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 3, 3, 'maybe',null);
INSERT INTO PARTICIPANT_ANSWER VALUES(1, 3, 4, 'It involves water', null);
UPDATE PARTICIPANT SET participant_complete = true WHERE user_id = 1 AND exam_id = 3;

###BOBBER WATERCOLOURS
INSERT INTO PARTICIPANT VALUES (2, 1, 1, 0);
INSERT INTO PARTICIPANT_ANSWER VALUES(2, 1, 1, 'A type of painting', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(2, 1, 2, 'Blue', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(2, 1, 3, 'Yellow', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(2, 1, 4, 'Mix blue and yellow', 10);
UPDATE PARTICIPANT SET participant_complete = true WHERE user_id = 2 AND exam_id = 1;

###BOBBER MECHANICS
INSERT INTO PARTICIPANT VALUES (2, 2, 1, 0);
INSERT INTO PARTICIPANT_ANSWER VALUES(2, 2, 1, 'Mechanics', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(2, 2, 2, 'A car', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(2, 2, 3, 'maybe', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(2, 2, 4, 'metal', 10);
UPDATE PARTICIPANT SET participant_complete = true WHERE user_id = 2 AND exam_id = 2;

###BOTWATSON GARDENING
INSERT INTO PARTICIPANT VALUES (3, 3, 1, 0);
INSERT INTO PARTICIPANT_ANSWER VALUES(3, 3, 1, 'A hoe', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(3, 3, 2, 'No', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(3, 3, 3, 'I dont know', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(3, 3, 4, 'Water to plants', 7.5);
UPDATE PARTICIPANT SET participant_complete = true WHERE user_id = 3 AND exam_id = 3;

###BOTWATSON WATERCOLOURS
INSERT INTO PARTICIPANT VALUES (3, 1, 1, 0);
INSERT INTO PARTICIPANT_ANSWER VALUES(3, 1, 1, 'A type of painting', null);
INSERT INTO PARTICIPANT_ANSWER VALUES(3, 1, 2, 'Blue', null);
INSERT INTO PARTICIPANT_ANSWER VALUES(3, 1, 3, 'Yellow', null);
INSERT INTO PARTICIPANT_ANSWER VALUES(3, 1, 4, 'Mix Blue and Yellow', null);
UPDATE PARTICIPANT SET participant_complete = true WHERE user_id = 3 AND exam_id = 1;

###UNBSJ PARTICIPANTS END###

###UNBF EXAMS START###

INSERT INTO EXAM VALUES (default, 'Fruit', 1, '2022-11-05', '2025-12-01');
INSERT INTO QUESTION VALUES (1, 6, 'What colour is an apple', 'Red', 10);
INSERT INTO QUESTION VALUES (1, 6, 'What colour is a banana', 'Yellow', 10);
INSERT INTO QUESTION VALUES (1, 6, 'What colour is a blueberry', 'Blue', 10);

###UNBF EXAMS END###

###UNBF PARTICIPANTS START###

INSERT INTO PARTICIPANT VALUES (4, 6, 1, 0);
INSERT INTO PARTICIPANT_ANSWER VALUES(4, 6, 1, 'Red', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(4, 6, 2, 'Yellow', 10);
INSERT INTO PARTICIPANT_ANSWER VALUES(4, 6, 3, 'Blue', 10);
UPDATE PARTICIPANT SET participant_complete = true WHERE user_id = 4 AND exam_id = 6;

###UNBF PARTICIPANTS END###

###STFX EXAMS START###

INSERT INTO EXAM VALUES (default, 'testExam', 3, '2022-11-05', '2025-12-06');
INSERT INTO QUESTION VALUES (1, 7, 'What colour is an apple', 'Red', 10);
INSERT INTO QUESTION VALUES (1, 7, 'What colour is a banana', 'Yellow', 10);
INSERT INTO QUESTION VALUES (1, 7, 'What colour is a blueberry', 'Blue', 10);

###STFX EXAMS END###

###SMU EXAMS START###

INSERT INTO EXAM VALUES (default, 'testExam2', 4, '2022-11-05', '2025-12-06');
INSERT INTO QUESTION VALUES (1, 8, 'What colour is an apple', 'Red', 10);
INSERT INTO QUESTION VALUES (1, 8, 'What colour is a banana', 'Yellow', 10);
INSERT INTO QUESTION VALUES (1, 8, 'What colour is a blueberry', 'Blue', 10);

###SMU EXAMS END###

###STU EXAMS START###

INSERT INTO EXAM VALUES (default, 'testExam3', 5, '2022-11-05', '2025-12-06');
INSERT INTO QUESTION VALUES (1, 9, 'What colour is an apple', 'Red', 10);
INSERT INTO QUESTION VALUES (1, 9, 'What colour is a banana', 'Yellow', 10);
INSERT INTO QUESTION VALUES (1, 9, 'What colour is a blueberry', 'Blue', 10);

###STU EXAMS END###

SELECT * FROM USER;

SELECT * FROM INSTITUTION;

SELECT * FROM EXAM;

SELECT * FROM PARTICIPANT;

SELECT * FROM PARTICIPANT_ANSWER;

SELECT question_id, question_q, question_a, participant_answer_a, participant_answer_grade, question_grade FROM QUESTION NATURAL JOIN PARTICIPANT_ANSWER WHERE user_id = 1 AND exam_id = 1


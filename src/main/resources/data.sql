MERGE INTO genres (title) KEY (title) VALUES ('Комедия');
MERGE INTO genres (title) KEY (title) VALUES ('Драма');
MERGE INTO genres (title) KEY (title) VALUES ('Мультфильм');
MERGE INTO genres (title) KEY (title) VALUES ('Триллер');
MERGE INTO genres (title) KEY (title) VALUES ('Документальный');
MERGE INTO genres (title) KEY (title) VALUES ('Боевик');
MERGE INTO mpa (title) KEY (title) VALUES ('G');
MERGE INTO mpa (title) KEY (title) VALUES ('PG');
MERGE INTO mpa (title) KEY (title) VALUES ('PG-13');
MERGE INTO mpa (title) KEY (title) VALUES ('R');
MERGE INTO mpa (title) KEY (title) VALUES ('NC-17');
MERGE INTO films (name, description, release_date, duration, mpa_id)
KEY (name, description, release_date, duration, mpa_id) VALUES
('testname1', 'testdescription1', '2025-01-01', 1000, 1);
MERGE INTO films (name, description, release_date, duration, mpa_id)
KEY (name, description, release_date, duration, mpa_id) VALUES
( 'testname2', 'testdescription2', '2025-01-01', 1000, 5);
MERGE INTO films (name, description, release_date, duration, mpa_id)
KEY (name, description, release_date, duration, mpa_id) VALUES
( 'testname3', 'testdescription3', '2023-01-01', 10, 5);
MERGE INTO users (email, login, name, BIRTHDAY) KEY (email, login, name, BIRTHDAY) VALUES
('test1@mail.ru', 'testlogin1', 'testname1', '2002-01-01');
MERGE INTO users (email, login, name, BIRTHDAY) KEY (email, login, name, BIRTHDAY) VALUES
('test2@mail.ru', 'testlogin2', 'testname2', '2002-01-01');
MERGE INTO users (email, login, name, BIRTHDAY) KEY (email, login, name, BIRTHDAY) VALUES
('test3@mail.ru', 'testlogin3', 'testname3', '2002-01-01');
MERGE INTO film_genre (film_id, genre_id) KEY (film_id, genre_id) VALUES (1, 1);
MERGE INTO film_genre (film_id, genre_id) KEY (film_id, genre_id) VALUES (1, 2);
MERGE INTO film_genre (film_id, genre_id) KEY (film_id, genre_id) VALUES (1, 3);
MERGE INTO film_genre (film_id, genre_id) KEY (film_id, genre_id) VALUES (2, 3);
MERGE INTO film_likes (user_id, film_id) KEY (user_id, film_id) VALUES (1, 1);
MERGE INTO film_likes (user_id, film_id) KEY (user_id, film_id) VALUES (1, 2);
MERGE INTO film_likes (user_id, film_id) KEY (user_id, film_id) VALUES (2, 1);
MERGE INTO film_likes (user_id, film_id) KEY (user_id, film_id) VALUES (3, 1);




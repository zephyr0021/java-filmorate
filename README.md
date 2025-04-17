# Filmorate DB

Filmorate — это база данных для социальной платформы о кино, где пользователи могут лайкать фильмы, добавлять друзей и просматривать информацию о фильмах, жанрах и рейтингах.

## Структура базы

- `users` — пользователи
- `films` — фильмы
- `genres` — жанры фильмов
- `rating` — возрастные рейтинги
- `film_genre` — связь "многие ко многим" между фильмами и жанрами
- `film_likes` — лайки пользователей
- `friends` — друзья пользователей

## Примеры SQL-запросов

### Получить все фильмы с жанрами и рейтингом
```sql
SELECT f.id, f.name, f.description, r.title AS rating, g.title AS genre
FROM films f
LEFT JOIN rating r ON f.rating_id = r.id
LEFT JOIN film_genre fg ON f.id = fg.film_id
LEFT JOIN genres g ON fg.genre_id = g.id;

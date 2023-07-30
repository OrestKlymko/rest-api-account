CREATE TABLE IF NOT EXISTS users
(
    email    VARCHAR(50) PRIMARY KEY CHECK (email like '%_@__%.__%'),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(10)
)
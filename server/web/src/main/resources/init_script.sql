CREATE TABLE Users
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE
);

INSERT INTO users (username)
VALUES ('testUser1');
INSERT INTO users (username)
VALUES ('testUser2');

CREATE TABLE UserSessions
(
    userId    INTEGER NOT NULL,
    sessionId TEXT    NOT NULL UNIQUE,
    FOREIGN KEY (userId) REFERENCES users (id)
);

CREATE TABLE Rounds
(
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    ownerId INTEGER NOT NULL,
    status  TEXT    NOT NULL CHECK (status IN ('NOT_STARTED', 'ONGOING', 'FINISHED')),
    FOREIGN KEY (ownerId) REFERENCES users (id)
);
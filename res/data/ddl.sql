DROP TABLE bookmark;
DROP TABLE bookmark_has_tag;
DROP TABLE environment;
DROP TABLE tag;

CREATE TABLE bookmark
(
  idbookmark  INTEGER        NOT NULL,
  title       VARCHAR(100)   NOT NULL,
  url         VARCHAR(65535) NOT NULL,
  modified       DATETIME       NOT NULL,
  description VARCHAR(255),
  environment INTEGER,
  PRIMARY KEY (idbookmark),
  FOREIGN KEY (environment) REFERENCES environment (idenvironment)
);

CREATE TABLE bookmark_has_tag
(
  idbookmark INTEGER NOT NULL,
  idtag      INTEGER NOT NULL,
  PRIMARY KEY (idbookmark, idtag),
  FOREIGN KEY (idbookmark) REFERENCES bookmark (idbookmark),
  FOREIGN KEY (idtag) REFERENCES tag (idtag)
);

CREATE TABLE environment
(
  idenvironment INTEGER     NOT NULL,
  name          VARCHAR(45) NOT NULL,
  description   VARCHAR(255),
  color         VARCHAR(7),
  PRIMARY KEY (idenvironment)
);

CREATE TABLE tag
(
  idtag INTEGER     NOT NULL,
  tag   VARCHAR(45) NOT NULL,
  PRIMARY KEY (idtag),
  UNIQUE (idtag, tag)
);
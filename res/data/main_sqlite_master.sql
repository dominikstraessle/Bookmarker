INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('table', 'bookmark', 'bookmark', 2, 'CREATE TABLE bookmark
(
  idbookmark  INTEGER        NOT NULL,
  title       VARCHAR(100)   NOT NULL,
  url         VARCHAR(65535) NOT NULL,
  modified       DATETIME       NOT NULL,
  description VARCHAR(255),
  environment INTEGER,
  PRIMARY KEY (idbookmark),
  FOREIGN KEY (environment) REFERENCES environment (idenvironment)
)');
INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('table', 'bookmark_has_tag', 'bookmark_has_tag', 3, 'CREATE TABLE bookmark_has_tag
(
  idbookmark INTEGER NOT NULL,
  idtag      INTEGER NOT NULL,
  PRIMARY KEY (idbookmark, idtag),
  FOREIGN KEY (idbookmark) REFERENCES bookmark (idbookmark),
  FOREIGN KEY (idtag) REFERENCES tag (idtag)
)');
INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('index', 'sqlite_autoindex_bookmark_has_tag_1', 'bookmark_has_tag', 5, null);
INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('table', 'environment', 'environment', 6, 'CREATE TABLE environment
(
  idenvironment INTEGER     NOT NULL,
  name          VARCHAR(45) NOT NULL,
  description   VARCHAR(255),
  color         VARCHAR(7),
  PRIMARY KEY (idenvironment)
)');
INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('table', 'tag', 'tag', 4, 'CREATE TABLE tag
(
  idtag INTEGER     NOT NULL,
  tag   VARCHAR(45) NOT NULL,
  PRIMARY KEY (idtag),
  UNIQUE (idtag, tag)
)');
INSERT INTO sqlite_master (type, name, tbl_name, rootpage, sql) VALUES ('index', 'sqlite_autoindex_tag_1', 'tag', 7, null);
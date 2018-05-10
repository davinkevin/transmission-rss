
DROP TABLE PATTERN_MATCHER;
CREATE TABLE PATTERN_MATCHER (
  URL VARCHAR,
  MATCHER VARCHAR,
  EXCLUDE VARCHAR,
  DOWNLOAD_PATH VARCHAR,
  CONSTRAINT PATTERN_MATCHER UNIQUE(URL,MATCHER,EXCLUDE,DOWNLOAD_PATH)
);

CREATE TABLE IF NOT EXISTS RSS_ITEM (
  TITLE VARCHAR,
  LINK VARCHAR,
  GUID VARCHAR,
  PUB_DATE TIMESTAMP WITH TIME ZONE,
  FROM_FEED VARCHAR,
  STATUS VARCHAR,
  DOWNLOADED_DATE TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY (LINK)
)
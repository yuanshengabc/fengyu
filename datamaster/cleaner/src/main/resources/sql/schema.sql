CREATE TABLE IF NOT EXISTS datahouse (
  dhid INT AUTO_INCREMENT PRIMARY KEY,
  uid INT,
  name VARCHAR(64),
  description VARCHAR(128),
  ip CHAR(64),
  port INT,
  username VARCHAR(30),
  password VARCHAR(16),
  created_on TIMESTAMP default CURRENT_TIMESTAMP,
  modified_on DATETIME,
  UNIQUE KEY `name` (name)
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `database` (
  dbid INT AUTO_INCREMENT PRIMARY KEY,
  dhid INT,
  name VARCHAR(255),
  INDEX dhid_index (dhid)
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS datatable (
  dtid INT AUTO_INCREMENT PRIMARY KEY,
  dhid INT,
  dbname VARCHAR(255),
  dtname VARCHAR(255),
  INDEX db_index (dhid, dbname(10))
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS datasource (
  dsid INT AUTO_INCREMENT PRIMARY KEY,
  dtname VARCHAR(255),
  dbname VARCHAR(255),
  dhid INT,
  rsid INT,
  status INT,
  description VARCHAR(255),
  ntotal INT,
  created_on DATETIME
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS datamaster_source (
  dsid INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64),
  rsid INT,
  source VARCHAR(64),
  description VARCHAR(128),
  created_on DATETIME,
  UNIQUE KEY `name` (name)
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS from_source (
  fdsid INT AUTO_INCREMENT PRIMARY KEY,
  dsid INT,
  `from` INT,
  stype INT
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS objecttype (
  otid INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64),
  description VARCHAR(40),
  UNIQUE KEY `name` (name)
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS propertygroup (
  pgid INT AUTO_INCREMENT PRIMARY KEY,
  otid INT, -- object id
  name VARCHAR(64),
  UNIQUE KEY `ot_name` (otid, name)
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS propertytype (
  ptid INT AUTO_INCREMENT PRIMARY KEY,
  otid INT,
  pgid INT, -- property group id.
  name VARCHAR(64),
  baseType INT,
  semaName VARCHAR(128),
  description VARCHAR(40),
  validationRule VARCHAR(100),
  UNIQUE KEY `ot_name` (otid, name),
  UNIQUE KEY `pg_sema_name` (`pgid`,`semaName`)
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS workspace (
  wsid INT AUTO_INCREMENT PRIMARY KEY,
  wstype INT DEFAULT 0,
  name VARCHAR(128),
  uid INT,
  finished INT,
  description VARCHAR(128),
  created_on DATETIME,
  modified_on DATETIME,
  UNIQUE KEY `name` (name)
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cleaning_workspace (
  wsid INT PRIMARY KEY,
  wsversion TINYINT
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS fusion_workspace (
  wsid INT PRIMARY KEY,
  step INT DEFAULT 0,
  otid INT,
  rsid INT,
  addressCodeType INT,
  entropyCalculationTid INT,
  threshold DOUBLE,
  fusionTid INT
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS workspace_edition (
  weid INT AUTO_INCREMENT PRIMARY KEY,
  wsid INT,
  wsversion SMALLINT,
  uid INT,
  rsid INT,
  otid INT,
  importid INT,
  analysisid INT,
  transformid INT,
  exportid INT,
  modified_on DATETIME
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS workspace_source (
  wsdsid INT AUTO_INCREMENT PRIMARY KEY,
  wsid INT,
  sid INT,
  stype INT,
  created_on DATETIME
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ontology_source (
  wsdsid INT PRIMARY KEY,
  `match` BOOLEAN,
  multimatch_ptids MEDIUMTEXT
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ontology_mapping (
  omid INT AUTO_INCREMENT PRIMARY KEY,
  wsid INT,
  wsversion INT,
  field_name VARCHAR(255),
  ptid INT,
  INDEX ws_field_index (wsid, wsversion, field_name(10))
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS fusion_property (
  wsptid INT AUTO_INCREMENT PRIMARY KEY,
  wsid INT,
  ptid INT,
  `unique` BOOLEAN
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS transformation (
  tfid INT AUTO_INCREMENT PRIMARY KEY,
  tftype VARCHAR(255),
  wsid INT,
  wsversion INT,
  args MEDIUMTEXT,
  filters MEDIUMTEXT,
  INDEX ws_index (wsid, wsversion)
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS workspace_edition_task (
  wetid INT AUTO_INCREMENT PRIMARY KEY,
  weid INT,
  tid INT
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `task` (
  tid INT AUTO_INCREMENT PRIMARY KEY,
  status TINYINT,
  source_id INT,
  name VARCHAR(255),
  sink_id INT,
  created_on DATETIME,
  modified_on DATETIME,
  type TINYINT,
  extra_data MEDIUMTEXT
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS recordsource (
  rsid INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  dsid INT,
  type INT,
  created_on TIMESTAMP default CURRENT_TIMESTAMP
)DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS rsfield (
  fid INT AUTO_INCREMENT PRIMARY KEY,
  rsid INT,
  name VARCHAR(255),
  type TINYINT
)DEFAULT CHARSET=utf8mb4;

ALTER TABLE workspace CHANGE COLUMN `name` `name` VARCHAR(128);

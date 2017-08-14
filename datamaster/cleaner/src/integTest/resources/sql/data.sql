CREATE DATABASE IF NOT EXISTS test1;
CREATE DATABASE IF NOT EXISTS test2;
CREATE DATABASE IF NOT EXISTS test3;

INSERT INTO datatable(dhid, dbname, dtname)
    VALUES
    (1, 'test1', 'test1'),
    (1, 'test1', 'test2');

DROP TABLE IF EXISTS test1.test1;
CREATE TABLE IF NOT EXISTS test1.test1 (f1 VARCHAR(255), f2 INT, field1 VARCHAR(255), field2 INT, field3 DATETIME);

INSERT INTO test1.test1(f1, f2)
    VALUES
    ('aBc', 1),
    ('bCd', 2);

DROP TABLE IF EXISTS test1.test2;
CREATE TABLE IF NOT EXISTS test1.test2 (pt1 VARCHAR(255), pt2 VARCHAR(255), pt3 VARCHAR(255), pt4 VARCHAR(255), pt5 VARCHAR(255), pt6 VARCHAR(255));

INSERT INTO test1.test2(pt1)
    VALUES
    ('aBc'),
    ('bCd');

INSERT INTO datahouse(dhid, uid, name, description, ip, port, username, password, modified_on)
    VALUES
    (1, 1, 'test1', '用于测试的dh1', '172.24.8.115', 3306, 'datamaster', 'datA123!@#', now()),
    (2, 1, 'test2', '用于测试的dh2', '172.24.8.115', 3306, 'datamaster', 'datA123!@#', now());

INSERT INTO `database`(dhid, name)
    VALUES
    (1, 'test1'),
    (1, 'test2'),
    (1, 'test3');

INSERT INTO datasource(dsid, dtname, dbname, dhid, rsid, status, description, ntotal, created_on)
    VALUES
    (1, 'test1', 'test1', 1, 1, 1, 'data source test1', 2, now()),
    (2, 'test2', 'test1', 1, 1, 1, 'data source test2', 2, now()),
    (3, 'test1', 'test2', 2, 1, 1, 'data source test1', 10, now()),
    (4, 'test2', 'test2', 2, 1, 1, 'data source test2 ', 5, now());

INSERT INTO datamaster_source(dsid, name, rsid, source, description)
    VALUES
    (1, 'starRiver1', 1, 'fusion', 'fusion data test1'),
    (2, 'starRiver2', 2, 'fusion', 'fusion data test2');

INSERT INTO from_source(fdsid, dsid, `from`, stype) VALUES
    (1, 2, 1, 1),
    (2, 2, 1, 0),
    (3, 2, 2, 0),
    (4, 1, 1, 0),
    (5, 1, 3, 0);

INSERT INTO objecttype(otid, name, description)
    VALUES
    (1, "ot1", "ot test 1"),
    (2, "ot2", "ot test 2"),
    (3, "otmatch", "ot test 2");

INSERT INTO propertygroup(pgid, otid, name)
    VALUES
    (1, 1, "pg1"),
    (2, 1, "pg2");

INSERT INTO propertytype(ptid, otid, pgid, name, baseType, semaName, description, validationRule)
    VALUES
    (1, 1, 1, "pt1", 0, "ptSema1", "pt1", "rule1"),
    (2, 1, 1, "pt2", 0, "ptSema2", "pt2", "rule2"),
    (3, 1, 2, "pt3", 0, "ptSema3", "pt3", "rule3"),
    (4, 1, 2, "pt4", 0, "ptSema4", "pt4", "rule4"),
    (5, 1, null, "pt5", 0, "ptSema5", "pt5", "rule5"),
    (6, 1, null, "pt6", 0, "ptSema6", "pt6", "rule6");

INSERT INTO propertytype(ptid, otid, name, baseType, semaName, description, validationRule)
    VALUES
    (11, 3, "f1", 0, "f1", "f1", "rule11"),
    (12, 3, "f2", 1, "f2", "f2", "rule12"),
    (13, 3, "field1", 0, "field1", "field1", "rule13"),
    (14, 3, "field2", 1, "field2", "field2", "rule14"),
    (15, 3, "field3", 5, "field3", "field3", "rule15");

INSERT INTO workspace(wsid, wstype, name, uid, finished, description, created_on, modified_on)
    VALUES
    (1, 0, 'wstest1', 1, 0, 'ws desc1', now(), null),
    (2, 0, 'wstest2', 1, 1, 'ws desc2', now(), null),
    (3, 1, 'step1', 1, 0, 'step1 desc', now(), null),
    (4, 1, 'step2', 1, 0, 'step2 desc', now(), null),
    (5, 1, 'step3', 1, 0, 'step3 desc', now(), null),
    (6, 1, 'step4', 1, 0, 'step4 desc', now(), null);

INSERT INTO cleaning_workspace(wsid, wsversion)
    VALUES
    (1, 1),
    (2, 1);

INSERT INTO fusion_workspace(wsid, step, otid, threshold, rsid, addressCodeType, entropyCalculationTid, fusionTid)
    VALUES
    (3, 0, null, null, 3, null, null, null),
    (4, 1, 3, null, 4, null, null, null),
    (5, 2, 3, null, 5, null, null, null),
    (6, 3, 3, 1.0, 6, 1, 11, 12);

INSERT INTO workspace_edition(weid, wsid, wsversion, uid, rsid, otid, importid, analysisid, transformid, exportid, modified_on)
    VALUES
    (1, 1, 1, 1, 1, 1, 1, 2, 3, 4, now()),
    (2, 2, 1, 1, 2, 1, 5, 6, 7, 8, now());

INSERT INTO workspace_source(wsdsid, wsid, sid, stype, created_on)
    VALUES
    (1, 1, 1, 0, NOW()),
    (2, 2, 2, 0, NOW()),
    (3, 5, 1, 0, NOW()),
    (4, 5, 1, 1, NOW()),
    (5, 6, 1, 1, NOW()),
    (6, 6, 1, 1, NOW());

INSERT INTO ontology_source(wsdsid, `match`)
    VALUES
    (3, true),
    (4, true),
    (5, true),
    (6, true);

INSERT INTO ontology_mapping(wsid, wsversion, field_name, ptid)
    VALUES
    (1, 1, 'f1', 1),
    (1, 1, 'f2', 2),
    (1, 1, 'field1', 1);

INSERT INTO fusion_property(wsptid, wsid, ptid, `unique`)
    VALUES
    (1, 6, 11, TRUE ),
    (2, 6, 12, FALSE ),
    (3, 6, 13, FALSE ),
    (4, 6, 14, FALSE ),
    (5, 6, 15, FALSE );

INSERT INTO transformation(tfid, tftype, wsid, wsversion, args)
    VALUES
    (1, 'ToLowerTF', 1, 1, '{\'sourceFieldName\': \'f1\'}'),
    (2, 'AddFieldTF', 1, 1, '{\'sourceFieldName\': \'f1\', \'targetFieldName\': \'n2\', \'baseType\': \'TEXT\'}'),
    (3, 'ToUpperTF', 1, 1, '{\'sourceFieldName\': \'n2\'}'),
    (4, 'SplitFieldTF', 1, 1, '{\'sourceFieldName\': \'field1\', \'splitCount\': 2, \'sep\': \',\'}'),
    (5, 'ToLowerTF', 1, 1, '{\'sourceFieldName\': \'field1_1\'}'),
    (6, 'ToLowerTF', 1, 1, '{\'sourceFieldName\': \'field1_2\'}'),
    (7, 'AddFieldTF', 1, 1, '{\'sourceFieldName\': \'f1\', \'baseType\': \'TEXT\', \'targetFieldName\': \'n3\'}'),
    (8, 'ConvertTypeTF', 1, 1, '{\'sourceFieldName\': \'field2\', \'targetType\': \'DATE\'}');

INSERT INTO workspace_edition_task(wetid, weid, tid)
    VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 1, 3),
    (4, 1, 4),
    (5, 2, 5),
    (6, 2, 6),
    (7, 2, 7),
    (8, 2, 8);

INSERT INTO task(tid, status, source_id, name, sink_id, type)
    VALUES
    (1, 1, 1, "test1", 2, 0),
    (2, 0, 1, "test2", 2, 1),
    (3, 2, 1, "test3", 2, 2),
    (4, -1, 1, "test4", 2, 3),
    (5, 1, 1, "test5", 2, 0),
    (6, 3, 1, "test6", 2, 1),
    (7, 1, 1, "test7", 2, 2),
    (8, 3, 1, "test8", 2, 3);

INSERT INTO task(tid, status, source_id, name, sink_id, type)
    VALUES
    (11, 2, 1, "entropy_compute", 2, 4),
    (12, 2, 1, "similarity_compute", 2, 5);

INSERT INTO recordsource(rsid, name, dsid, type)
    VALUES
    (1, 'rs1', 1, 0),
    (2, 'rs2', 1, 0),
    (3, 'rs3', 1, 0),
    (4, 'rs4', 1, 0),
    (5, 'rs4', 1, 0),
    (6, 'rs4', 1, 0);

INSERT INTO rsfield(rsid, name, type)
    VALUES
    (1, 'f1', 0),
    (1, 'f2', 1),
    (1, 'field1', 0),
    (1, 'field2', 1),
    (1, 'field3', 5);

INSERT INTO rsfield(rsid, name, type)
    VALUES
    (2, 'pt1', 0),
    (2, 'pt2', 0),
    (2, 'pt3', 0),
    (2, 'pt4', 0),
    (2, 'pt5', 0),
    (2, 'pt6', 0);

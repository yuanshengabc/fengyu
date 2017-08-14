#!/usr/bin/python
import sys
import json
import os
import uuid

def main():
    reload(sys)
    sys.setdefaultencoding('utf8')

    inputs = ''
    for line in sys.stdin:
        inputs += line

    param = json.loads(inputs)

    fs_id = param["fsid"]
    table_base_path = "/fusion/" + fs_id + "/tables/"

    db_configs = param["dbConfigs"]
    for config in db_configs:
        type = config["type"]
        ip = config["ip"]
        port = config["port"]
        username = config["username"]
        password = config["password"]
        db_name = config["dbName"]
        table_name = config["tableName"]
        uuid_pre = str(uuid.uuid4())[0:24]

        table_path = "'" + table_base_path + type.lower() + "." + table_name
        table_path = table_path + "'"

        table_name = "'" + table_name
        table_name = table_name + "'"

        params = [connect_str(type, ip, port, db_name), username, password, table_name, table_path, uuid_pre]
        os.system("sh /opt/datamaster/bin/fusion/sqoop-import.sh " + " ".join(params))

def connect_str(type, ip, port, dbName):
    lower_type = type.lower()
    if lower_type == "mysql":
        return "jdbc:mysql://" + ip + ":" + str(port) + "/" + dbName + "?useSSL=false"
    elif lower_type == "postgresql":
        return "jdbc:postgresql://" + ip + "/" + dbName
    elif lower_type == "sqlserver":
        return ""
    elif lower_type == "db2":
        return ""
    elif lower_type == "oracle":
        return ""

if __name__ == "__main__":
    main()

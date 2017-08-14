#!/usr/bin/python
# -*- coding: UTF-8 -*-
import json
import codecs
import os
import sys, getopt


def main(argv):
    input_file = ''
    output_file = ''

    # 无命令和参数时
    if not argv:
        usage()
        sys.exit(2)

    try:
        opts, args = getopt.getopt(argv, "hi:o:", ["help", "input=", "output="])
    except getopt.GetoptError:
        print 'Error: Please write correct parameter'
        sys.exit(2)

    # 没有命令时
    if not opts:
        print 'Error: Please write correct parameter'
        sys.exit(2)

    # 扫描命令并传递参数
    for opt, arg in opts:
        if opt in ('-h', '--help'):
            usage()
            sys.exit(2)
        elif opt in ("-i", "--import"):
            input_file = arg
        elif opt in ("-o", "--output"):
            output_file = arg

    trandsform(input_file, output_file)


def trandsform(input_file, output_file):
    try:
        with open(input_file) as f:
            try:
                setting = json.load(f)
            except ValueError:
                print 'Error: No JSON object could be decoded'
                sys.exit(2)
            object_type = setting["ObjectTypes"]
    except IOError:
        print "Error: No such input '" + input_file + "'"
        sys.exit(2)

    try:
        if check_properties_name(object_type) != 1:
            print check_properties_name(object_type)
            sys.exit(2)
    except KeyError:
        print "Error :Incorrect ontology file"
        sys.exit(2)
    except Exception, e:
        print repr(e)
        sys.exit(2)

    try:
        file_object = codecs.open(output_file, 'w', "utf-8")
    except IOError:
        print "Error: Incorrect sql"
        sys.exit(2)

    try:
        file_object.write('DELIMITER $$\r\n'
                          'DROP PROCEDURE IF EXISTS load_ontology $$\r\n'
                          'CREATE PROCEDURE load_ontology()\r\n'
                          'BEGIN\r\n'
                          'DECLARE t_error INTEGER DEFAULT 0;\r\n'
                          "DECLARE V_ERR_MSG VARCHAR(100) DEFAULT 'Insert database successfully';\r\n"
                          'DECLARE CONTINUE HANDLER FOR SQLEXCEPTION,SQLWARNING,'
                          'NOT FOUND  SET t_error=1;\r\n'
                          'START TRANSACTION;\r\n')
        transform_object_type(object_type, file_object)
        file_object.write("IF t_error = 1 THEN ROLLBACK;SET V_ERR_MSG='Failed to insert database'; "
                          "ELSE COMMIT; END IF;\r\n"
                          "SELECT V_ERR_MSG AS 'Message:';"
                          'END$$\r\n'
                          'call load_ontology();$$\r\n'
                          'DROP PROCEDURE load_ontology;$$\r\n'
                          'DELIMITER ;\r\n')
    except KeyError:
        print('Error :Incorrect ontology file')
        os.remove(output_file)
        sys.exit(2)
    file_object.close()


def usage():
    print 'usage:'
    print 'python ontology-import.py -i <input> -o <output>'


# 检查properties的name是否有重复
def check_properties_name(object_type):
    str1 = {}
    i = 0
    str2 = {}
    j = 0
    for k in range(len(object_type)):
        if "properties" in object_type[k]:
            properties = object_type[k]["properties"]
            for p in range(len(properties)):
                if properties[p]["name"] in str1.values():
                    str2[j] = properties[p]["name"]
                    j += 1
                else:
                    str1[i] = properties[p]["name"]
                    i += 1
        if "propertyGroups" in object_type[k]:
            property_group = object_type[k]["propertyGroups"]
            for q in range(len(property_group)):
                property = property_group[q]["properties"]
                for s in range(len(property)):
                    if property[s]["name"] in str1.values():
                        str2[j] = property[s]["name"]
                        j += 1
                    else:
                        str1[i] = property[s]["name"]
                        i += 1
    if len(str2) == 0:
        return 1
    else:
        str3 = "Error: Exist same property name"
        for m in range(len(str2)):
            str3 += "\r\n" + str2[m]
        return str3


# 转换ObjectTypes
def transform_object_type(object_type, file_object):
    for i in range(len(object_type)):
        str = "INSERT INTO objecttype(name) VALUES "
        str = str + "('%s');\r\n" % (object_type[i]['name'])
        file_object.write(str)
        file_object.write("set @last_object_id = LAST_INSERT_ID();\r\n")

        if "properties" in object_type[i]:
            properties = object_type[i]["properties"]
            transform_properties(properties, file_object)

        if "propertyGroups" in object_type[i]:
            property_group = object_type[i]["propertyGroups"]
            transform_property_group(property_group, file_object)


# 转换ObjectTypes中的properties
def transform_properties(properties, file_object):
    for i in range(len(properties)):
        try:
            basetype = transform_basetype(properties[i]["baseType"])
        except ValueError:
            print "Error: Unknown basetype '" + properties[i]["baseType"] + "'"
            sys.exit()

        if "semaType" in properties[i]:
            str = "INSERT INTO propertytype(otid,pgid,name,baseType,semaName) VALUES "
            str = str + "(%s, %s,'%s',%s,'%s');\r\n" % (
                "@last_object_id", 'null', properties[i]["name"], basetype, properties[i]["semaType"])
        else:
            str = "INSERT INTO propertytype(otid,pgid,name,basetype,semaName) VALUES "
            str = str + "(%s,%s,'%s',%s,'%s');\r\n" % (
                "@last_object_id", 'null', properties[i]["name"], basetype, properties[i]["name"])
        file_object.write(str)


# 转换propertyGroups
def transform_property_group(property_groups, file_object):
    for i in range(len(property_groups)):
        str = "INSERT INTO propertygroup(otid,name) VALUES "
        str = str + "(%s,'%s');\r\n" % ('@last_object_id', property_groups[i]["name"])
        file_object.write(str)
        file_object.write("set @last_property_group_id = LAST_INSERT_ID();\r\n")

        property = property_groups[i]["properties"]
        transform_properties_in_pg(property, file_object)


# 转换propertyGroups中的properties
def transform_properties_in_pg(property, file_object):
    for i in range(len(property)):
        try:
            basetype = transform_basetype(property[i]["baseType"])
        except ValueError:
            print "Error: Unknown basetype '" + property[i]["baseType"] + "'"
            sys.exit()

        if "semaType" in property[i]:
            str = "INSERT INTO propertytype(otid,pgid,name,baseType,semaName) VALUES "
            str = str + "(%s,%s,'%s',%s,'%s');\r\n" % (
                "@last_object_id", "@last_property_group_id", property[i]["name"], basetype, property[i]["semaType"])
        else:
            str = "INSERT INTO propertytype(otid,pgid,name,basetype,semaName) VALUES "
            str = str + "(%s,%s,'%s',%s,'%s');\r\n" % (
                "@last_object_id", "@last_property_group_id", property[i]["name"], basetype, property[i]["name"])
        file_object.write(str)


def transform_basetype(basetype):
    if basetype == 'Text':
        return 0
    elif basetype == 'Int':
        return 1
    elif basetype == 'BigInt':
        return 2
    elif basetype == 'Float':
        return 3
    elif basetype == 'Double':
        return 4
    elif basetype == 'Date':
        return 5
    else:
        raise ValueError


if __name__ == "__main__":
    main(sys.argv[1:])

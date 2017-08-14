#!/bin/python
import sys
import base64

def main():
    column = sys.argv[1]
    column = column[1:]
    column = column.replace('0', '=')
    column = base64.b32decode(column)
    print column

if __name__ == '__main__':
    main()
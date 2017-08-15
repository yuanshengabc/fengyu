string = 'aZZg/x\ZbavpZiEZp+n)o+'
a = []
for i in string:
  a.append(ord(i))
print a
for i in range(26):
    string1 = ''
    for c in string:
        string1 = string1 + chr(ord(c) + i)
    print string1
    string2 = ''
    for c in string:
        string2 = string2 + chr(ord(c) - i)
    print string2

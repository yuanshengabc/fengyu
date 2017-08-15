string = 'dloguszijluswogany'
flag = 1
c = ''
for i in string:
    if flag != 2:
        c = i
        flag += 1
        continue
    else:
        print chr(((ord(c) - 96) + (-2) * (ord(i) - 96)) % 26 + 96)
        print i
        flag = 1

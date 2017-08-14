with open('/home/yuansheng/yuansheng/test/dictionary.txt') as f:
    count = 0
    i = 1
    for i in range(705373):
        string = f.readline().strip()
        if 'ctf' in string:
            count = count + len(string)
    print count

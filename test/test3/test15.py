n = 920139713
e1 = 19
x = 920071380
e2 = 96849619
p = 18443
q = 49891

d = e2
res = []
with open('/home/yuansheng/yuansheng/test/RSAROLL.txt') as f:
    f.readline()
    f.readline()
    for i in f:
        res.append(chr(pow(int(i), d, n)))
print ("".join(res))

n = 920139713
e1 = 19
x = 920071380
e2 = 96849619
p = 18443
q = 49891

# d = e2
# res = []
# with open('/home/yuansheng/yuansheng/test/RSAROLL.txt') as f:
#     f.readline()
#     f.readline()
#     for i in f:
#         res.append(chr(pow(int(i), d, n)))
# print ("".join(res))


def sushu(n):
    for i in range(2, n / 2, 1):
        if n % i == 0:
            return 0
    return 1


def a():
    for i in range(3, n, 1):
        if n % i == 0:
            if sushu(i) == 1 and sushu(n / i) == 1:
                print i, n / i


# e2 = 2
# flag = 0
# while flag == 0:
#     if (e1 * e2) % x == 1:
#         flag = 1
#     else:
#         e2 = e2 + 1
# print e2


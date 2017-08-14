# coding:utf-8
def panduan(num):
    n = num
    for i in range(6):
        n = n * num
    if n == 4357186184021382204544:
        print num
        return 100

x = 1
y = 1
while x != 100:
    panduan(y)
    y += 1


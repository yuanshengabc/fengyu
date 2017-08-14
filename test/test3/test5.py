# coding=utf-8
array = []
i = 1
for i in range(2 ** 16 - 1):
    array.append(0)


def down(data):
    l = data * 2  # 左边树的号码
    r = data * 2 + 1  # 右边树的号码
    # 超过16层就跳出
    if l >= (2 ** 16 - 1):
        return data
    if array[data]:
        array[data] = 0
        return down(r)
    else:
        array[data] = 1
        return down(l)

for j in xrange(12345):
    if j == 12344:
        print down(1)
    else:
        down(1)

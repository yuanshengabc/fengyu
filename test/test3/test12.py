# coding:utf-8
# n = 12345
# for i in range(100):
#     if n == 0:
#         break
#     for j in range(2, 12345, 1):
#         if n % j == 0:
#             n = n / j
#             print j
#             break
i = 1
j = 1
k = 1
for m in range(12346):
    i = i * 3
    j = j * 5
    k = k * 823
i = (i - 1) / 2
j = (j - 1) / 4
k = (k - 1) / 822
i = i % 9901
j = j % 9901
k = k % 9901
n = i * j * k
n = n % 9901
print n
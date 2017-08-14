for i in range(100):
    j = i
    a = [1] * i
    k = 0
    flag = 0
    while j > 1:
        if k > i - 1:
            k = 0
        if a[k] == 0:
            k += 1
            continue
        else:
            flag += 1
            if flag == 2:
                a[k] = 0
                j -= 1
                flag = 0
            k += 1
    for m in range(i):
        if a[m] == 1:
            print m + 1


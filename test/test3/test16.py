import gmpy2

p, q, e = 473398607161, 4511491, 17
d = gmpy2.invert(e, (p - 1) * (q - 1))
print d

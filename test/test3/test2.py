# coding=utf-8
# 速度爆破
import re
import requests
import hashlib

url = 'http://ctf5.shiyanbar.com/ppc/sd.php/sd.php'

session = requests.session()
f = session.get(url)
zz = r'red">(.*)</div>'
a = re.findall(zz, f.content)[0]  # 获取题目中的sha1值
print a

for x in range(0, 100001):
    sx = str(x)
    xmd5 = hashlib.md5(sx).hexdigest()  # md5加密
    xsha1 = hashlib.sha1(xmd5).hexdigest()  # sha1加密
    if xsha1 == a:  # 比较是否与题目的sha1值相同
        post = session.post(url, {'inputNumber': x})
        print post.content

# -*- coding:utf-8 -*-
import requests

cry = "Rm9yM0re354v5E4FUg5FasDboooo=="
addr = 'soroki.PHP?passwd='
locatie = "http://ctf4.shiyanbar.com/crypto/4/"
pass2 = cry[15:16] + cry[24:25] + cry[0:1] + cry[7:8] + cry[11:12] + cry[13:14] + cry[6:7]
addr = addr[:addr.index('?') + 1] + 'l0vau='
location = locatie + addr + pass2
print location
html = requests.get(location)



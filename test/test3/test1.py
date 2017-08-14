# coding=utf-8
# 百米
from bs4 import BeautifulSoup
import requests

getUrl = 'http://ctf5.shiyanbar.com/jia/index.php'
postUrl = 'http://ctf5.shiyanbar.com/jia/index.php?action=check_pass'
s1 = requests.session()
r = s1.get(getUrl)
soup = BeautifulSoup(r.content, "html.parser")
noSpace = ''
print soup.find('p')
# for i in soup.find('p').contents[1].text:
#     if ord(i) != 32:
#         noSpace += i
# string = noSpace.encode('ascii')
# string = string.replace('x', '*')
# payload = {'pass_key': str(eval(string))}
# r = s1.post(postUrl, payload)
# print r.text

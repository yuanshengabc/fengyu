# encoding=utf-8
import requests
import re

url = 'http://ctf5.shiyanbar.com/ppc/acsii.php'
session = requests.session()
f = session.get(url)
zz = r'red">(.*?)</div>'
string = re.findall(zz, f.content)[0]
string = string.replace(
    '&nbsp;xxx&nbsp;<br />x&nbsp;&nbsp;&nbsp;x<br />x&nbsp;&nbsp;&nbsp;x<br />x&nbsp;&nbsp;&nbsp;x<br '
    '/>&nbsp;xxx&nbsp;<br />',
    '0')
string = string.replace(
    '&nbsp;xx<br>&nbsp;&nbsp;x&nbsp;x&nbsp;&nbsp;<br>&nbsp;&nbsp;x&nbsp;&nbsp;<br>&nbsp;&nbsp;x&nbsp;&nbsp;<br>xxxxx'
    '<br>',
    '1')
string = string.replace(
    '&nbsp;xxx&nbsp;<br />x&nbsp;&nbsp;&nbsp;x&nbsp;<br />&nbsp;&nbsp;xx&nbsp;<br />&nbsp;x&nbsp;&nbsp;&nbsp;<br '
    '/>xxxxx<br />',
    '2')
string = string.replace(
    '&nbsp;xxx&nbsp;<br />x&nbsp;&nbsp;&nbsp;x<br />&nbsp;&nbsp;xx&nbsp;<br />x&nbsp;&nbsp;&nbsp;x<br '
    '/>&nbsp;xxx&nbsp;<br />',
    '8')
string = string.replace(
    '&nbsp;x&nbsp;&nbsp;&nbsp;x<br />x&nbsp;&nbsp;&nbsp;&nbsp;x<br />&nbsp;xxxxx<br '
    '/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;x<br />&nbsp;&nbsp;&nbsp;&nbsp;x<br />',
    '4')
string = string.replace(
    'xxxxx<br />x&nbsp;&nbsp;&nbsp;&nbsp;<br />&nbsp;xxxx<br />&nbsp;&nbsp;&nbsp;&nbsp;x<br />xxxxx<br />', '5')
string = string.replace('<br />', '')
string = string.replace('<br/>', '')
string = string.replace('<br>', '')
print string
post = session.post(url, {'inputNumber': string, 'submit': '提交'})
print post.content

# string = 'XMZFSLDZ'
string = 'NEQETLYDSF'
for i in range(26):
    string1 = ''
    for j in range(len(string)):
        if ord(string[j]) + i > ord('Z'):
            string1 = string1 + chr(ord(string[j]) + i - ord('Z') + ord('A') - 1)
        else:
            string1 = string1 + chr(ord(string[j]) + i)
    print string1.lower()

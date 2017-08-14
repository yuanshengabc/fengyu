#coding=utf-8
import sys
import threading
import base64

if len(sys.argv) != 2:
    print '<usage>:python this.py filename'
    exit()


class Migong(threading.Thread):
    """docstring for ClassName"""
    def __init__(self, scale, begin, end, migong):
        super(Migong, self).__init__()
        self.scale = scale
        self.begin = begin
        self.end = end
        self.migong = migong
        self.path = set()
        self.result = '0'

    def run(self):
        self.result = str(self.go(self.begin))

    def vaule(self, point):
        return self.migong[point[0]-1][point[1]-1] == 'O'

    def valid(self, point):
        if 0 < point[0] <= self.scale and 0 < point[1] <= self.scale:
            return point not in self.path and self.vaule(point)
        return False

    def go(self, begin):
        if not self.vaule(begin):
            return 0
        if begin == self.end:
            return 1
        top = (begin[0]-1, begin[1])
        left = (begin[0], begin[1]-1)
        bottom = (begin[0]+1, begin[1])
        right = (begin[0], begin[1]+1)
        nexts = [i for i in [top, left, bottom, right] if self.valid(i)]
        if not nexts:
            return 0
        self.path.add(begin)
        for i in nexts:
            if self.go(i) == 1:
                return 1
        return 0

with open(sys.argv[1]) as f:
    n = int(f.readline().strip())
    threads = []
    for i in range(n):
        line = None
        while not line:
            line = f.readline().strip()
        scale = int(line)
        begin = tuple(int(x) for x in f.readline().strip().split())
        end = tuple(int(x) for x in f.readline().strip().split())
        migong = []
        for _ in range(scale):
            migong.append(f.readline().strip())
        t = Migong(scale, begin, end, migong)
        t.start()
        threads.append(t)

    for t in threads:
        t.join()

T = ''.join([t.result for t in threads])

flag = ''
for i in range(0, len(T), 8):
    c = T[i:i+8]
    flag += chr(int(c, 2))

print base64.b64decode(flag)
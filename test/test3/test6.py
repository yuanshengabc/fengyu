import Image
# CTF{Living_quiet_in_anoisy_world}
im = Image.open("/home/yuansheng/yuansheng/test/noise.png")
width, height = im.size
pix = im.load()
bgcolor = (255, 255, 255)
im2 = Image.new('RGB', (width, height), bgcolor)
pix2 = im2.load()

count = 0
for x in xrange(0, width):
    for y in xrange(0, height):
        g, b = pix[x, y][1:]
        pix2[g, b] = (0, 0, 0)
        count += 1
        if count == 12000:
            break
    else:
        continue
    break
im2.show()

from itertools import *

# example input:
seedA = 65
seedB = 8921

# real input:
# seedA = 591
# seedB = 393

def gen(seed, factor):
    value = seed
    while True:
        value = (value * factor) % 2147483647
        yield value

def judge(a, b, i):
    return len(list(ifilter(
        lambda (x, y): x & 0xffff == y & 0xffff,
        islice(izip(a, b), i))))

print judge(
        gen(seedA, 16807),
        gen(seedB, 48271),
        40000000)

print judge(
        ifilter(lambda x: x % 4 == 0, gen(seedA, 16807)),
        ifilter(lambda x: x % 8 == 0, gen(seedB, 48271)),
        5000000)

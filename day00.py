input = open('day00_input.txt', 'r').read().strip()
print("input is " + str(len(input)) + " chars long")

floor = 0
pos = 0
firstBasementPos = -1
for c in input:
    pos += 1
    floor += 1 if c == '(' else -1
    # if floor < 0 and firstBasementPos < 0:
        # firstBasementPos = pos

print("final floor: " + str(floor) + " # the correct answer is 280")
print("first basement position: " + str(firstBasementPos) + " # the correct answer is 1797")

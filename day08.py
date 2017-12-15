input = open('day08_input.txt', 'r').read().strip()
print("input is " + str(len(input)) + " chars long")

def parse_instruction(line):
    parts = line.split(" ")
    offset = int(parts[2])
    if parts[1] == "dec":       # convert dec to inc
        offset *= -1
    op = parts[5]
    comp = int(parts[6])
    if op == ">=":              # convert >= to >
        op = ">"
        comp -= 1
    if op == "<=":              # convert <= to <
        op = "<"
        comp += 1
    return (parts[0], offset, parts[4], op, comp)

instructions = map(parse_instruction, input.split("\n"))

print "some instructions: " + str(instructions[0:3])

registers = {}

def get_register(name):
    if name not in registers:
        return 0
    return registers[name]

def increment_register(name, delta):
    val = get_register(name) + delta
    registers[name] = val
    return val

print "get zero: " + str(get_register("fred"))
print "inc six: " + str(increment_register("fred", 6))
print "inc six: " + str(increment_register("fred", 6))
print "get twelve: " + str(get_register("fred"))

maxWorkingVal = None

for (targetRegister, offset, checkRegister, op, comp) in instructions:
    checkValue = get_register(checkRegister)
    if op == ">":
        doIt = checkValue > comp
    elif op == "<":
        doIt = checkValue < comp
    elif op == "==":
        doIt = checkValue == comp
    elif op == "!=":
        doIt = checkValue != comp
    else:
        print "um. '" + op + "' isn't recognized"

    if doIt:
        newVal = increment_register(targetRegister, offset)
        if maxWorkingVal == None or newVal > maxWorkingVal:
            maxWorkingVal = newVal

maxVal = None
for key in registers:
    val = registers[key]
    if maxVal == None or val > maxVal:
        maxVal = val

print "part one: " + str(maxVal)
print "part two: " + str(maxWorkingVal)

input = open('day05_input.txt', 'r').read().strip()
print("input is " + str(len(input)) + " chars long")

instructions = map(int, input.split("\n"))  # remember this trick?
instructionCount = len(instructions)        # to avoid recomputing
count = 0                                   # track the number of iterations
pointer = 0                                 # which instruction we're at
while 0 <= pointer < instructionCount:      # loop while we're at a valid
    offset = instructions[pointer]          # get the jump offset
    instructions[pointer] += 1              # increment it for next time
    pointer += offset                       # move the pointer
    count += 1                              # record the iteration

print "part one: " + str(count)             # the loop's done; print the answer

# all the same, except the incrementing
instructions = map(int, input.split("\n"))
instructionCount = len(instructions)
count = 0
pointer = 0
while 0 <= pointer < instructionCount:
    offset = instructions[pointer]
    instructions[pointer] += -1 if offset >= 3 else 1   # -1/+1 for next time
    pointer += offset
    count += 1

print "part two: " + str(count)

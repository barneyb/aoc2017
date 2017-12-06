input = open('day01_input.txt', 'r').read().strip()
print("input is " + str(len(input)) + " chars long")

#
# Part One
#

numChars = len(input)                   # make a variable for easier reuse
answer = 0                              # this is where we'll add up the answer
lastChar = input[numChars - 1]          # the first character needs to compare against
                                        # the last character, so grab it now for the
                                        # first iteration below
for i in range(0, numChars):            # loop over the input, one character at a time
    c = input[i]                        # get the i-th character
    if c == lastChar:                   # if it's the same, we need to add
        answer += int(c)                # convert to an int and add to answer
    lastChar = c                        # save it for the next iteration

print("part one: " + str(answer))       # this _should_ be the answer!

#
# Part Two
#

answer = 0 # set this back to zero
halfWayAround = numChars / 2            # so we don't have to calculate this every time
for i in range(0, numChars):            # loop over the input, one character at a time
    c = input[i]                        # get the i-th character
    if i < halfWayAround:               # if we're in the first half of the string
        otherIndex = i + halfWayAround  # add half the length to i
    else:                               # if we're in the not-first (second) half of the string
        otherIndex = i - halfWayAround  # subtract half the length from i
    otherChar = input[otherIndex]       # grab _that_ character out of the input
    if c == otherChar:                  # if it's the same, need to add
        answer += int(c)                # convert to an int and add to answer
                                        # no need to save things this time!

print("part two: " + str(answer))       # this _should_ be the answer!

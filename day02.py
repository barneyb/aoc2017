input = open('day02_input.txt', 'r').read().strip()
print("input is " + str(len(input)) + " chars long")

#
# Part One
#

answer = 0                  # where we'll add up each row's checksum
rows = input.splitlines()   # break it into rows
for row in rows:            # loop over the rows
    cells = row.split()     # split the row into cells
    min = int(cells[0])     # pick the first cell (as an int) to get started
    max = min               # same for max!
    for cell in cells:      # loop over all the cells
        n = int(cell)       # convert it to an int
        if n < min:         # see if it's less than min
            min = n         # it is! update min
        if n > max:         # see if it's greater than max
            max = n         # it is! update max
    rowChecksum = max - min # compute the row's checksum
    answer += rowChecksum   # add it onto the answer

print "part one: " + str(answer)

#
# Part Two
#
# hmmm, this would be a _perfect_ place to resuse some of that looping logic
# from part one, since the first bit of it is exactly the same. But that
# sounds hard, so let's just blindly forge ahead and retype it!

answer = 0                                  # reset it!
for row in rows:                            # loop over the rows
    cells = row.split()                     # split the row into cells
    cells = map(int, cells)                 # this is a neat trick for converting every
                                            # cell to a number all at once
    cellCount = len(cells)                  # `len` works for lists too!
    smallNum = 0                            # a bucket to stick this in later
    bigNum = 0                              # a bucket to stick this in later
    for i in range(0, cellCount):           # loop over the cell _indexes_
        cellOne = cells[i]                  # the first cell we'll compare
        for j in range(i + 1, cellCount):   # loop over the rest of the cell _indexes_
            cellTwo = cells[j]              # the second cell we'll compare
            if cellOne > cellTwo:           # first one is bigger, so check "one / two"
                if cellOne % cellTwo == 0:  # the modulo (%) operator does division and gives
                                            # the _remainder_. If the remainder is zero, that
                                            # means the numbers evenly divide, which is what
                                            # we're looking for!
                    smallNum = cellTwo      # save them into those buckets we made earlier
                    bigNum = cellOne
            else:                           # cell two is bigger, so check "two / one"
                if cellTwo % cellOne == 0:  # same modulo check and save as above
                    smallNum = cellOne
                    bigNum = cellTwo

    rowChecksum = bigNum / smallNum         # the checksum is the division (which we now
                                            # know will be even divisible)
    answer += rowChecksum                   # add it onto the answer

print "part two: " + str(answer)

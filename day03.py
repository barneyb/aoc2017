import math

input = 361527 # not going to use a file for this one

#
# Part One
#
# Since the spiral is pretty close to square, we'll assume we need a square with
# area a little bigger than 361527 to draw the spiral. The square root of 361527
# is roughly 601.27..., so a 602x602 square should be enough. For safely, let's
# go with 605x605, and we'll put the origin at (302, 302), which is about the
# middle. Remember that Python starts counting at 0, not 1, so our 605x605
# square is made up of coordinates 0-604 for both x and y (not 1-605).

gridDim = 605 # the dimension of the grid to draw the spiral on
originCoord = 302 # the coordinate of the origin we'll start from

# All well and good, but we may as well just calculate those with the code!

gridDim = int(math.sqrt(input)) + 4 # square root, as int, plus a little safety
originCoord = int(gridDim / 2)      # the middle, as int

print "input: " + str(input)
print "gridDim: " + str(gridDim) # still 605
print "originCoord: " + str(originCoord) # still 302

# so lets build our grid, but we'll use a "factory" function:
def make_grid():
    grid = []
    for x in range(0, gridDim):     # loop over all the x values we want
        grid.insert(x, [])          # set each to an empty array
        for y in range(0, gridDim): # for loop over all the y values we want
            grid[x].insert(y, 0)    # set each to zero (to indicate "no value")
    return grid

grid = make_grid() # actually make a grid for ourselves

print "value at origin: " + str(grid[originCoord][originCoord]) # perfect! a zero!

# lets make a couple helper functions for get-ing and set-ing coordinates in the
# grid, but lets make our jobs easier, and translate them so that the origin is
# at (0, 0), like we're used to. Easy enough to do a little addition here in
# these functions, and make things rather easier on ourselves down the road via
# less mental gymnastics.

def get(x, y):
    return grid[x + originCoord][y + originCoord]

def set(x, y, number):
    grid[x + originCoord][y + originCoord] = number
    return number

# lets test to make sure they're correct. These should do the same thing:
print "value at origin: " + str(grid[originCoord][originCoord])
print "value at origin: " + str(get(0, 0))

set(0, 0, 42) # set it to something else

# these should _still_ do the same thing
print "value at origin: " + str(grid[originCoord][originCoord])
print "value at origin: " + str(get(0, 0))

set(0, 0, 0) # put it back, before we continue on our way

# now we have to actually draw this damn spiral, and we're going to do it pretty
# much exactly how you'd expect:
#  1) start at the origin, facing east, with a counter holding 1
#  2) write the counter in the grid
#  3) increment the counter
#  4) move one step forward
#  5) if the cell to our left is empty, turn 90 degrees left
#  6) go to step 2
#
# From that description, we're going to need a variable for tracking the last
# number we wrote. We'll also need to track our current coordinate and which
# way we're facing. Finally, we'll need to loop until we reach out input number.
# Once we've reached it, we'll use the coordinates we're at to figure out the
# manhattan/taxicab distance back to the origin (by simply adding the x and y
# coordinates together).

# set up our initial state (step 1)
currentX = 0
currentY = 0
facing = "east"
counter = 1

# loop over steps 2-6 until we reach our input
while counter < input: # loop until we reach our input
    set(currentX, currentY, counter) # step 2
    counter += 1 # step 3
    # the last two steps depend on which way we're currently facing, so we'll
    # use a conditional (if/else) to express that
    if facing == "east":
        currentX += 1 # step 4
        if get(currentX, currentY + 1) == 0: # facing east, the square one north of us is the one to check
            facing = "north" # step 5
    elif facing == "north":
        currentY += 1 # step 4
        if get(currentX - 1, currentY) == 0: # facing north, check one west
            facing = "west" # step 5
    elif facing == "west":
        currentX -= 1 # step 4
        if get(currentX, currentY - 1) == 0: # facing west, check one south
            facing = "south" # step 5
    elif facing == "south":
        currentY -= 1 # step 4
        if get(currentX + 1, currentY) == 0: # facing south, check one east
            facing = "east" # step 5

# since we broke out of our loop, we got to our number!

answer = currentX + currentY # calculate the taxicab distance

print "part one: " + str(answer) + " at coords (" + str(currentX) + ", " + str(currentY) + ")"

#
# Part Two
#
# Oof. That was a mess, but this one should be easier. It's basically the same
# as last time, except that our counter won't be changing by one each time,
# it'll be based on the content of the grid. One other thing is that our loop
# needs to keep going while the counter is less than _or equal_ to our input,
# in case we end up writing exactly our input at some point. And we're off...

grid = make_grid() # that function _was_ handy!

# set up the initial state again
currentX = 0
currentY = 0
facing = "east"
counter = 1

# i just copy and pasted the whole loop, changed the `while` condition, and
# tweaked the first couple lines
while counter <= input:
    if currentX == 0 and currentY == 0:
        counter = 1 # this is specified by the problem
    else: # otherwise add up the eight surrounding cells
        counter = (0
            + get(currentX + 1, currentY    ) # east
            + get(currentX + 1, currentY + 1) # north east
            + get(currentX    , currentY + 1) # north
            + get(currentX - 1, currentY + 1) # north west
            + get(currentX - 1, currentY    ) # west
            + get(currentX - 1, currentY - 1) # south west
            + get(currentX    , currentY - 1) # south
            + get(currentX + 1, currentY - 1) # south east
        )
    set(currentX, currentY, counter)
    # everything below here is the same as above
    if facing == "east":
        currentX += 1
        if get(currentX, currentY + 1) == 0:
            facing = "north"
    elif facing == "north":
        currentY += 1
        if get(currentX - 1, currentY) == 0:
            facing = "west"
    elif facing == "west":
        currentX -= 1
        if get(currentX, currentY - 1) == 0:
            facing = "south"
    elif facing == "south":
        currentY -= 1
        if get(currentX + 1, currentY) == 0:
            facing = "east"

# we broke out, which means we wrote a too-big number. which is our answer.
answer = counter

print "part two: " + str(answer) + " at coords (" + str(currentX) + ", " + str(currentY) + ")"
input = open('day06_input.txt', 'r').read().strip()
print("input is " + str(len(input)) + " chars long")

banks = map(int, input.split("\t")) # this is a common thing. :)
bankCount = len(banks)              # to avoid recalculating

# lets make a function that'll do one reallocation, that way we don't have to
# think about it while we're doing the looping stuff. later

def reallocate(banks):
    blocks = max(banks)             # find the most blocks
    i = banks.index(blocks)         # find the first bank with that many blocks
                                    # there could be more than one, but `index`
                                    # will give us the first, which is perfect.
    banks[i] = 0                    # empty that bank
    for j in range(0, blocks):      # loop over the blocks to redistribute
        idx = i + 1 + j             # the index to write to starts one after the
                                    # i-th index, plus the block we're writing
        banks[idx % bankCount] += 1 # use the modulo (%) trick to "wrap" around

# We're going to need a new data structure here: a set. Sets store a - wait for
# it - set of distinct elements, without any order. It's a math term, and the
# "distinct" part is why we're interested. If we have a set of every state the
# memory banks have been in, it'll be easy to figure out when we reach one that
# we've seen before. Sets have two main operations: add and contains (the `in`
# operator, in Python).
#
# While we want to track the history of our memory banks, we can't use the list
# of integers in the `banks` variable directly. Python says we have to use a
# "hashable" value in a set, and lists aren't. Fortunately, strings _are_, and
# they're easy to make with the `str` function, so our history will work with
# `str(banks)` instead of just `banks`.

history = set()                     # construct an empty set for us

while str(banks) not in history:    # loop while we have a new state
    history.add(str(banks))         # add it to the history
    reallocate(banks)

# broke out, so must have found a dupe. length of history is how many cycles. We
# could have used a `count` variable to track the iterations, but no sense in
# doing the work a second time when our `history` set is already doing it.

answer = len(history)

print "part one: " + str(answer)

# We're going to need another new data structure here: a dict. Dicts (short for
# dictionary) store mappings from one type of value to another, just like a
# paper dictionary stores mappings from words to definitions. We're going to use
# it to track not just the states in the history (the source of the mapping, or
# key), but also the position of that state in the history (the destination of
# the mapping, or value). If you think about it a bit, you'll conclude that all
# the keys of a dict must form a set (distinct values), which means we can do
# the same thing we did before using the set.

history = dict()                        # construct an empty set for us

while str(banks) not in history:        # loop while we have a new state
    history[str(banks)] = len(history)  # add it to the history
    reallocate(banks)

# as before we know that the length of the history is the first duplicate state,
# but now we also know the position of the _first_ time we got that same state
# by looking it up in the history. Simple subtraction will tell us how many
# reallocation cycles occurred between them.

answer = len(history) - history[str(banks)]

print "part two: " + str(answer)

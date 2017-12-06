input = open('day04_input.txt', 'r').read().strip()
print("input is " + str(len(input)) + " chars long")

passphrases = input.splitlines()            # split the input into a list of passphrases
count = 0                                   # count how many valid ones there are
for passphrase in passphrases:              # loop over each passphrase
    words = passphrase.split()              # split it into words
    wordCount = len(words)                  # how many words in this phrase
    foundDupe = False                       # track whether we've found a duplicate word
    for i in range(0, wordCount):           # loop over each word, by _index_
        wordOne = words[i]                  # get the word to check
        for j in range(i + 1, wordCount):   # loop over the _rest_ of the words
            wordTwo = words[j]              # get the second word to check
            if wordOne == wordTwo:          # if they're the same ...
                foundDupe = True            # ... record that we found a dupe

    if not foundDupe:                       # if we didn't find any dupes ...
        count += 1                          # ... record that this one is valid

print "part one: " + str(count)

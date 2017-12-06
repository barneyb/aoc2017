
     ,-----.,--.                  ,--. ,---.   ,--.,------.  ,------.
    '  .--./|  | ,---. ,--.,--. ,-|  || o   \  |  ||  .-.  \ |  .---'
    |  |    |  || .-. ||  ||  |' .-. |`..'  |  |  ||  |  \  :|  `--, 
    '  '--'\|  |' '-' ''  ''  '\ `-' | .'  /   |  ||  '--'  /|  `---.
     `-----'`--' `---'  `----'  `---'  `--'    `--'`-------' `------'
    ----------------------------------------------------------------- 


Welcome to your Python AoC project on Cloud9 IDE!

If you've never looked at the tutorial or are interested in learning Python,
go check it out. It's a great hands-on way for learning all about programming
in Python.

* _Learning Python The Hard Way_, online version and videos: 
http://learnpythonthehardway.org/book/

* Full book: http://learnpythonthehardway.org

## Running a script

To run day zero, open the file in `day00.py` and click the green "Run"
button. Note that the first basement position is wrong! Fortunately, it's a
simple fix: just uncomment lines 10 and 11 and hit "Run" again.

Note that day zero is actually 2015's day one. :)

## Doing it for real

Each day will follow the same general pattern:

* go to https://adventofcode.com/ and find the right day
* create a new `dayXX_input.txt` file and copy & paste the puzzle input
* create a new `dayXX.py` file with
    ```
    input = open('dayXX_input.txt', 'r').read().strip()
    print("input is " + str(len(input)) + " chars long")
    ```
* hit the green "Run" button up in the top bar
* check the output down in the bottom pane telling you how long the input is
* actually implement the solution in your script, printing things and hitting
    "Run" to check it as you go

Some of the inputs are so small that you don't really _need_ a separate file
for the input, but most of them are better off using one, and it's never _wrong_
to use one.

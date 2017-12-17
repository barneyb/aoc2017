package event2017;

import java.io.*;

/**
 * @author barneyb
 */
public class Day16 {

    public static void main(String[] args) throws IOException {
        String input = new BufferedReader(new FileReader(new File("input/2017/day16.txt"))).readLine();
        char[] dancers = "abcdefghijklmnop".toCharArray();
        String[] cmds = input.split(",");
        Move[] moves = new Move[cmds.length];
        for (int i = 0; i < cmds.length; i++) {
            moves[i] = parseMove(cmds[i]);
        }

        System.out.println("1 round: " + new String(dance(dancers, moves, 1)));
        int targetRounds = 1_000_000_000;
        int rounds = 100_000;
        long start = System.currentTimeMillis();
        System.out.println(rounds + " rounds: " + new String(dance(dancers, moves, rounds)));
        long elapsed = System.currentTimeMillis() - start;
        System.out.println((1.0 * elapsed / 1000) + " sec for " + rounds + " rounds; expecting " + (1.0 * elapsed / rounds * targetRounds / 1000 / 60 / 60) + " hrs for all " + targetRounds);
        // estimate is 105 hours
    }

    private static char[] dance(char[] dancers, final Move[] moves, final int rounds) {
        for (int r = 0; r < rounds; r++) {
            for (Move move : moves) {
                dancers = move.move(dancers);
            }
        }
        return dancers;
    }

    private static Move parseMove(String cmd) {
        if (cmd.startsWith("s")) {
            return new Spin(cmd);
        } else if (cmd.startsWith("x")) {
            return new Exchange(cmd);
        } else if (cmd.startsWith("p")) {
            return new Partner(cmd);
        }
        throw new RuntimeException("unrecognized command: '" + cmd + "'");
    }

}

interface Move {
    char[] move(char[] dancers);
}

class Spin implements Move {

    private final int n;

    Spin(String cmd) {
        n = Integer.parseInt(cmd.substring(1));
    }

    @Override
    public char[] move(char[] dancers) {
        char[] next = new char[16];
        // copy tail -> head
        System.arraycopy(dancers, 16 - n, next, 0, n);
        // copy head -> tail
        System.arraycopy(dancers, 0, next, n, 16 - n);
        return next;
    }
}

class Exchange implements Move {

    private final int i;
    private final int j;

    Exchange(String cmd) {
        String[] halves = cmd.substring(1).split("/");
        i = Integer.parseInt(halves[0]);
        j = Integer.parseInt(halves[1]);
    }

    @Override
    public char[] move(char[] dancers) {
        char c = dancers[i];
        dancers[i] = dancers[j];
        dancers[j] = c;
        return dancers;
    }

}

class Partner implements Move {

    private final char a;
    private final char b;

    Partner(String cmd) {
        a = cmd.charAt(1);
        b = cmd.charAt(3);
    }

    @Override
    public char[] move(char[] dancers) {
        int i = indexOf(dancers, a);
        int j = indexOf(dancers, b);
        char c = dancers[i];
        dancers[i] = dancers[j];
        dancers[j] = c;
        return dancers;
    }

    private int indexOf(char[] haystack, char needle) {
        for (int i = haystack.length - 1; i >= 0; i--) {
            if (haystack[i] == needle) {
                return i;
            }
        }
        return -1;
    }
}


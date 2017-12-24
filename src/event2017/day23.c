#include <stdio.h>

int main() {
    printf("do it?\n");

    long b = 57;
    long c = b;
    long d = 0;
    long e = 0;
    long f = 0;
    long g = 0;
    long h = 0;

    b *= 100;
    b += 100000;
    c = b;
    c += 17000;

    printf("and we're off %d\n", b);
    do {
        f = 1;
        d = 2;
        do {
            e = 2;
            do {
                g = d;
                g *= e;
                g -= b;
                if (g == 0) {
                    f = 0;
                }
                e += 1;
                g = e;
                g -= b;
            } while (g != 0);
            d += 1;
            g = d;
            g -= b;
        } while (g != 0);
        if (f == 0) {
            h += 1;
        }
        g = b;
        g -= c;
        printf("b=%d, c=%d, d=%d, e=%d, f=%d, g=%d, h=%d\n", b, c, d, e, f, g, h);
        if (g == 0) {
            printf("done (%d)!\n", h);
            return 0;
        }
        b += 17;
    } while (1);
}

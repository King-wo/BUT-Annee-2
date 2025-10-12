// my_cat_sys.c
#include <unistd.h>
#include <stdlib.h>

int main(int argc, char **argv) {
    size_t BS = 1;
    if (argc > 1) BS = (size_t) strtoul(argv[1], NULL, 10);

    char *buf = malloc(BS);
    if (!buf) return 1;

    ssize_t n;
    while ((n = read(STDIN_FILENO, buf, BS)) > 0) {
        ssize_t off = 0;
        while (off < n) {
            ssize_t w = write(STDOUT_FILENO, buf + off, (size_t)(n - off));
            if (w <= 0) { free(buf); return 2; }
            off += w;
        }
    }
    free(buf);
    return (n < 0) ? 3 : 0;
}
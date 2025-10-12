// my_cat_stdio.c
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
    size_t BS = 1;                       // taille de bloc par défaut
    if (argc > 1) BS = (size_t) strtoul(argv[1], NULL, 10);

    // Neutralise le buffering stdio pour stdin & stdout
    setvbuf(stdin,  NULL, _IONBF, 0);
    setvbuf(stdout, NULL, _IONBF, 0);

    char *buf = malloc(BS);
    if (!buf) return 1;

    size_t n;
    while ((n = fread(buf, 1, BS, stdin)) > 0) {
        if (fwrite(buf, 1, n, stdout) != n) { free(buf); return 2; }
    }
    free(buf);
    return ferror(stdin) ? 3 : 0;
}
// my_tail.c
#define _XOPEN_SOURCE 700
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <ctype.h>
#include <string.h>
#include <errno.h>
#include <sys/stat.h>
#include <sys/types.h>

#define DEF_N 10
#define BUF 4096

static int parse_n(const char *s) {
    // accepte "-10" ou "10"
    if (s[0] == '-') s++;
    for (const char *p = s; *p; ++p) if (!isdigit((unsigned char)*p)) return -1;
    long v = strtol(s, NULL, 10);
    return (v > 0 && v <= 100000000) ? (int)v : -1;
}

static int tail_fd(int fd, int n) {
    // Si fd est un vrai fichier régulier, on peut lseek depuis la fin.
    struct stat st;
    if (fstat(fd, &st) == -1) { perror("fstat"); return 1; }

    // Si on ne peut pas lseek (pipe/tty), on lit tout et on garde une fenêtre circulaire.
    if (!S_ISREG(st.st_mode)) {
        // Fallback pipe/tty : anneau de n lignes
        char **ring = calloc(n, sizeof(char*));
        size_t *len = calloc(n, sizeof(size_t));
        if (!ring || !len) { perror("calloc"); return 1; }

        // Lire par blocs et découper en lignes (simple et robuste)
        char buf[BUF];
        size_t cap = 0, used = 0; char *cur = NULL;
        int idx = 0;
        ssize_t r;
        while ((r = read(fd, buf, sizeof buf)) > 0) {
            for (ssize_t i=0;i<r;i++){
                if (used+1 >= cap){ cap = cap? cap*2:256; cur = realloc(cur, cap); }
                cur[used++] = buf[i];
                if (buf[i] == '\n'){
                    char *line = malloc(used+1);
                    if(!line){ perror("malloc"); return 1; }
                    memcpy(line, cur, used); line[used] = 0;
                    free(ring[idx]); ring[idx]=line; len[idx]=used;
                    idx = (idx+1)%n;
                    used = 0;
                }
            }
        }
        if (used){ // dernière ligne sans '\n'
            char *line = malloc(used+1);
            if(!line){ perror("malloc"); return 1; }
            memcpy(line, cur, used); line[used]=0;
            free(ring[idx]); ring[idx]=line; len[idx]=used;
            idx = (idx+1)%n;
            used=0;
        }
        free(cur);

        // imprimer dans l’ordre (depuis idx)
        for (int k=0;k<n;k++){
            int j = (idx+k)%n;
            if (ring[j]) write(STDOUT_FILENO, ring[j], len[j]);
        }
        for (int k=0;k<n;k++) free(ring[k]);
        free(ring); free(len);
        return (r<0)? (perror("read"),1):0;
    }

    // Fichier régulier : back-scan avec lseek
    off_t pos = lseek(fd, 0, SEEK_END);
    if (pos == (off_t)-1) { perror("lseek"); return 1; }
    int lines = 0;
    char buf[BUF];

    // Reculer par blocs depuis la fin jusqu’à trouver n sauts de ligne
    while (pos > 0 && lines <= n){
        size_t chunk = (pos >= (off_t)sizeof buf) ? sizeof buf : (size_t)pos;
        pos = lseek(fd, pos - chunk, SEEK_SET);
        if (pos == (off_t)-1) { perror("lseek"); return 1; }
        ssize_t r = read(fd, buf, chunk);
        if (r < 0) { perror("read"); return 1; }
        for (ssize_t i = r-1; i >= 0; --i) {
            if (buf[i] == '\n') {
                lines++;
                if (lines > n) { pos += i+1; goto FOUND; }
            }
        }
        // pas assez de lignes, continuer à reculer
    }
FOUND:
    // Si on n’a pas trouvé assez de lignes, début du fichier
    if (lines <= n) pos = 0;
    if (lseek(fd, pos, SEEK_SET) == (off_t)-1) { perror("lseek"); return 1; }

    // Copier la fin vers stdout
    ssize_t r;
    while ((r = read(fd, buf, sizeof buf)) > 0) {
        ssize_t off = 0;
        while (off < r) {
            ssize_t w = write(STDOUT_FILENO, buf + off, r - off);
            if (w <= 0) { perror("write"); return 1; }
            off += w;
        }
    }
    if (r < 0) { perror("read"); return 1; }
    return 0;
}

int main(int argc, char *argv[]) {
    int n = DEF_N;
    const char *file = NULL;

    if (argc == 2) {
        // soit "-N", soit "fichier"
        int maybe = parse_n(argv[1]);
        if (maybe > 0) n = maybe;
        else file = argv[1];
    } else if (argc == 3) {
        // "-N fichier"
        int maybe = parse_n(argv[1]);
        if (maybe < 0) { fprintf(stderr, "Usage: %s [-N] [file]\n", argv[0]); return 1; }
        n = maybe; file = argv[2];
    } else if (argc > 3) {
        fprintf(stderr, "Usage: %s [-N] [file]\n", argv[0]);
        return 1;
    }

    int fd;
    if (file) {
        fd = open(file, O_RDONLY);
        if (fd < 0) { perror("open"); return 1; }
    } else {
        fd = STDIN_FILENO; // lecture sur stdin
    }

    int rc = tail_fd(fd, n);
    if (file) close(fd);
    return rc;
}
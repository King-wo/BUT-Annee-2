#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char *argv[]) {
    if (argc < 3) {
        fprintf(stderr, "Usage: %s <repertoire> <fichier>\n", argv[0]);
        exit(1);
    }

    char *rep = argv[1];
    char *fichier = argv[2];

    pid_t pid;

    // 1. Création du répertoire avec mkdir
    pid = fork();
    if (pid == 0) {
        execlp("mkdir", "mkdir", rep, NULL);
        perror("execlp mkdir");
        exit(1);
    }
    wait(NULL); // le père attend que mkdir finisse

    // 2. Création du fichier avec touch rep/fichier
    pid = fork();
    if (pid == 0) {
        char chemin[256];
        snprintf(chemin, sizeof(chemin), "%s/%s", rep, fichier);
        execlp("touch", "touch", chemin, NULL);
        perror("execlp touch");
        exit(1);
    }
    wait(NULL);

    printf("Répertoire '%s' et fichier '%s/%s' créés avec succès.\n", rep, rep, fichier);
    return 0;
}

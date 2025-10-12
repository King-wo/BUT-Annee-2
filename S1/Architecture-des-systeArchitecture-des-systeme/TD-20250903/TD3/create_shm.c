#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <string.h>

#define TAILLE 1024

int main() {
    key_t key = ftok("shmfile", 65); // Génère une clé unique
    int shmid = shmget(key, TAILLE, 0666 | IPC_CREAT);

    if (shmid == -1) {
        perror("Erreur shmget");
        exit(1);
    }

    printf("Segment mémoire partagée créé avec id = %d\n", shmid);
    return 0;
}
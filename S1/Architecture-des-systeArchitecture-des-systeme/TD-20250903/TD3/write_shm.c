#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <string.h>

#define TAILLE 1024

int main() {
    key_t key = ftok("shmfile", 65);
    int shmid = shmget(key, TAILLE, 0666);

    char *str = (char*) shmat(shmid, NULL, 0);

    printf("Entrez un message à écrire dans la SHM : ");
    fgets(str, TAILLE, stdin);

    printf("Message écrit en SHM : %s\n", str);

    shmdt(str); // détacher
    return 0;
}
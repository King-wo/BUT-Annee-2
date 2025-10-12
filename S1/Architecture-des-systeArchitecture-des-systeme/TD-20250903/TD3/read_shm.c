#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/shm.h>

#define TAILLE 1024

int main() {
    key_t key = ftok("shmfile", 65);
    int shmid = shmget(key, TAILLE, 0666);

    char *str = (char*) shmat(shmid, NULL, 0);

    printf("Message lu depuis la SHM : %s\n", str);

    shmdt(str); // détacher
    shmctl(shmid, IPC_RMID, NULL); // supprimer la mémoire partagée
    return 0;
}
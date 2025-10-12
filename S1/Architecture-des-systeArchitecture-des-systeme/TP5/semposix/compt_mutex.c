#include <pthread.h>
#include <semaphore.h>
#include <fcntl.h>    
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h> 
#define N 1000000

int compteur = 0;
sem_t *mutex = NULL;
void* incr(void* a) {
    for (int i = 0; i < N; i++) {
        sem_wait(mutex);
        int tmp = compteur; 
        tmp = tmp + 1;        
        compteur = tmp;       
        sem_post(mutex);
    }
    return NULL;
}
int main(void) {
    pthread_t tid1, tid2;
    char name[64];
    snprintf(name, sizeof(name), "/compteur_mutex_%d", getpid());
    sem_unlink(name);
    mutex = sem_open(name, O_CREAT | O_EXCL, 0644, 1);
    if (mutex == SEM_FAILED) {
        perror("sem_open");
        return 1;
    }
    if (pthread_create(&tid1, NULL, incr, NULL) != 0) { perror("pthread_create tid1"); return 1; }
    if (pthread_create(&tid2, NULL, incr, NULL) != 0) { perror("pthread_create tid2"); return 1; }
    pthread_join(tid1, NULL);
    pthread_join(tid2, NULL);
    if (compteur < 2 * N)
        printf("\nBOOM! compteur = [%d], devrait être %d\n", compteur, 2 * N);
    else
        printf("\nOK! compteur = [%d]\n", compteur);
    sem_close(mutex);
    sem_unlink(name);
    return 0;
}

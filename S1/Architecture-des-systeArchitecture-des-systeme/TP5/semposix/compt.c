#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <stdlib.h>
#define N 1000000
int compteur = 0;
void * incr(void * a)
{
    int i, tmp;
    for(i = 0; i < N; i++)
    {
    tmp = compteur; tmp = tmp+1; compteur = tmp;
    }
    pthread_exit(NULL);
}
int main(int argc, char * argv[]){

    pthread_t tid1, tid2;
    if(pthread_create(&tid1, NULL, incr, NULL))
    {
        printf("\n ERREUR création thread 1");
        exit(1);
    }
    if(pthread_create(&tid2, NULL, incr, NULL))
    {
        printf("\n ERROR création thread 2");
        exit(1);
    }
    if(pthread_join(tid1, NULL)) {
        printf("\n ERREUR thread 1 ");
        exit(1);
    }
    if(pthread_join(tid2, NULL)) {
        printf("\n ERREUR thread 2");
        exit(1);
    }
    if ( compteur < 2 * N)
        printf("\n BOOM! compteur = [%d], devrait être %d\n", compteur, 2*N);
    else
        printf("\n OK! compteur = [%d]\n", compteur);
        pthread_exit(NULL);
    return 0 ;
}
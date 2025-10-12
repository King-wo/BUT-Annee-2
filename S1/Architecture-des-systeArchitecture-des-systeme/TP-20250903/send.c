#include <stdio.h>   
#include <stdlib.h>   
#include <string.h>   
#include <fcntl.h>    
#include <sys/stat.h> 
#include <mqueue.h>   

#define QUEUE_NAME "/ma_file" 

int main() {
    mqd_t mq = mq_open(QUEUE_NAME, O_CREAT | O_WRONLY, 0666, NULL);
    if (mq == (mqd_t)-1) {
        perror("Erreur ouverture de la file de messages");
        exit(EXIT_FAILURE);
    }

    char message[64];
    for (int i = 1; i <= 10; ++i) {
        snprintf(message, sizeof(message), "Message %d", i);
        if (mq_send(mq, message, strlen(message) + 1, 0) == -1) {
            perror("Erreur envoi message");
            mq_close(mq);
            exit(EXIT_FAILURE);
        }
    }
    mq_close(mq);
    return 0;
}

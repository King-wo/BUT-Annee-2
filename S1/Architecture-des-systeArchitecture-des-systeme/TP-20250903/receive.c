#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>  
#include <sys/stat.h>
#include <mqueue.h>

#define QUEUE_NAME "/ma_file"

int main() {
    mqd_t mq = mq_open(QUEUE_NAME, O_CREAT | O_RDONLY, 0666, NULL);
    if (mq == (mqd_t)-1) {
        perror("Erreur ouverture de la file de messages");
        exit(EXIT_FAILURE);
    }
    char buffer[64];
    unsigned int prio;
    for (int i = 1; i <= 10; ++i) {
        ssize_t bytes_read = mq_receive(mq, buffer, sizeof(buffer), &prio);
        if (bytes_read >= 0) {
            buffer[bytes_read] = '\0';
            printf("Reçu: %s (prio=%u)\n", buffer, prio);
        } else {
            perror("Erreur réception message");
            mq_close(mq);
            mq_unlink(QUEUE_NAME);
            exit(EXIT_FAILURE);
        }
    }
    mq_close(mq);
    mq_unlink(QUEUE_NAME);
    return 0;
}

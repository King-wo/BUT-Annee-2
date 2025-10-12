#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <mqueue.h>
#include <fcntl.h>

#define QUEUE_NAME "/ma_file_posix"
#define MSG_COUNT 10
#define MAX_SIZE 256

int main() {
    mqd_t mq;
    char buffer[MAX_SIZE];

    mq = mq_open(QUEUE_NAME, O_RDONLY);
    if (mq == -1) {
        perror("mq_open");
        exit(EXIT_FAILURE);
    }

    for (int i = 0; i < MSG_COUNT; i++) {
        ssize_t bytes = mq_receive(mq, buffer, MAX_SIZE, NULL);
        if (bytes >= 0) {
            buffer[bytes] = '\0';
            printf("Reçu : %s\n", buffer);
        } else {
            perror("mq_receive");
            exit(EXIT_FAILURE);
        }
    }

    mq_close(mq);
    mq_unlink(QUEUE_NAME);
    return 0;
}

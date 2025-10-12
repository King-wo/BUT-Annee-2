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
    struct mq_attr attr;

    attr.mq_flags = 0;
    attr.mq_maxmsg = MSG_COUNT;
    attr.mq_msgsize = MAX_SIZE;
    attr.mq_curmsgs = 0;

    mq = mq_open(QUEUE_NAME, O_CREAT | O_WRONLY, 0644, &attr);
    if (mq == -1) {
        perror("mq_open");
        exit(EXIT_FAILURE);
    }

    char message[MAX_SIZE];
    for (int i = 1; i <= MSG_COUNT; i++) {
        printf("Entrez le message %d : ", i);
        fflush(stdout);
        if (fgets(message, MAX_SIZE, stdin) == NULL) {
            perror("fgets");
            exit(EXIT_FAILURE);
        }

        // Supprimer le saut de ligne
        size_t len = strlen(message);
        if (len > 0 && message[len - 1] == '\n') {
            message[len - 1] = '\0';
        }

        if (mq_send(mq, message, strlen(message) + 1, 0) == -1) {
            perror("mq_send");
            exit(EXIT_FAILURE);
        }

        printf("→ Message %d envoyé : \"%s\"\n", i, message);
    }

    mq_close(mq);
    return 0;
}

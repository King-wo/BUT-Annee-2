#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <sys/stat.h>

#define FIFO_PATH "/tmp/fifo"

int main(void) {
    if (mkfifo(FIFO_PATH, 0666) == -1 && errno != EEXIST) {
        perror("mkfifo");
        return 1;
    }
    int fd = open(FIFO_PATH, O_WRONLY);
    if (fd == -1) {
        perror("open ecriture FIFO");
        return 1;
    }
    char buf[512];
    ssize_t n;
    while ((n = read(STDIN_FILENO, buf, sizeof(buf))) > 0) {
        if (write(fd, buf, n) == -1) {
            perror("write FIFO");
            break;
        }
    }
    if (n == -1) perror("read stdin");

    close(fd);
    return 0;
}

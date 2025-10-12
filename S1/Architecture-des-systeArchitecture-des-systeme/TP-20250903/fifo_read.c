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
    int fd = open(FIFO_PATH, O_RDONLY);
    if (fd == -1) {
        perror("open lecture FIFO");
        return 1;
    }
    char buf[512];
    ssize_t n;
    while ((n = read(fd, buf, sizeof(buf))) > 0) {
        if (write(STDOUT_FILENO, buf, n) == -1) {
            perror("write stdout");
            break;
        }
    }
    if (n == -1) perror("read FIFO");

    close(fd);
    return 0;
}

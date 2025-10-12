#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>

#define FIFO_PATH "/tmp/fifo_channel"
#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <fichier à lire>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    if (mkfifo(FIFO_PATH, 0666) == -1) {
        perror("mkfifo");
    }

    int file_fd = open(argv[1], O_RDONLY);
    if (file_fd == -1) {
        perror("open fichier");
        exit(EXIT_FAILURE);
    }

    int fifo_fd = open(FIFO_PATH, O_WRONLY);
    if (fifo_fd == -1) {
        perror("open FIFO");
        close(file_fd);
        exit(EXIT_FAILURE);
    }

    char buffer[BUFFER_SIZE];
    ssize_t bytes_read;
    while ((bytes_read = read(file_fd, buffer, BUFFER_SIZE)) > 0) {
        write(fifo_fd, buffer, bytes_read);
    }

    close(file_fd);
    close(fifo_fd);
    return 0;
}

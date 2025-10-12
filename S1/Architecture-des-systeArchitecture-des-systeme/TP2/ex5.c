#define _XOPEN_SOURCE 700
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>

int open_out(const char *name) {
    int fd = open(name, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (fd == -1) {
        perror(name);
        exit(EXIT_FAILURE);
    }
    return fd;
}

int main(void) {
    printf("[P1] PID=%d (parent), PPID=%d\n", getpid(), getppid());
    pid_t p2 = fork();
    if (p2 == -1) {
        perror("fork P2");
        exit(EXIT_FAILURE);
    }

    if (p2 == 0) {
        int f = open_out("f.txt");                 
        dup2(f, STDOUT_FILENO);                    
        close(f);
        printf("[P2] stdout redirigé vers f.txt (PID=%d, PPID=%d)\n", getpid(), getppid());
        _exit(0);
    }

    pid_t p3 = fork();
    if (p3 == -1) {
        perror("fork P3");
        exit(EXIT_FAILURE);
    }
    if (p3 == 0) {
        int g = open_out("g.txt");                 
        int h = open_out("h.txt");                 
        dup2(g, STDOUT_FILENO);
        dup2(h, STDERR_FILENO);
        close(g);
        close(h);
        printf("[P3] stdout redirigé vers g.txt (PID=%d, PPID=%d)\n", getpid(), getppid());
        fprintf(stderr, "[P3] stderr redirigé vers h.txt (PID=%d, PPID=%d)\n", getpid(), getppid());
        _exit(0);
    }
    int status;
    while (wait(&status) > 0);  

    printf("[P1] stdout sur le terminal (PID=%d)\n", getpid());
    fprintf(stderr, "[P1] stderr sur le terminal (PID=%d)\n", getpid());

    return 0;
}

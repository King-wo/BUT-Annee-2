#define _XOPEN_SOURCE 700
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s cmd1 [cmd2 ...]\n", argv[0]);
        return EXIT_FAILURE;
    }
    printf("[parent] PID=%d, PPID=%d\n", getpid(), getppid());

    for (int i = 1; i < argc; ++i) {
        pid_t pid = fork();
        if (pid == -1) { perror("fork"); continue; }

        if (pid == 0) {
            printf("[fils-pre-exec] CMD=%s PID=%d PPID=%d\n", argv[i], getpid(), getppid());
            execlp(argv[i], argv[i], (char*)NULL);
            perror("execlp");            
            _exit(127);
        }
    }
    int status;
    pid_t wpid;
    while ((wpid = wait(&status)) > 0) {
        if (WIFEXITED(status)) {
            printf("[wait] PID=%d exit=%d\n", wpid, WEXITSTATUS(status));
        } else if (WIFSIGNALED(status)) {
            printf("[wait] PID=%d signal=%d\n", wpid, WTERMSIG(status));
        }
    }
    return 0;
}

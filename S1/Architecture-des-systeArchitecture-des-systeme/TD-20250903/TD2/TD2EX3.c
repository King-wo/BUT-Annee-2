#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <cmd1> [cmd2 ...]\n", argv[0]);
        return 1;
    }
    for (int i = 1; i < argc; i++) {
        pid_t pid = fork();
        if (pid < 0) {
            perror("fork");
            return 2;
        } else if (pid == 0) { 
            printf("[child] cmd=%s  PID=%d  PPID=%d\n", argv[i], getpid(), getppid());
            execlp(argv[i], argv[i], (char *)NULL); 
            perror("execlp");                         
            _exit(127);
        } else {
            printf("[parent] lancé '%s' avec PID=%d (parent=%d)\n",
                   argv[i], pid, getpid());
        }
    }
    int status;
    pid_t wpid;
    while ((wpid = wait(&status)) > 0) {
        if (WIFEXITED(status)) {
            printf("[reap] PID=%d terminé code=%d\n", wpid, WEXITSTATUS(status));
        } else if (WIFSIGNALED(status)) {
            printf("[reap] PID=%d tué par signal=%d\n", wpid, WTERMSIG(status));
        }
    }
    return 0;
}

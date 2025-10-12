#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>

int main(void) {
    pid_t pid = fork();
    if (pid < 0) { perror("fork"); return 1; }

    if (pid == 0) {
        printf("[FILS] PID=%d, PPID=%d -> je termine\n", getpid(), getppid());
        _exit(0); 
    }
    printf("[PERE] PID=%d, fils=%d -> je dors sans wait()\n", getpid(), pid);
    sleep(30); 
    return 0;
}

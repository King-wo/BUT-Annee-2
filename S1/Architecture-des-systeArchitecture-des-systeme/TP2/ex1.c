#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int carre(int x) {          
    return x * x;
}

int main(void) {
    printf("[fonction] 5^2 = %d\n", carre(5));

    pid_t pid = fork();
    if (pid == -1) { perror("fork"); exit(EXIT_FAILURE); }

    if (pid == 0) {           
        exit(42);            
    } else {                  
        int status;
        waitpid(pid, &status, 0);
        if (WIFEXITED(status)) {
            printf("[processus] code de retour du fils = %d\n", WEXITSTATUS(status));
        }
    }
    return 0;
}

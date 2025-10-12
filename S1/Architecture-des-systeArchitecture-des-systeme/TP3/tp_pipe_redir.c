#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <string.h>

static void die(const char *msg) {
    perror(msg);
    exit(EXIT_FAILURE);
}
int main(int argc, char *argv[]) {
    if (argc < 8) {
        fprintf(stderr,
            "usage : %s cmd1 opt1 arg1 cmd2 opt2 arg2 file\n"
            "ex    : %s ls -l /etc grep -e m out.txt\n",
            argv[0], argv[0]);
        return EXIT_FAILURE;
    }
    const char *cmd1 = argv[1];
    const char *opt1 = argv[2];
    const char *arg1 = argv[3];
    const char *cmd2 = argv[4];
    const char *opt2 = argv[5];
    const char *arg2 = argv[6];
    const char *outfile = argv[7];
    int p[2];
    if (pipe(p) == -1) die("pipe");

    pid_t pid = fork();
    if (pid < 0) die("fork");

    if (pid == 0) {
        if (close(p[1]) == -1) die("close p[1] (child)");
        if (dup2(p[0], STDIN_FILENO) == -1) die("dup2 pipe->stdin");
        if (close(p[0]) == -1) die("close p[0] after dup2");
        int fd = open(outfile, O_WRONLY | O_CREAT | O_TRUNC, 0644);
        if (fd == -1) die("open outfile");
        if (dup2(fd, STDOUT_FILENO) == -1) die("dup2 file->stdout");
        if (close(fd) == -1) die("close fd after dup2");
        execlp(cmd2, cmd2, opt2, arg2, (char*)NULL);
        fprintf(stderr, "execlp(%s) failed: %s\n", cmd2, strerror(errno));
        _exit(127);
    }
    if (close(p[0]) == -1) die("close p[0] (parent)");
    if (dup2(p[1], STDOUT_FILENO) == -1) die("dup2 pipe->stdout");
    if (close(p[1]) == -1) die("close p[1] after dup2");
    execlp(cmd1, cmd1, opt1, arg1, (char*)NULL);
    fprintf(stderr, "execlp(%s) failed: %s\n", cmd1, strerror(errno));
    _exit(127);
}

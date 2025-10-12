#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
int main()
{
    int n = 100;
    printf("Bonjour > ");
    n *= 2;
    switch (fork())
    {
    case 1:
        perror("fork");
        break;
    case 0:
        sleep(1);
        printf("dans le fils, adresse de n= %p\n", &n);
        n += 10;
        sleep(1);
        printf("n= %d\n", n);
        break;
    default:
        printf("dans le père, adresse de n= %p\n", &n);
        n += 10;
        sleep(3);
        printf("n= %d\n", n);
    }
}
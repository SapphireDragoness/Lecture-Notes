#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

int main(int argc, char **argv) {
    if(argc != 3) {
        printf("Utilizzo: ./shell <numero processi da eseguire> <nome file>");
        exit(1);
    }
    if(argv[1] <= 0) {
        printf("Inserire intero maggiore di 0");
        exit(1);
    }

    int n = atoi(argv[1]);
    int f = creat(argv[2], S_IRUSR | S_IWUSR);
    char s[64];
    int status;
    pid_t pid;

    printf("> ");   
    while(fgets(s, sizeof s, stdin) != NULL) {
        if(s[strlen(s)-1] == '\n') {
            s[strlen(s)-1] = 0;
        }
        if(strcmp(s, "exit") == 0) {
            close(f);
            exit(0);
        }
        for(int i = 0; i < n; i++) {
            pid = fork();
            if(pid == 0) {
                dup2(f, 1);
                execlp(s, s, (char *)0);
                exit(0);
            }
            if(n % 2 == 0) {
                waitpid(pid, &status, 0);
            }
        }
        printf("> ");
    }
}
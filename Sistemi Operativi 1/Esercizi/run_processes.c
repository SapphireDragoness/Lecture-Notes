#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int arg, char **argv) {
    char line[64];
    pid_t pid;
    int status;
    FILE *fp = fopen(argv[1], "w");
    char *n, *s;

    printf("> ");
    while(fgets(line, sizeof line, stdin) != NULL) {
        n = strtok(line, " ");
        s = strtok(NULL, " ");
        for(int i = 0; i < atoi(n); i++) {
            if(fork() == 0) {
                fprintf(fp, "ID processo: %ld\n", getpid());
                execlp(s, s, (char*)0);
                exit(0);
            }
        }
        wait(NULL);
        printf("> ");
    }
    
    fclose(fp);
    exit(0);
}
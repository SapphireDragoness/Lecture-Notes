#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

int main(int argc, char **argv) {
    char line[128];
    int seconds;
    char message[64];
    pid_t pid;

    while(1) {
        printf("Allarme > ");
        if(fgets(line, sizeof line, stdin) == NULL) {
            exit(0);
        }
        if(strlen(line) <= 1) {
            continue;
        }
        if(sscanf(line, "%d %64[^\n]", &seconds, message) < 2) {
            printf("Comando sconosciuto\n");
        }
        else {
            pid = fork();
            if(pid == -1) {
                printf("Errore fork");
                exit(1);
            }
            if(pid == 0) {
                sleep(seconds);
                printf("(%d) %s", seconds, message);
                exit(0);
            }
        }
    }
}
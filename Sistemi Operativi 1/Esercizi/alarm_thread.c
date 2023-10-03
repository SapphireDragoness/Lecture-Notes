#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

typedef struct alarm {
    int seconds;
    char message[64];
} alarm_t;

void *tbody(void *arg){
    alarm_t *alarm;
    alarm = (alarm_t*) arg;
    sleep(alarm->seconds);
    printf("Messaggio: %s\n", alarm->message);
}

int main(int argc, char *argv[]){
    char line[128];
    alarm_t *alarm;
    pthread_t t;
    while(1){
        printf("Allarme >");
        if(fgets(line, sizeof(line), stdin)==NULL) exit(0);
        if(strlen(line)<=1) continue;
        alarm=(alarm_t *) malloc(sizeof(alarm_t));
        if(sscanf(line, "%d %64[^\n]", &(alarm->seconds), &(alarm->message))<2) {
            fprintf(stderr, "Comando sconosciuto\n");
        } else {
            pthread_create(&t, NULL, tbody, (void *)alarm);
        }
    }
}
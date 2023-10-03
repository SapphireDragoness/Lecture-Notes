#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>
#include <unistd.h>
#include <pthread.h>

sem_t sem, mutex;
int arrived = 0;
int n = 5;

void wall() {
    printf("Sono il thread %ld e sto arrivando alla barriera... \n", pthread_self());
    sem_wait(&mutex);
    arrived++;
    if(arrived < n) {
        sem_post(&mutex);
        sem_wait(&sem);
        printf("Mi sono bloccato sulla barriera.\n");
    }
    else {
        for(int i = 1; i < n; i++) {
            sem_post(&sem);
        }
        sem_post(&mutex);
    }
}

int main(int argc, char **argv) {
    pthread_t t[n];

    sem_init(&sem, 1, n+1);
    sem_init(&mutex, 1, 1);

    for(int i = 0; i < n; i++) {
        pthread_create(&t[i], NULL, (void*)wall, NULL);
    }
    for(int i = 0; i < n; i++) {
        pthread_join(t[i], NULL);
    }
}
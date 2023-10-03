#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>
#include <pthread.h>

sem_t mutex;

void *thr_fn1(void *arg) {
    printf("Part A of thread 1\n");
    sem_post(&mutex);
    printf("Part B of thread 1\n");
    return ((void*)0);
}

void *thr_fn2(void *arg) {
    printf("Part A of thread 2\n");
    sem_wait(&mutex);
    printf("Part B of thread 2 should be last!\n");
    return ((void*)0);
}

int main(void) {
    pthread_t t1, t2;

    sem_init(&mutex, 0, 0);

    pthread_create(&t1, NULL, thr_fn1, NULL);
    pthread_create(&t2, NULL, thr_fn2, NULL);

    pthread_join(t1, NULL);
    pthread_join(t2, NULL);

    exit(0);
}
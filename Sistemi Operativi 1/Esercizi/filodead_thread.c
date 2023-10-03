#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <wait.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <semaphore.h>
#include <pthread.h>

#define NFILOSOFI 5
#define LEFT (i+NFILOSOFI-1)%NFILOSOFI
#define RIGHT (i+1)%NFILOSOFI

pthread_t t[NFILOSOFI];
sem_t *sem;
sem_t mutex;
int shm_fd1, shm_fd2;

char *stato;

void print() {
    for(int i = 0; i < NFILOSOFI; i++) {
        putchar(stato[i]);
    }
    printf("\n");
}

void test(int i) {
    if(stato[i] == 'H' && stato[LEFT] != 'E' && stato[RIGHT] != 'E') {
        stato[i]='E';
        printf("FILOSOFO %d MANGIA:       ",i);
        print();
        sem_post(&sem[i]);
    }
}

void take_forks(int i) {
    sem_wait(&mutex);
    printf("FILOSOFO %d E' AFFAMATO:       ",i);
    stato[i]='H';
    print();
    test(i);
    sem_post(&mutex);
    sem_wait(&sem[i]);
}

void put_forks(int i) {
    sem_wait(&mutex);
    stato[i]='T';
    printf("FILOSOFO %d SMETTE:       ",i);
    print();
    test(LEFT);
    test(RIGHT);
    sem_post(&mutex);
}

void proc(int i) {
    int j, n;
    int m;

    m = 50000000 + 30000000 * i;

    for(n = 0; n < 10; n++) {
        take_forks(i);
        for(j=0;j<m;j++);
        put_forks(i);
    }
}

int main(void) {
    /* inizializzazione memoria condivisa */
    shm_fd1 = shm_open("/myshm1", O_CREAT|O_RDWR,0600);
    if (shm_fd1 == -1) perror("Creazione memoria condivisa: stato");

    ftruncate(shm_fd1, NFILOSOFI*sizeof(sem_t));
    sem = mmap(0, NFILOSOFI*sizeof(sem_t),PROT_READ|PROT_WRITE,MAP_SHARED, shm_fd1,0);
    if (sem == MAP_FAILED) perror("Creazione memoria condivisa: stato");

    shm_fd2 = shm_open("/myshm2", O_CREAT|O_RDWR,0600);
    if (shm_fd2 == -1) perror("Creazione memoria condivisa: semafori");
    ftruncate(shm_fd2, NFILOSOFI*sizeof(char));
    stato = mmap(0, NFILOSOFI*sizeof(char),PROT_READ|PROT_WRITE,MAP_SHARED, shm_fd2,0);
    if (stato == MAP_FAILED) perror("Creazione memoria condivisa: semafori");

    /* inizializzazione mutex */
    sem_init(&mutex, 0, 1);

    /* inizializzazione array di semafori */
    for(int i = 0; i < NFILOSOFI; i++) {
        sem_init(&sem[i], 0, 1);
        stato[i] = 'T';
    }

    /* creazione thread */
    for(int i = 0; i < NFILOSOFI; i++) {
        pthread_create(&t[i], NULL, (void*)proc, (void*)i);
    }

    for(int i = 0; i < NFILOSOFI; i++) {
        pthread_join(t[i], NULL);
    }
}
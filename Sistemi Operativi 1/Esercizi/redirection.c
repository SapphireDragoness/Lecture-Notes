#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

int main(int argc, char **argv) {
    int fd = open(argv[1], O_RDWR);
    close(1);
    dup(fd);
    printf("I'm a duck.\n");
    close(fd);
}

#include <iostream>
#include <cstring>

#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>

int main(int, char **)
{
    std::cout << "Trying to read shared memory\n";

    int address = 0xd7a6;
    int size = 100;

    int shmid = shmget(address, size, 0666 | IPC_CREAT);

    if (shmid == -1) {
        std::cerr << "Failed to open shared memory segment: " << std::strerror(errno) << "\n";
    }

    char *buffer = (char *) shmat(shmid, 0, 0);

    for (int b = 0; b < 8; ++b) {
        std::cout << (int)buffer[b] << "\n";
    }
    
    std::cout << "success\n";

    return 0;
}
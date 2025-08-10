/**
 * Author: Luiz Carlos d'Oleron
 * doleron@gmail.com
 * 
 * DISCLAIMER: This software is provided “AS IS”, without warranty of any kind, express or implied, 
 * including but not limited to the warranties of merchantability, fitness for a particular purpose, 
 * and noninfringement. In no event shall the author or copyright holders be liable for any claim, 
 * damages, or other liability, whether in an action of contract, 
 * tort, or otherwise, arising from, out of, or in connection with the software or the use or other dealings in the software.
 * 
 */

#include <iostream>
#include <cstring>

#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>

int main(int, char **)
{
    std::cout << "Trying to write to shared memory\n";

    int address = 0xd7a6;
    int size = 100;

    int shmid = shmget(address, size, 0666 | IPC_CREAT);

    if (shmid == -1) {
        std::cerr << "Failed to open shared memory segment: " << std::strerror(errno) << "\n";
    }

    char *buffer = (char *) shmat(shmid, 0, 0);

    for (int b = 0; b < 8; ++b) {
        buffer[b] = b;
    }

    std::cout << "success\n";

    return 0;
}
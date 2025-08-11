# java-shared-memory
Simple API to read and write on shared memory segments with Java on Linux.

Note1: So far, it runs only on <3 Linux <3
Note2: Yes, there are plans to add support to Windows and OSx

## Example: basic reading/write

It is easy to read or write on shared memory.

For example, you can use the following code to write:

```java
import java.nio.ByteBuffer;
import com.improvess.shared.connector.Connector;

public class Example {

    public static void main(String[] args) {
        Connector connector = new Connector();

        int shm_id = connector.initialize_shared_buffer(0xd7a6, 8);

        ByteBuffer buffer = connector.get_shared_buffer(shm_id);

        // Writing 8 bytes on shared segment
        for (int i = 0; i < 8; ++i) {
            buffer.put(i, (byte) i);
        }
    }
}
```

and use another process to read the values.

For example, you can use this C++ program to read the values you just wrote:

```c++
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>

int main(int, char **)
{
    int shmid = shmget(0xd7a6, 8, 0666 | IPC_CREAT);
    if (shmid == -1) {
        std::cerr << "Failed to open shared memory segment: " << std::strerror(errno) << "\n";
    }

    char *buffer = (char *) shmat(shmid, 0, 0);
    for (int b = 0; b < 8; ++b) {
        std::cout << (int)buffer[b] << "\n";
    }
    return 0;
}
```

## Example: inter process synchronization with semaphores

With this API, you can also use semaphores to avoid inter processing concurrence issues:

```java
import java.nio.ByteBuffer;
import com.improvess.shared.connector.Connector;

Connector connector = new Connector();

int shm_id = connector.initialize_shared_buffer(0xd7a6, 8);

ByteBuffer buffer = connector.get_shared_buffer(shm_id);

// initializing and locking semaphore
int sem_id = connector.initialize_shared_semaphore(sem_key, 1, Connector.IPC_CREAT | 0666);
connector.semaphoro_lock(sem_id);

// Writing 8 bytes on shared segment as in the previous example
for (int i = 0; i < 8; ++i) {
    buffer.put(i, (byte) i);
}

// unlocking and releasing semaphore
connector.semaphoro_unlock(sem_id);
connector.release_shared_semaphore(sem_id);
```

## Building 

First, set JAVA_HOME environment variable:
```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
```
Replacing `/usr/lib/jvm/java-11-openjdk-amd64/` accordingly with your settings.

Clone the repo:
```bash
git clone https://github.com/improvess/java-shared-memory.git
```

Then, call Maven:
```bash
cd java-shared-memory
mvn clean package
```

This library uses JNI. The native shared library `libjava_shared_memory_lib.so` is automatically shipped into generated jar.

The maven build also copy the shared library into the `target` folder. Thus, there is two ways to load the shared library:

- You can copy it somewhere and use `-Djava.library.path=your-folder` to load the libray or
- You can use `com.improvess.shared.connector.LoadLib.load()` method to automatically load the library

## Running the Hello World example

```bash
cd java-shared-memory
java -Djava.library.path=target -cp target com.improvess.shared.examples.CheckConnector 2
```

This command should print:

```bash
Checking Connector:
Hello from connector!
Connector say hello!
Connector say hello again!
```

## Running the Concurrent Access Examples

### Using no locking

```bash
java -Djava.library.path=target -cp target com.improvess.shared.examples.ConcurrentAccessWithLock
```
It outpus something like:
```bash
Checking Concurrent access using no locking:
decrementTask get -26
incrementTask get 1
End
```

The values may vary due to the randomization of concurrent processing.

### Using locking

```bash
java -Djava.library.path=target -cp target com.improvess.shared.examples.ConcurrentAccessWithLock
```
It should outputs:

No locking:

```bash
Checking Concurrent access using semaphores:
- incrementTask: Semaphore created
- incrementTask: Semaphore locked
decrementTask: Semaphore created
- incrementTask: get 100
- incrementTask: Semaphore unlocked
- incrementTask: Semaphore released
decrementTask: Semaphore locked
decrementTask: get 0
decrementTask: Semaphore unlocked
decrementTask: Semaphore released
End
```
Or, depending on which thread starts first:

```bash
Checking Concurrent access using semaphores:
decrementTask: Semaphore created
- incrementTask: Semaphore created
decrementTask: Semaphore locked
decrementTask: get -100
decrementTask: Semaphore unlocked
decrementTask: Semaphore released
- incrementTask: Semaphore locked
- incrementTask: get 0
- incrementTask: Semaphore unlocked
- incrementTask: Semaphore released
End
```

### Automatically loading the shared library

In the previous examples, we used `-Djava.library.path=target` to indicate which folder to look for the shared library.

In the next example, we load the library through java code:

```bash
java -cp target com.improvess.shared.examples.LoadingLibrary
```
This example works by calling:

```java
static {
    try {
        LoadLib.load();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

```
which automatically loads the shared library.
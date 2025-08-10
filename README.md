# java-shared-memory
Read and Write shared memory segments with Java on Linux.

So far, it runs only on <3 Linux <3

Yes, there are plans to add support to Windows and OSx

## Example

It is easy to read or write on shared memory:

```java
import java.nio.ByteBuffer;
import com.improvess.shared.connector.Connector;

Connector connector = new Connector();

int shm_id = connector.initialize_shared_buffer(0xd7a6, 8);

ByteBuffer buffer = connector.get_shared_buffer(shm_id);

// Writing 8 bytes on shared segment
for (int i = 0; i < 8; ++i) {
    buffer.put(i, (byte) i);
}
```

You can also use semaphores to avoid inter processing concurrence issues:

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

Maven generates in the `target` folder both the jar and the shared library `libjava_shared_memory_lib.so`

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
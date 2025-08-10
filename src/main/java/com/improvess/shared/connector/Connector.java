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

package com.improvess.shared.connector;

import java.nio.ByteBuffer;

public class Connector {

    public static final int IPC_CREAT = 01000;
    public static final int IPC_EXCL = 02000;
    public static final int IPC_NOWAIT = 04000;

    // The following methods exemplify the use of JNI
    public native void say_hello();

    public native void say_hello_again(int t);

    public native int get_count();

    // The following methods handle shared memory segments

    public native int initialize_shared_buffer(int address, long capacity);

    public native ByteBuffer get_shared_buffer(int shm_fd);

    public native boolean release_shared_buffer(int shm_fd);

    // The following methods handle shared memory semaphores
    public native int initialize_shared_semaphore(int key, int nsems, int semflg);

    public native boolean semaphoro_lock(int semid);

    public native boolean semaphoro_unlock(int semid);

    public native boolean release_shared_semaphore(int shm_fd);

}

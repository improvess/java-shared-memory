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

package com.improvess.shared.examples;

import java.nio.ByteBuffer;

import com.improvess.shared.connector.Connector;

public class ConcurrentAccessNoLock {

    static {
        System.loadLibrary("java_shared_memory_lib");
    }

    public static void main(String[] args) {

        System.out.println("Checking Concurrent access using no locking:");

        Connector connector = new Connector();

        int shm_id = connector.initialize_shared_buffer(0xd7a6, 100);

        ByteBuffer buffer = connector.get_shared_buffer(shm_id);

        Runnable incrementTask = () -> {
            for (int i = 0; i < 100; ++i) {
                buffer.put(0, (byte) (buffer.get(0) + 1));
                Thread.yield();
            }
            System.out.println("incrementTask get " + buffer.get(0));
        };

        Runnable decrementTask = () -> {
            for (int i = 100; i > 0; --i) {
                buffer.put(0, (byte) (buffer.get(0) - 1));
                Thread.yield();
            }
            System.out.println("decrementTask get " + buffer.get(0));
        };

        try {

            Thread incrementThread = new Thread(incrementTask);
            Thread decrementThread = new Thread(decrementTask);

            incrementThread.start();
            decrementThread.start();

            incrementThread.join();
            decrementThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            connector.release_shared_buffer(shm_id);

            System.out.println("End");
        }

    }

}

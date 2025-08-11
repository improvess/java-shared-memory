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

import java.io.IOException;

import com.improvess.shared.connector.Connector;
import com.improvess.shared.connector.LoadLib;

public class LoadingLibrary {

    static {
        try {
            LoadLib.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Checking Connector by loading library on the fly:");
        Connector connector = new Connector();
        connector.say_hello();
        int times = 0;
        if (args.length > 0) {
            times = Integer.parseInt(args[0]);
        }
        connector.say_hello_again(times);

        System.out.println("Count: " + connector.get_count());

        System.out.println("End");

    }
    
}

/*==========================================================================
 |Implementing a Hash function
 |
 |Author : Henry King
 |Language: Java
 |
 |Sample input file with 100 words for hashing is included
 |
 |Class: COP3503 - CS II Summer 2021
 |Instructor: McAlpin
 |Submission Date: 08/06/2021
 +==========================================================================*/

import java.util.*;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.io.File;

public class UCFxram_HASH {

    // main method
    public static void main(String[] args) throws FileNotFoundException {

        // Scan input into array for easier reading
        File input = new File("100words.txt");
        // File input = new File(args[0]);
        Scanner sc = new Scanner(input);
        // create array to hold strings
        ArrayList<String> inputdatalist = new ArrayList<String>();
        // puts strings in array
        while (sc.hasNext()) {
            inputdatalist.add(sc.next());
        }
        // close the scanner
        sc.close();

        int hashValue = 0;

        System.out.println("\n\n=================");
        // sort through each string
        for (int i = 0; i < inputdatalist.size(); i++) {
            // run the string through UCFxram
            hashValue = UCFxram(inputdatalist.get(i), inputdatalist.get(i).length());
            // print the string and its hash value
            System.out.format("%10x:%s\n", hashValue, inputdatalist.get(i));
        }
        // done with file
        System.out.println("Input file processed\n=================\n\n");

    }

    public static int UCFxram(String input_string, int string_length) {

        int randVal1 = 0xbcde98ef; // arbitrary value
        int randVal2 = 0x7890face;
        int hashVal = 0xfa01bc96; // start seed value
        int roundedEnd = string_length & 0xfffffffc; // array d gets 4 byte blocks
        int tempData = 0;

        byte[] d = null;

        // assigns values to d and catches exception
        try {
            d = input_string.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            System.out.println(" NON ASCII chars in input ");
            e.printStackTrace();
            return 0;
        }

        for (int i = 0; i < roundedEnd; i += 4) {

            tempData = (d[i] & 0xff) | ((d[i + 1] & 0xff) << 8) | ((d[i + 2] & 0xFF) << 16) | ((d[i + 3] << 24));
            // multiply
            tempData = tempData * randVal1;
            // rotate left 12 bits
            tempData = Integer.rotateLeft(tempData, 12);
            // another multiply
            tempData = tempData * randVal2;
            hashVal = hashVal ^ tempData;
            // rotate left 13 bits
            hashVal = Integer.rotateLeft(hashVal, 13);
            hashVal = hashVal * 5 + 0x46b6456e;
            // now collect orphan input characters
        }

        tempData = 0;
        int len = string_length;

        if ((len & 0x03) == 3) {
            tempData = (d[roundedEnd + 2] & 0xFF) << 16;
            len = len - 1;
        }

        if ((len & 0x03) == 2) {
            tempData |= (d[roundedEnd + 1] & 0xff) << 8;
            len = len - 1;
        }

        if ((len & 0x03) == 1) {
            tempData |= (d[roundedEnd] & 0xff);
            tempData = tempData * randVal1; // multiply
            tempData = Integer.rotateLeft(tempData, 14); // rotate left 14 bits
            tempData = tempData * randVal2; // another multiply
            hashVal = hashVal ^ tempData;
        }

        hashVal = hashVal ^ len; // XOR
        hashVal = hashVal & 0xb6acbe58; // AND

        hashVal = hashVal ^ hashVal >>> 13;

        hashVal = hashVal * 0x53EA2B2C; // another arbitrary value

        hashVal = hashVal ^ hashVal >>> 16;

        return hashVal; // return the 32 bit int hash

    }

}
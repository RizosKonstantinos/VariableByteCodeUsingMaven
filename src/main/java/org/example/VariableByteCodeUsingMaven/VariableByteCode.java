package org.example.VariableByteCodeUsingMaven;

import javax.swing.*;
import java.util.ArrayList;

public class VariableByteCode {

    static final int block = 8;

    public static void main(String[] args) {

        while(true) {
            int inputInteger = getUserInput();
            if(inputInteger == -1) {
                System.exit(-1);
            }
            StringBuilder inputToBinary = new StringBuilder();

            // Converts the integer input into binary
            inputToBinary.append(Integer.toBinaryString(inputInteger));

            // Reverse the inputToBinary so that the process will go from the end towards the beginning
            StringBuilder inputBinaryReverse = inputToBinary.reverse();

            // This list will store all the 8bit blocks
            ArrayList<StringBuilder> bytes = new ArrayList<>();

            computeResult(inputToBinary, inputBinaryReverse, bytes);

            printResult(inputInteger, bytes);
        }
    }


    public static int getUserInput(){
        String input;

        while(true) {
            input = JOptionPane.showInputDialog("Enter an integer between 0 and 2^32");

            // When user closes the pop-up window or presses the "Cancel" button then the input is either null or empty and the program terminates
            if(input == null || input.isEmpty()){
                System.exit(-1);
            }
            if(numberIsNegative(input)) {
                if(handlingExceptionMethod()) {
                    continue;
                }
                else {
                    input = "-1";
                    break;
                }
            }
            try {
                return Integer.parseInt(input);
            } catch(NumberFormatException nfe) {
                if (!handlingExceptionMethod()) {
                    input = "-1";
                    break;
                }
            }
        }
        return Integer.parseInt(input);
    }


    public static boolean numberIsNegative(String input) {
        return input.charAt(0)=='-';
    }


    /*
     *   JOptionPane.showConfirmDialog() returns an int depending to users reaction
     *
     *   User pressed "YES": returns 0
     *   User pressed "NO": returns 1
     *   User pressed closed the pop-up window: returns -1
     */
    public static boolean handlingExceptionMethod() {
        int selectedValue = JOptionPane.showConfirmDialog(null,"Oops! Seems like you didn't enter a valid number.\n\nWould you like to try again?\n\n"
                ,"Error",JOptionPane.YES_NO_OPTION);

        return selectedValue != 1 && selectedValue != -1;
    }



    public static void computeResult(StringBuilder inputBinary, StringBuilder inputBinaryReverse, ArrayList<StringBuilder> bytes ){
        int index = 0;
        double numOfBytes = (int)Math.ceil((double)inputBinary.length()/(block - 1)); // Calculates how many bytes will be needed
        int remainder = inputBinaryReverse.length(); // This variable is used to calculate how many bits are left for the process to end
        boolean isTheFirstByte = true;


        for(int i=0; i<numOfBytes; i++){
            // if only 1 byte is needed
            if(numOfBytes == 1){
                bytes.add(vbCodeForOneByte(inputBinary, block, inputBinaryReverse));
            }
            else{

                // for the 1st byte of the n byte binary
                if(i == 0){
                    bytes.add(vbCodeForNBytes(inputBinaryReverse, index, isTheFirstByte));
                    isTheFirstByte = false;
                    index += 7;
                }
                else{
                    // for the rest of the bytes
                    if(remainder >= 7) {
                        bytes.add(vbCodeForNBytes(inputBinaryReverse, index, false));
                        index += 7;
                    }
                    else{
                        bytes.add(vbCodeForTheLastByte(inputBinaryReverse, index, remainder));
                    }
                }
                remainder -= 7;
            }
        }
    }


    /*
    Method for processing an input that is less than 1 byte
    This method appends "0" at the end until it reaches the 8th bit where it appends "1".
    Note that the input is reverse processed.
     */
    public static StringBuilder vbCodeForOneByte(StringBuilder input, int block,StringBuilder inputReverse){
        int diff = block - input.length();  // calculates how many zero-bits must be added to the binary input so that its length is 7
        for(int j=0; j<diff-1; j++){
            inputReverse.append(0);
        }
        inputReverse.append(1);

        return inputReverse;
    }


    /*
    Method for processing an input that is more than one byte
    This method is being called for the first N - 1 bytes
     */
    public static StringBuilder vbCodeForNBytes(StringBuilder inputReverse, int index, boolean firstByte){
        StringBuilder sb;
        sb = stringAppend(inputReverse,index,7);
        if(firstByte){
            sb.append(1);
        }
        else{
            sb.append(0);
        }
        return sb;
    }

    // Method for processing the last byte of the input
    public static StringBuilder vbCodeForTheLastByte(StringBuilder inputBinaryReverse, int index, int remainder) {
        StringBuilder subString = stringAppend(inputBinaryReverse, index, remainder);
        int diff = block - subString.length();
        for(int i=0; i<diff; i++){
            subString.append(0);
        }
        return subString;
    }



    // Method that appends the bits to a string
    public static StringBuilder stringAppend(StringBuilder inputReverse, int index, int flag){
        StringBuilder sb = new StringBuilder();
        for (int i=index; i<index+flag; i++) {
            sb.append(inputReverse.charAt(i));
        }
        return sb;
    }


    // The ArrayList holds the bytes in reverse order, so this method reverse them back to normal
    public static String stringFormatting(ArrayList<StringBuilder> bytes) {
        StringBuilder result = new StringBuilder();
        for (StringBuilder aByte : bytes) {
            result.append(aByte);
            result.append(" ");
        }
        return result.reverse().toString();
    }


    public static void printResult(int inputInteger, ArrayList<StringBuilder> bytes) {
        String resultString = stringFormatting(bytes);
        JOptionPane.showMessageDialog(null,"Decimal: "+ inputInteger +"\nBinary: "+Integer.toBinaryString(inputInteger)+"\n\nVB-code: "+resultString+"\n\n");
    }

}

package org.example.variableByteCodeUsingMaven;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class VariableByteCodeTest {
    
    @Test
    public void testingUserGivesNegativeNumber() {
        assertTrue(VariableByteCode.numberIsNegative("-5"));
    }
	
    @Test
    public void testingUserGivesPositiveNumber() {
        assertFalse(VariableByteCode.numberIsNegative("123"));
    }
	
    @Test
    public void testingTheInputNumberIsLessThanOneByte() {
            int block = 8;
            int input = 5;

            StringBuilder inputToBinary = new StringBuilder();
            StringBuilder inputReverse = new StringBuilder();
            inputToBinary.append(Integer.toBinaryString(input));
            inputReverse = inputToBinary.reverse();

            assertEquals("10000101", VariableByteCode.vbCodeForOneByte(inputToBinary, block, inputReverse).reverse().toString());

    }
	
    @Test
    public void testingTheInputNumberIsTwoBytes() {
            int index = 0;
            int input = 129;

            StringBuilder inputToBinary = new StringBuilder();
            StringBuilder inputReverse = new StringBuilder();
            inputToBinary.append(Integer.toBinaryString(input));
            inputReverse = inputToBinary.reverse();

            //First Byte
            assertEquals("10000001",VariableByteCode.vbCodeForNBytes(inputReverse, index, true).reverse().toString());

            //Second Byte
            index +=7;
            int remainder = inputReverse.length()-7;
            assertEquals("00000001",VariableByteCode.vbCodeForTheLastByte(inputReverse, index, remainder).reverse().toString());	
    }
		
	
    @Test
    public void testingRandomInput() {
            ArrayList<StringBuilder> expectedList = new ArrayList<StringBuilder>();
            StringBuilder m = new StringBuilder(); 
            m.append("000000010111111011010100").reverse(); //32596 in decimal. The string must be reversed because the computedList() method computes strings in reverse
            expectedList.add(m);

            int input = 32596;
            StringBuilder inputToBinary = new StringBuilder();
            inputToBinary.append(Integer.toBinaryString(input));

            StringBuilder inputReverse = inputToBinary.reverse();

            ArrayList<StringBuilder> computedList = new ArrayList<StringBuilder>();

            VariableByteCode.computeResult(inputToBinary, inputReverse, computedList);
            StringBuilder s = new StringBuilder();
            for(StringBuilder a: computedList){
                s.append(a);
            }
            assertEquals(expectedList.get(0).toString(), s.toString());
            
    }
	
    @Test
    public void testingTheStringFormattingMethodForOneByte() {
            ArrayList<StringBuilder> expectedList = new ArrayList<StringBuilder>();
            StringBuilder sb = new StringBuilder();
            sb.append("00000001");
            expectedList.add(sb);

            // There must be an empty space at the beginning
            assertEquals(" 10000000",VariableByteCode.stringFormatting(expectedList));
    }
	
    @Test
    public void testingTheStringFormattingMethodForTwoBytes() {
            ArrayList<StringBuilder> expectedList = new ArrayList<StringBuilder>();
            StringBuilder sb = new StringBuilder();
            sb.append("00000000 00000001");
            expectedList.add(sb);

            // There must be an empty space at the beginning and between the bytes
            assertEquals(" 10000000 00000000",VariableByteCode.stringFormatting(expectedList));
    }
}

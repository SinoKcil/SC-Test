package P1;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

/*
test strategy:给出若干会导致方法抛出不同结果的含有矩阵的文件，包括是幻方、不是幻方、输入错误，判断各种情况的执行情况。
 */
public class MagicSquareTest {
    @Test
    public void isLegalMagicSquareTest(){
        try
        {
            assertEquals(true, MagicSquare.isLegalMagicSquare("./P1/txt/1.txt"));
            assertEquals(false,MagicSquare.isLegalMagicSquare("./P1/txt/2.txt"));
        }catch (Exception e){}
        assertThrows(IOException.class,()->MagicSquare.isLegalMagicSquare("./P1/txt/3.txt"));
        assertThrows(IOException.class,()->MagicSquare.isLegalMagicSquare("./P1/txt/4.txt"));
        assertThrows(IOException.class,()->MagicSquare.isLegalMagicSquare("./P1/txt/5.txt"));
        assertThrows(FileNotFoundException.class,()->MagicSquare.isLegalMagicSquare("./P1/txt/7.txt"));
    }
}
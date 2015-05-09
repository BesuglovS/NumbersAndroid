package ru.besuglovs.games.numbers.ToolBox;

/**
 * Created by Besug on 04.05.2015.
 */
public class NumsNum
{
    public byte[] Digits;

    public int getNum() {
        return Digits[0] * 10000 + Digits[1] * 1000 + Digits[2] * 100 + Digits[3] * 10 + Digits[4];
    }
    public void setNum(int value) {
        Digits = NumToDigits(value);
    }

    public String getNumString()
    {
        return "" + Digits[0] + Digits[1] + Digits[2] + Digits[3] + Digits[4];
    }

    public NumsNum(int num)
    {
        Digits = NumToDigits(num);
    }

    public static byte[] NumToDigits(int num)
    {
        byte[] result = new byte[5];
        result[0] = (byte)(num / 10000);
        result[1] = (byte)(num / 1000 - result[0] * 10);
        result[2] = (byte)(num / 100 - result[0] * 100 - result[1] * 10);
        result[3] = (byte)(num / 10 - result[0] * 1000 - result[1] * 100 - result[2] * 10);
        result[4] = (byte)(num % 10);

        return result;
    }
}
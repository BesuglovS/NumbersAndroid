package ru.besuglovs.games.numbers.ToolBox;

import java.util.ArrayList;

/**
 * Created by Besug on 04.05.2015.
 */
public class ToolBox
{
    public static boolean IsCorrect(int num)
    {
        if ((num < 1234) || (num > 98765))
        {
            return false;
        }

        byte[] digits = NumsNum.NumToDigits(num);

        return (digits[0] != digits[1]) &&
                (digits[0] != digits[2]) &&
                (digits[0] != digits[3]) &&
                (digits[0] != digits[4]) &&
                (digits[1] != digits[2]) &&
                (digits[1] != digits[3]) &&
                (digits[1] != digits[4]) &&
                (digits[2] != digits[3]) &&
                (digits[2] != digits[4]) &&
                (digits[3] != digits[4]);
    }

    public static NumsCount CalculateCount(NumsNum move, NumsNum idealNumber)
    {
        NumsCount result = new NumsCount();

        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                if (move.Digits[i] == idealNumber.Digits[j])
                {
                    result.First++;
                    if (i == j)
                    {
                        result.Second++;
                    }
                }
            }
        }

        return result;
    }
}
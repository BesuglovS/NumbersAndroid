package ru.besuglovs.games.numbers.ToolBox;

/**
 * Created by Besug on 04.05.2015.
 */
public class NumsMove
{
    public NumsNum Guess;
    public NumsCount Count;

    public NumsMove(NumsNum guess) {
        Guess = guess;
    }

    public NumsMove(NumsNum guess, NumsCount count) {
        Guess = guess;
        Count = count;
    }
}

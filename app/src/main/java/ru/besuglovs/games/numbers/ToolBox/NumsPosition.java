package ru.besuglovs.games.numbers.ToolBox;

import java.util.ArrayList;

/**
 * Created by Besug on 04.05.2015.
 */
public class NumsPosition
{
    public NumsNum FirstPlayerNumber;
    public ArrayList<NumsMove> FirstPlayerMoves;
    public NumsNum SecondPlayerNumber;
    public ArrayList<NumsMove> SecondPlayerMoves;

    public byte PlayerToMakeMove;


    public NumsPosition()
    {
        FirstPlayerNumber = new NumsNum(0);
        FirstPlayerMoves = new ArrayList<NumsMove>();
        SecondPlayerNumber = new NumsNum(0);
        SecondPlayerMoves = new ArrayList<NumsMove>();
        PlayerToMakeMove = 1;
    }

    public NumsPosition(NumsNum firstPlayerNum, NumsNum secondPlayerNum)
    {
        FirstPlayerNumber = firstPlayerNum;
        FirstPlayerMoves = new ArrayList<NumsMove>();
        SecondPlayerNumber = secondPlayerNum;
        SecondPlayerMoves = new ArrayList<NumsMove>();
        PlayerToMakeMove = 1;
    }
}

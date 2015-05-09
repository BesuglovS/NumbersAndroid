package ru.besuglovs.games.numbers.engines;

import java.util.ArrayList;

import ru.besuglovs.games.numbers.ToolBox.NumsMove;
import ru.besuglovs.games.numbers.ToolBox.NumsNum;

/**
 * Created by Besug on 04.05.2015.
 */

public interface INumsEngineInterface
{
    ArrayList<NumsNum> AnalysePosition(ArrayList<NumsMove> moves);

    NumsNum GetMove(ArrayList<NumsMove> moves);

    NumsNum CreateOwnNum();
}

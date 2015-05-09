package ru.besuglovs.games.numbers.engines;

import java.util.ArrayList;
import java.util.Random;

import ru.besuglovs.games.numbers.ToolBox.NumsCount;
import ru.besuglovs.games.numbers.ToolBox.NumsMove;
import ru.besuglovs.games.numbers.ToolBox.NumsNum;
import ru.besuglovs.games.numbers.ToolBox.ToolBox;

/**
 * Created by Besug on 04.05.2015.
 */
public class DefaultEngine implements INumsEngineInterface {
    @Override
    public ArrayList<NumsNum> AnalysePosition(ArrayList<NumsMove> moves) {
        ArrayList<NumsNum> result = new ArrayList<NumsNum>();

        for (int i = 1234; i < 98766; i++)
        {
            if (ToolBox.IsCorrect(i))
            {
                boolean possible = true;

                NumsNum iNum = new NumsNum(i);

                for (int j = 0; j < moves.size(); j++)
                {
                    NumsCount count = ToolBox.CalculateCount(new NumsNum(moves.get(j).Guess.getNum()), iNum);
                    if ((count.First != moves.get(j).Count.First) || (count.Second != moves.get(j).Count.Second))
                    {
                        possible = false;
                        break;
                    }
                }

                if (possible)
                {
                    result.add(new NumsNum(i));
                }
            }
        }

        return result;
    }

    @Override
    public NumsNum GetMove(ArrayList<NumsMove> moves) {
        ArrayList<NumsNum> possibleMoves = AnalysePosition(moves);

        Random r = new Random();
        int index = r.nextInt(possibleMoves.size());

        return possibleMoves.get(index);
    }

    @Override
    public NumsNum CreateOwnNum() {
        ArrayList<NumsNum> numsList = new ArrayList<NumsNum>();

        for (int i = 1234; i < 98766; i++)
        {
            if (ToolBox.IsCorrect(i))
            {
                numsList.add(new NumsNum(i));
            }
        }

        Random r = new Random();
        int index = r.nextInt(numsList.size());

        return numsList.get(index);
    }
}

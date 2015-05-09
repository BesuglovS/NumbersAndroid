package ru.besuglovs.games.numbers;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.besuglovs.games.numbers.RecyclerView.RecyclerViewAnalysisAdapter;
import ru.besuglovs.games.numbers.ToolBox.NumsCount;
import ru.besuglovs.games.numbers.ToolBox.NumsMove;
import ru.besuglovs.games.numbers.ToolBox.NumsNum;
import ru.besuglovs.games.numbers.engines.DefaultEngine;


public class AnalysisActivity extends Activity {

    private DefaultEngine engine;
    private NumsMove currentMove;
    private NumsNum possibleMove;
    private ArrayList<NumsMove> moves;

    private RecyclerView mRecyclerView;
    private RecyclerViewAnalysisAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ToggleButton button1, button2, button3, button4, button5, count1, count2;
    private int selectedButton;
    private ToggleButton digit1, digit2, digit3, digit4, digit5, digit6, digit7, digit8, digit9, digit0;
    private Button addMoveButton;
    private Button possibleMoveButton;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gson = new Gson();
        String json = gson.toJson(moves);
        outState.putString("moves", json);

        if (currentMove != null) {
            outState.putInt("currentMoveGuess", currentMove.Guess.getNum());
            outState.putInt("currentMoveCountFirst", currentMove.Count.First);
            outState.putInt("currentMoveCountSecond", currentMove.Count.Second);
        }

        if (possibleMove != null)
        {
            outState.putInt("possibleMove", possibleMove.getNum());
        }

        outState.putInt("selectedButton", selectedButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        engine = new DefaultEngine();

        button1 = (ToggleButton) findViewById(R.id.button1);
        button2 = (ToggleButton) findViewById(R.id.button2);
        button3 = (ToggleButton) findViewById(R.id.button3);
        button4 = (ToggleButton) findViewById(R.id.button4);
        button5 = (ToggleButton) findViewById(R.id.button5);

        count1 = (ToggleButton) findViewById(R.id.count1);
        count2 = (ToggleButton) findViewById(R.id.count2);

        digit1 = (ToggleButton) findViewById(R.id.digit1);
        digit2 = (ToggleButton) findViewById(R.id.digit2);
        digit3 = (ToggleButton) findViewById(R.id.digit3);
        digit4 = (ToggleButton) findViewById(R.id.digit4);
        digit5 = (ToggleButton) findViewById(R.id.digit5);
        digit6 = (ToggleButton) findViewById(R.id.digit6);
        digit7 = (ToggleButton) findViewById(R.id.digit7);
        digit8 = (ToggleButton) findViewById(R.id.digit8);
        digit9 = (ToggleButton) findViewById(R.id.digit9);
        digit0 = (ToggleButton) findViewById(R.id.digit0);

        addMoveButton = (Button) findViewById(R.id.addMove);


        if (savedInstanceState != null)
        {
            selectedButton = savedInstanceState.getInt("selectedButton");

            String movesString = savedInstanceState.getString("moves");

            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<NumsMove>>() {}.getType();
            moves = gson.fromJson(movesString, collectionType);

            SetRecycleViewMoves();

            int cmgInt = savedInstanceState.getInt("currentMoveGuess");
            if (cmgInt != 0) {
                NumsNum cmg = new NumsNum(cmgInt);
                NumsCount cmc = new NumsCount(
                        (byte)savedInstanceState.getInt("currentMoveCountFirst"),
                        (byte)savedInstanceState.getInt("currentMoveCountSecond"));
                currentMove = new NumsMove(cmg, cmc);
            }

            SetButtons(selectedButton);

            int pmgInt = savedInstanceState.getInt("possibleMove");
            if (pmgInt != 0)
            {
                possibleMove = new NumsNum(pmgInt);
                RefreshAnalysis(possibleMove);
            }
            else {
                RefreshAnalysis(null);
            }
        }
        else
        {
            NumsNum guess = new NumsNum(12345);
            NumsCount count = new NumsCount(((byte) 0), ((byte) 0));
            currentMove = new NumsMove(guess, count);

            moves = new ArrayList<NumsMove>();

            SetRecycleViewMoves();

            selectedButton = 1;
            SetButtons(selectedButton);

            RefreshAnalysis(null);
        }



        addMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moves.add(currentMove);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mAdapter.moves.size() - 1);

                NumsNum guess = new NumsNum(currentMove.Guess.getNum());
                NumsCount count = new NumsCount(currentMove.Count.First, currentMove.Count.Second);
                currentMove = new NumsMove(guess, count);

                RefreshAnalysis(null);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button1.isChecked()) {
                    selectedButton = 1;
                    SetButtons(1);
                } else {
                    button1.setChecked(true);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button2.isChecked()) {
                    selectedButton = 2;
                    SetButtons(2);
                }
                else
                {
                    button2.setChecked(true);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button3.isChecked()) {
                    selectedButton = 3;
                    SetButtons(3);
                }
                else
                {
                    button3.setChecked(true);
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button4.isChecked()) {
                    selectedButton = 4;
                    SetButtons(4);
                }
                else
                {
                    button4.setChecked(true);
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button5.isChecked()) {
                    selectedButton = 5;
                    SetButtons(5);
                }
                else
                {
                    button5.setChecked(true);
                }
            }
        });

        count1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count1.isChecked()) {
                    selectedButton = 6;
                    SetButtons(6);
                }
                else
                {
                    count1.setChecked(true);
                }
            }
        });

        count2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count2.isChecked()) {
                    selectedButton = 7;
                    SetButtons(7);
                }
                else
                {
                    count2.setChecked(true);
                }
            }
        });

        digit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit1.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 1;
                            break;
                        case 6:
                            currentMove.Count.First = 1;
                            break;
                        case 7:
                            currentMove.Count.Second = 1;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit1.setChecked(true);
                }
            }
        });

        digit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit2.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 2;
                            break;
                        case 6:
                            currentMove.Count.First = 2;
                            break;
                        case 7:
                            currentMove.Count.Second = 2;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit2.setChecked(true);
                }
            }
        });

        digit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit3.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 3;
                            break;
                        case 6:
                            currentMove.Count.First = 3;
                            break;
                        case 7:
                            currentMove.Count.Second = 3;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit3.setChecked(true);
                }
            }
        });

        digit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit4.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 4;
                            break;
                        case 6:
                            currentMove.Count.First = 4;
                            break;
                        case 7:
                            currentMove.Count.Second = 4;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit4.setChecked(true);
                }
            }
        });

        digit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit5.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 5;
                            break;
                        case 6:
                            currentMove.Count.First = 5;
                            break;
                        case 7:
                            currentMove.Count.Second = 5;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit5.setChecked(true);
                }
            }
        });

        digit6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit6.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 6;
                            break;
                        case 6:
                            currentMove.Count.First = 6;
                            break;
                        case 7:
                            currentMove.Count.Second = 6;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit6.setChecked(true);
                }
            }
        });

        digit7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit7.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 7;
                            break;
                        case 6:
                            currentMove.Count.First = 7;
                            break;
                        case 7:
                            currentMove.Count.Second = 7;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit7.setChecked(true);
                }
            }
        });

        digit8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit8.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 8;
                            break;
                        case 6:
                            currentMove.Count.First = 8;
                            break;
                        case 7:
                            currentMove.Count.Second = 8;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit8.setChecked(true);
                }
            }
        });

        digit9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit9.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 9;
                            break;
                        case 6:
                            currentMove.Count.First = 9;
                            break;
                        case 7:
                            currentMove.Count.Second = 9;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit9.setChecked(true);
                }
            }
        });

        digit0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit0.isChecked()) {
                    switch(selectedButton)
                    {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            currentMove.Guess.Digits[selectedButton - 1] = 0;
                            break;
                        case 6:
                            currentMove.Count.First = 0;
                            break;
                        case 7:
                            currentMove.Count.Second = 0;
                            break;
                    }

                    SetButtons(selectedButton);
                }
                else
                {
                    digit0.setChecked(true);
                }
            }
        });

    }

    private void SetRecycleViewMoves() {
        mRecyclerView = (RecyclerView) findViewById(R.id.movesList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAnalysisAdapter(moves, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void RefreshAnalysis(NumsNum pMove) {
        TextView resultView = (TextView) findViewById(R.id.result);

        ArrayList<NumsNum> possibleMoves = engine.AnalysePosition(mAdapter.moves);
        int possibleMovesCount = possibleMoves.size();

        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.result2Layout);
        resultLayout.removeAllViews();

        if (possibleMovesCount > 0) {
            boolean possible = false;
            int pMoveInt = 0;
            if (pMove != null) {
                pMoveInt = pMove.getNum();
                possible = false;
                for (int i = 0; i < possibleMoves.size(); i++) {
                    if (possibleMoves.get(i).getNum() == pMoveInt) {
                        possible = true;
                        break;
                    }
                }
            }

            if (possible && (pMove != null))
            {
                possibleMove = new NumsNum(pMoveInt);
            }
            else {
                Random r = new Random();
                int index = r.nextInt(possibleMoves.size());
                possibleMove = possibleMoves.get(index);
            }

            resultView.setText(getString(R.string.activity_analysis_possible_moves_count) + possibleMovesCount);

            TextView resultView2 = new TextView(AnalysisActivity.this);
            resultView2.setTextColor(Color.WHITE);
            resultView2.setText(getString(R.string.activity_analysis_possible_move));
            resultLayout.addView(resultView2);

            possibleMoveButton = new Button(AnalysisActivity.this);
            possibleMoveButton.setText(possibleMove.getNumString());

            possibleMoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentMove.Guess.Digits[0] = possibleMove.Digits[0];
                    currentMove.Guess.Digits[1] = possibleMove.Digits[1];
                    currentMove.Guess.Digits[2] = possibleMove.Digits[2];
                    currentMove.Guess.Digits[3] = possibleMove.Digits[3];
                    currentMove.Guess.Digits[4] = possibleMove.Digits[4];

                    currentMove.Count.First = 0;
                    currentMove.Count.Second = 0;

                    selectedButton = 6;
                    SetButtons(6);
                }
            });


            resultLayout.addView(possibleMoveButton);
        }
        else
        {
            resultView.setText(getString(R.string.activity_analysis_possible_moves_count) + possibleMovesCount);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analysis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public void SetButtons(int selected) {
        switch (selected)
        {
            case 1:
                button1.setChecked(true);
                button2.setChecked(false);
                button3.setChecked(false);
                button4.setChecked(false);
                button5.setChecked(false);
                count1.setChecked(false);
                count2.setChecked(false);
                break;
            case 2:
                button1.setChecked(false);
                button2.setChecked(true);
                button3.setChecked(false);
                button4.setChecked(false);
                button5.setChecked(false);
                count1.setChecked(false);
                count2.setChecked(false);
                break;
            case 3:
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(true);
                button4.setChecked(false);
                button5.setChecked(false);
                count1.setChecked(false);
                count2.setChecked(false);
                break;
            case 4:
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                button4.setChecked(true);
                button5.setChecked(false);
                count1.setChecked(false);
                count2.setChecked(false);
                break;
            case 5:
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                button4.setChecked(false);
                button5.setChecked(true);
                count1.setChecked(false);
                count2.setChecked(false);
                break;
            case 6:
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                button4.setChecked(false);
                button5.setChecked(false);
                count1.setChecked(true);
                count2.setChecked(false);
                break;
            case 7:
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                button4.setChecked(false);
                button5.setChecked(false);
                count1.setChecked(false);
                count2.setChecked(true);
                break;
        }

        button1.setText(Byte.toString(currentMove.Guess.Digits[0]));
        button2.setText(Byte.toString(currentMove.Guess.Digits[1]));
        button3.setText(Byte.toString(currentMove.Guess.Digits[2]));
        button4.setText(Byte.toString(currentMove.Guess.Digits[3]));
        button5.setText(Byte.toString(currentMove.Guess.Digits[4]));

        count1.setText(Byte.toString(currentMove.Count.First));
        count2.setText(Byte.toString(currentMove.Count.Second));

        int digit = -1;
        switch(selectedButton)
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                digit = currentMove.Guess.Digits[selected-1];
                break;
            case 6:
                digit = currentMove.Count.First;
                break;
            case 7:
                digit = currentMove.Count.Second;
                break;
        }


        if ((selected == 6) || (selected == 7))
        {
            digit6.setChecked(false);
            digit7.setChecked(false);
            digit8.setChecked(false);
            digit9.setChecked(false);

            digit6.setEnabled(false);
            digit7.setEnabled(false);
            digit8.setEnabled(false);
            digit9.setEnabled(false);
        }
        else
        {
            digit6.setEnabled(true);
            digit7.setEnabled(true);
            digit8.setEnabled(true);
            digit9.setEnabled(true);
        }

        switch (digit)
        {
            case 1:
                digit0.setChecked(false);
                digit1.setChecked(true);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(false);
                    digit8.setChecked(false);
                    digit9.setChecked(false);
                }
                break;
            case 2:
                digit0.setChecked(false);
                digit1.setChecked(false);
                digit2.setChecked(true);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(false);
                    digit8.setChecked(false);
                    digit9.setChecked(false);
                }
                break;
            case 3:
                digit0.setChecked(false);
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(true);
                digit4.setChecked(false);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(false);
                    digit8.setChecked(false);
                    digit9.setChecked(false);
                }
                break;
            case 4:
                digit0.setChecked(false);
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(true);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(false);
                    digit8.setChecked(false);
                    digit9.setChecked(false);
                }
                break;
            case 5:
                digit0.setChecked(false);
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(true);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(false);
                    digit8.setChecked(false);
                    digit9.setChecked(false);
                }
                break;
            case 6:
                digit0.setChecked(false);
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(true);
                    digit7.setChecked(false);
                    digit8.setChecked(false);
                    digit9.setChecked(false);
                }
                break;
            case 7:
                digit0.setChecked(false);
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(true);
                    digit8.setChecked(false);
                    digit9.setChecked(false);
                }
                break;
            case 8:
                digit0.setChecked(false);
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(false);
                    digit8.setChecked(true);
                    digit9.setChecked(false);
                }
                break;
            case 9:
                digit0.setChecked(false);
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(false);
                    digit8.setChecked(false);
                    digit9.setChecked(true);
                }
                break;
            case 0:
                digit0.setChecked(true);
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                if ((selected != 6) && (selected != 7)) {
                    digit6.setChecked(false);
                    digit7.setChecked(false);
                    digit8.setChecked(false);
                    digit9.setChecked(false);
                }
                break;
        }
    }
}

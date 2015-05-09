package ru.besuglovs.games.numbers;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.besuglovs.games.numbers.RecyclerView.RecyclerViewAdapter;
import ru.besuglovs.games.numbers.ToolBox.NumsMove;
import ru.besuglovs.games.numbers.ToolBox.NumsNum;
import ru.besuglovs.games.numbers.ToolBox.ToolBox;
import ru.besuglovs.games.numbers.engines.DefaultEngine;


public class SingleGameActivity extends Activity {

    private NumsNum computersNumber;
    private DefaultEngine computersEngine;
    private ArrayList<NumsMove> moves;
    private NumsMove currentMove;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ToggleButton button1, button2, button3, button4, button5;
    private int selectedButton;
    private ToggleButton digit1, digit2, digit3, digit4, digit5, digit6, digit7, digit8, digit9, digit0;
    private Button makeMoveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_game);

        computersEngine = new DefaultEngine();

        if (savedInstanceState != null)
        {
            int savedNum = savedInstanceState.getInt("computersNumber");
            computersNumber = new NumsNum(savedNum);
        }
        else
        {
            computersNumber = computersEngine.CreateOwnNum();
        }

        moves = new ArrayList<NumsMove>();
        NumsNum guess = new NumsNum(12345);
        currentMove = new NumsMove(guess);

        if (savedInstanceState != null)
        {
            String movesString = savedInstanceState.getString("moves");

            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<NumsMove>>() {}.getType();
            moves = gson.fromJson(movesString, collectionType);

            if (playerWon())
            {
                hideControls();
            }

            int curMove = savedInstanceState.getInt("currentMove");
            currentMove = new NumsMove(new NumsNum(curMove));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.movesList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(moves);
        mRecyclerView.setAdapter(mAdapter);

        button1 = (ToggleButton) findViewById(R.id.button1);
        button2 = (ToggleButton) findViewById(R.id.button2);
        button3 = (ToggleButton) findViewById(R.id.button3);
        button4 = (ToggleButton) findViewById(R.id.button4);
        button5 = (ToggleButton) findViewById(R.id.button5);

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

        makeMoveButton = (Button) findViewById(R.id.makeMove);

        makeMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolBox.IsCorrect(currentMove.Guess.getNum())) {

                    currentMove.Count = ToolBox.CalculateCount(currentMove.Guess, computersNumber);
                    moves.add(currentMove);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(moves.size() - 1);

                    if (currentMove.Count.Second == 5) {
                        Toast.makeText(SingleGameActivity.this, getString(R.string.activity_single_game_you_won), Toast.LENGTH_LONG).show();

                        hideControls();

                    } else {
                        NumsNum guess = new NumsNum(currentMove.Guess.getNum());
                        currentMove = new NumsMove(guess);
                    }
                }
                else {
                    Toast.makeText(SingleGameActivity.this, getString(R.string.distinct_digits), Toast.LENGTH_LONG).show();
                }
            }
        });

        selectedButton = 1;
        button1.setChecked(true);
        SetButtons(1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button1.isChecked()) {
                    selectedButton = 1;
                    SetButtons(1);
                }
                else
                {
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

        digit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit1.isChecked()) {
                    currentMove.Guess.Digits[selectedButton-1] = 1;
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
                    currentMove.Guess.Digits[selectedButton-1] = 2;
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
                    currentMove.Guess.Digits[selectedButton-1] = 3;
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
                    currentMove.Guess.Digits[selectedButton-1] = 4;
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
                    currentMove.Guess.Digits[selectedButton-1] = 5;
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
                    currentMove.Guess.Digits[selectedButton-1] = 6;
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
                    currentMove.Guess.Digits[selectedButton-1] = 7;
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
                    currentMove.Guess.Digits[selectedButton-1] = 8;
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
                    currentMove.Guess.Digits[selectedButton-1] = 9;
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
                    currentMove.Guess.Digits[selectedButton-1] = 0;
                    SetButtons(selectedButton);
                }
                else
                {
                    digit0.setChecked(true);
                }
            }
        });
    }

    private void hideControls() {
        final LinearLayout layout15 = (LinearLayout) findViewById(R.id.digits15);
        final LinearLayout layout60 = (LinearLayout) findViewById(R.id.digits60);
        final LinearLayout numberLayout = (LinearLayout) findViewById(R.id.guessNumber);

        layout15.setVisibility(View.GONE);
        layout60.setVisibility(View.GONE);
        numberLayout.setVisibility(View.GONE);
    }

    private boolean playerWon() {
        return (moves.size() > 0) && (moves.get(moves.size() - 1).Count.Second == 5);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gson = new Gson();
        String json = gson.toJson(moves);
        outState.putString("moves", json);

        outState.putInt("computersNumber", computersNumber.getNum());

        outState.putInt("currentMove", currentMove.Guess.getNum());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_game, menu);
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
                break;
            case 2:
                button1.setChecked(false);
                button2.setChecked(true);
                button3.setChecked(false);
                button4.setChecked(false);
                button5.setChecked(false);
                break;
            case 3:
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(true);
                button4.setChecked(false);
                button5.setChecked(false);
                break;
            case 4:
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                button4.setChecked(true);
                button5.setChecked(false);
                break;
            case 5:
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                button4.setChecked(false);
                button5.setChecked(true);
                break;
        }

        button1.setText(Byte.toString(currentMove.Guess.Digits[0]));
        button2.setText(Byte.toString(currentMove.Guess.Digits[1]));
        button3.setText(Byte.toString(currentMove.Guess.Digits[2]));
        button4.setText(Byte.toString(currentMove.Guess.Digits[3]));
        button5.setText(Byte.toString(currentMove.Guess.Digits[4]));

        int digit = currentMove.Guess.Digits[selected-1];

        switch (digit)
        {
            case 1:
                digit1.setChecked(true);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                digit6.setChecked(false);
                digit7.setChecked(false);
                digit8.setChecked(false);
                digit9.setChecked(false);
                digit0.setChecked(false);
                break;
            case 2:
                digit1.setChecked(false);
                digit2.setChecked(true);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                digit6.setChecked(false);
                digit7.setChecked(false);
                digit8.setChecked(false);
                digit9.setChecked(false);
                digit0.setChecked(false);
                break;
            case 3:
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(true);
                digit4.setChecked(false);
                digit5.setChecked(false);
                digit6.setChecked(false);
                digit7.setChecked(false);
                digit8.setChecked(false);
                digit9.setChecked(false);
                digit0.setChecked(false);
                break;
            case 4:
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(true);
                digit5.setChecked(false);
                digit6.setChecked(false);
                digit7.setChecked(false);
                digit8.setChecked(false);
                digit9.setChecked(false);
                digit0.setChecked(false);
                break;
            case 5:
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(true);
                digit6.setChecked(false);
                digit7.setChecked(false);
                digit8.setChecked(false);
                digit9.setChecked(false);
                digit0.setChecked(false);
                break;
            case 6:
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                digit6.setChecked(true);
                digit7.setChecked(false);
                digit8.setChecked(false);
                digit9.setChecked(false);
                digit0.setChecked(false);
                break;
            case 7:
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                digit6.setChecked(false);
                digit7.setChecked(true);
                digit8.setChecked(false);
                digit9.setChecked(false);
                digit0.setChecked(false);
                break;
            case 8:
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                digit6.setChecked(false);
                digit7.setChecked(false);
                digit8.setChecked(true);
                digit9.setChecked(false);
                digit0.setChecked(false);
                break;
            case 9:
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                digit6.setChecked(false);
                digit7.setChecked(false);
                digit8.setChecked(false);
                digit9.setChecked(true);
                digit0.setChecked(false);
                break;
            case 0:
                digit1.setChecked(false);
                digit2.setChecked(false);
                digit3.setChecked(false);
                digit4.setChecked(false);
                digit5.setChecked(false);
                digit6.setChecked(false);
                digit7.setChecked(false);
                digit8.setChecked(false);
                digit9.setChecked(false);
                digit0.setChecked(true);
                break;
        }
    }
}

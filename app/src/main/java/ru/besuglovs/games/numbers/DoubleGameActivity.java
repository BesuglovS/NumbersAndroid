package ru.besuglovs.games.numbers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class DoubleGameActivity extends Activity {

    private NumsNum playersNumber;
    private NumsNum computersNumber;
    private DefaultEngine engine;
    private ArrayList<NumsMove> playerMoves;
    private ArrayList<NumsMove> computerMoves;
    private NumsMove currentMove, savedPlayerMove;
    private MoveTurn currentTurn;
    private LayoutType currentLayout;
    private boolean isNumberChosen;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ToggleButton button1, button2, button3, button4, button5;
    private int selectedButton;
    private ToggleButton digit1, digit2, digit3, digit4, digit5, digit6, digit7, digit8, digit9, digit0;
    private Button makeMoveButton, makeComputerMoveButton, continueGameButton;
    private LinearLayout guessNumber;
    private LinearLayout digits15;
    private LinearLayout digits60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_game);

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
        makeComputerMoveButton = (Button) findViewById(R.id.makeComputerMove);
        continueGameButton = (Button) findViewById(R.id.continueGame);

        guessNumber = (LinearLayout) findViewById(R.id.guessNumber);
        digits15 = (LinearLayout) findViewById(R.id.digits15);
        digits60 = (LinearLayout) findViewById(R.id.digits60);

        engine = new DefaultEngine();

        if (savedInstanceState != null)
        {
            int savedNum = savedInstanceState.getInt("computersNumber");
            computersNumber = new NumsNum(savedNum);
        }
        else
        {
            computersNumber = engine.CreateOwnNum();
        }

        playerMoves = new ArrayList<NumsMove>();
        computerMoves = new ArrayList<NumsMove>();
        NumsNum guess = new NumsNum(12345);
        currentMove = new NumsMove(guess);
        currentTurn = MoveTurn.Player;

        if (savedInstanceState != null)
        {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<NumsMove>>() {}.getType();

            String movesString = savedInstanceState.getString("playerMoves");
            if (movesString != null) {
                playerMoves = gson.fromJson(movesString, collectionType);
            }

            movesString = savedInstanceState.getString("computerMoves");
            if (movesString != null) {
                computerMoves = gson.fromJson(movesString, collectionType);
            }

            int playersNumberInt = savedInstanceState.getInt("playersNumber");
            if (playersNumberInt != 0) {
                playersNumber = new NumsNum(playersNumberInt);
            }

            int curMove = savedInstanceState.getInt("currentMove");
            if (curMove != 0) {
                currentMove = new NumsMove(new NumsNum(curMove));
            }

            int savedPlayerMoveInt = savedInstanceState.getInt("savedPlayerMove");
            if (savedPlayerMoveInt != 0) {
                savedPlayerMove = new NumsMove(new NumsNum(savedPlayerMoveInt));
            }

            Boolean currentTurnBoolean = savedInstanceState.getBoolean("MoveTurn");
            currentTurn = currentTurnBoolean ? MoveTurn.Player : MoveTurn.Computer;

            currentLayout = (LayoutType) savedInstanceState.getSerializable("currentLayout");
            ShowLayout(currentLayout);

            switch (currentLayout)
            {
                case SingleGame:
                    SetRecycleViewMoves(playerMoves);
                    break;
                case ComputerGame:
                    SetRecycleViewMoves(computerMoves);
                    break;
                case Continue:
                    switch (currentTurn) {
                        case Player:
                            SetRecycleViewMoves(playerMoves);
                            break;
                        case Computer:
                            SetRecycleViewMoves(computerMoves);
                            break;
                    }
                    break;
            }

            isNumberChosen = savedInstanceState.getBoolean("isNumberChosen");
        }
        else {
            SetRecycleViewMoves(playerMoves);

            isNumberChosen = false;
        }

        if (!isNumberChosen) {
            final AlertDialog requestDialog = new AlertDialog.Builder(DoubleGameActivity.this)
                    .setTitle(getString(R.string.activity_computer_game_set_own_number))
                    .setView(R.layout.computer_game_input)
                    .setPositiveButton("OK", null)
                    .create();

            requestDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button b = requestDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText numText = (EditText) (requestDialog.findViewById(R.id.number));
                            int num = 0;
                            boolean correctFormat = true;
                            try {
                                num = Integer.parseInt(numText.getText().toString());
                            } catch (NumberFormatException e) {
                                correctFormat = false;
                            }
                            if (correctFormat && ToolBox.IsCorrect(num)) {
                                playersNumber = new NumsNum(num);
                                isNumberChosen = true;
                                requestDialog.dismiss();
                            } else {
                                Toast.makeText(DoubleGameActivity.this, getString(R.string.distinct_digits), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            requestDialog.show();
        }

        if (savedInstanceState == null) {
            ShowLayout(LayoutType.SingleGame);

            selectedButton = 1;
            button1.setChecked(true);
            SetButtons(1);
        }

        continueGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerWon() && computerWon()) {
                    switch (currentTurn) {
                        case Player:
                            mAdapter.moves = computerMoves;
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(mAdapter.moves.size() - 1);

                            currentTurn = MoveTurn.Computer;
                            break;
                        case Computer:
                            mAdapter.moves = playerMoves;
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(mAdapter.moves.size() - 1);

                            currentTurn = MoveTurn.Player;
                            break;
                    }
                } else {
                    switch (currentTurn) {
                        case Player:
                            if (computerWon()) {
                                mAdapter.moves = playerMoves;
                            } else {
                                mAdapter.moves = computerMoves;

                                ShowLayout(LayoutType.ComputerGame);
                                currentTurn = MoveTurn.Computer;
                            }

                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(mAdapter.moves.size() - 1);
                            break;
                        case Computer:
                            if (playerWon()) {
                                mAdapter.moves = computerMoves;
                            } else {
                                mAdapter.moves = playerMoves;

                                ShowLayout(LayoutType.SingleGame);
                                currentTurn = MoveTurn.Player;

                                NumsNum oldGuess = new NumsNum(savedPlayerMove.Guess.getNum());
                                currentMove = new NumsMove(oldGuess);
                            }

                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(mAdapter.moves.size() - 1);
                            break;
                    }
                }
            }
        });



        makeMoveButton = (Button) findViewById(R.id.makeMove);

        makeMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolBox.IsCorrect(currentMove.Guess.getNum())) {

                    currentMove.Count = ToolBox.CalculateCount(currentMove.Guess, computersNumber);
                    playerMoves.add(currentMove);

                    if (currentMove.Count.Second == 5) {
                        Toast.makeText(DoubleGameActivity.this, getString(R.string.activity_single_game_you_won), Toast.LENGTH_LONG).show();
                    } else {
                        NumsNum guess = new NumsNum(currentMove.Guess.getNum());
                        savedPlayerMove = new NumsMove(guess);
                    }

                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mAdapter.moves.size() - 1);

                    if ((playerWon() && computerWon()) || (!computerWon())) {
                        ShowLayout(LayoutType.Continue);
                    } else {
                        NumsNum oldGuess = new NumsNum(savedPlayerMove.Guess.getNum());
                        currentMove = new NumsMove(oldGuess);
                    }

                } else {
                    Toast.makeText(DoubleGameActivity.this, getString(R.string.distinct_digits), Toast.LENGTH_LONG).show();
                }
            }
        });

        makeComputerMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumsNum guess = engine.GetMove(computerMoves);
                NumsMove move = new NumsMove(guess);
                move.Count = ToolBox.CalculateCount(guess, playersNumber);
                computerMoves.add(move);

                if (move.Count.Second == 5) {
                    Toast.makeText(DoubleGameActivity.this, getString(R.string.activity_computer_game_guess_is_right), Toast.LENGTH_LONG).show();
                    makeComputerMoveButton.setVisibility(View.GONE);
                }

                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mAdapter.moves.size() - 1);

                if ((playerWon() && computerWon()) || (!playerWon())) {
                    ShowLayout(LayoutType.Continue);
                }
            }
        });



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

    private void SetRecycleViewMoves(ArrayList<NumsMove> moves) {
        mRecyclerView = (RecyclerView) findViewById(R.id.movesList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(moves);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gson = new Gson();
        String json = gson.toJson(playerMoves);
        outState.putString("playerMoves", json);

        json = gson.toJson(computerMoves);
        outState.putString("computerMoves", json);

        if (playersNumber != null) {
            outState.putInt("playersNumber", playersNumber.getNum());
        }

        if (computersNumber != null) {
            outState.putInt("computersNumber", computersNumber.getNum());
        }

        if (currentMove != null) {
            outState.putInt("currentMove", currentMove.Guess.getNum());
        }

        if (savedPlayerMove != null) {
            outState.putInt("savedPlayerMove", savedPlayerMove.Guess.getNum());
        }

        if (currentTurn != null) {
            outState.putBoolean("MoveTurn", currentTurn == MoveTurn.Player);
        }

        if (currentLayout != null)
        {
            outState.putSerializable("currentLayout", currentLayout);
        }

        outState.putBoolean("isNumberChosen", isNumberChosen);
    }

    private boolean playerWon() {
        return (playerMoves.size() > 0) && (playerMoves.get(playerMoves.size() - 1).Count.Second == 5);
    }

    private boolean computerWon() {
        return (computerMoves.size() > 0) && (computerMoves.get(computerMoves.size() - 1).Count.Second == 5);
    }

    private void ShowLayout(LayoutType layoutType) {
        currentLayout = layoutType;

        switch (layoutType)
        {
            case SingleGame:
                guessNumber.setVisibility(View.VISIBLE);
                digits15.setVisibility(View.VISIBLE);
                digits60.setVisibility(View.VISIBLE);
                makeComputerMoveButton.setVisibility(View.GONE);
                continueGameButton.setVisibility(View.GONE);
                break;
            case ComputerGame:
                guessNumber.setVisibility(View.GONE);
                digits15.setVisibility(View.GONE);
                digits60.setVisibility(View.GONE);
                makeComputerMoveButton.setVisibility(View.VISIBLE);
                continueGameButton.setVisibility(View.GONE);
                break;
            case Continue:
                guessNumber.setVisibility(View.GONE);
                digits15.setVisibility(View.GONE);
                digits60.setVisibility(View.GONE);
                makeComputerMoveButton.setVisibility(View.GONE);
                continueGameButton.setVisibility(View.VISIBLE);

                if (playerWon() && computerWon())
                {
                    continueGameButton.setText(getString(R.string.double_game_activity_swith_view));
                }
                break;
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_double_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public enum LayoutType {
        SingleGame,
        ComputerGame,
        Continue
    }

    public enum MoveTurn {
        Player,
        Computer
    }
}

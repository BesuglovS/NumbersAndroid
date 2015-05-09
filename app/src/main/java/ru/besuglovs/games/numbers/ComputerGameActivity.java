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
import android.widget.Toast;

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


public class ComputerGameActivity extends Activity {

    private NumsNum playersNumber;
    private ArrayList<NumsMove> moves;
    private DefaultEngine engine;
    private boolean isNumberChosen;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_game);

        final Button makeMoveButton = (Button) findViewById(R.id.makeMove);

        moves = new ArrayList<NumsMove>();

        if (savedInstanceState != null)
        {
            String movesString = savedInstanceState.getString("moves");

            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<NumsMove>>() {}.getType();
            moves = gson.fromJson(movesString, collectionType);


            int playersNumberInt = savedInstanceState.getInt("playersNumber");
            playersNumber = new NumsNum(playersNumberInt);

            if (computerWon())
            {
                makeMoveButton.setVisibility(View.GONE);
            }

            isNumberChosen = savedInstanceState.getBoolean("isNumberChosen");
        }
        else
        {
            isNumberChosen = false;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.movesList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(moves);
        mRecyclerView.setAdapter(mAdapter);

        engine = new DefaultEngine();

        if (!isNumberChosen) {
            final AlertDialog requestDialog = new AlertDialog.Builder(ComputerGameActivity.this)
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
                                Toast.makeText(ComputerGameActivity.this, getString(R.string.distinct_digits), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            requestDialog.show();
        }

        makeMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumsNum guess = engine.GetMove(moves);
                NumsMove move = new NumsMove(guess);
                move.Count = ToolBox.CalculateCount(guess, playersNumber);
                moves.add(move);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(moves.size() - 1);

                if (move.Count.Second == 5) {
                    Toast.makeText(ComputerGameActivity.this, getString(R.string.activity_computer_game_guess_is_right), Toast.LENGTH_LONG).show();
                    makeMoveButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean computerWon() {
        return (moves.size() > 0) && (moves.get(moves.size() - 1).Count.Second == 5);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gson = new Gson();
        String json = gson.toJson(moves);
        outState.putString("moves", json);

        if (playersNumber != null) {
            outState.putInt("playersNumber", playersNumber.getNum());
        }

        outState.putBoolean("isNumberChosen", isNumberChosen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_computer_game, menu);
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
}

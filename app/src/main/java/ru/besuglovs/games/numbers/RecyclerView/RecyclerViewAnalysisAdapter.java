package ru.besuglovs.games.numbers.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import ru.besuglovs.games.numbers.AnalysisActivity;
import ru.besuglovs.games.numbers.R;
import ru.besuglovs.games.numbers.ToolBox.NumsMove;

/**
 * Created by Besug on 06.05.2015.
 */
public class RecyclerViewAnalysisAdapter extends RecyclerView.Adapter<RecyclerViewAnalysisAdapter.ViewHolder> {
    public ArrayList<NumsMove> moves;
    private final AnalysisActivity activity;

    public RecyclerViewAnalysisAdapter(ArrayList<NumsMove> moves, AnalysisActivity activity) {
        this.moves = moves;
        this.activity = activity;
    }

    /**
     * ???????? ????? View ? ViewHolder ???????? ??????, ??????? ???????????? ????? ??????????????????.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.move_view_analysis, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAnalysisAdapter.ViewHolder viewHolder, int i) {
        NumsMove record = moves.get(i);
        viewHolder.digit1.setText(Byte.toString(record.Guess.Digits[0]));
        viewHolder.digit2.setText(Byte.toString(record.Guess.Digits[1]));
        viewHolder.digit3.setText(Byte.toString(record.Guess.Digits[2]));
        viewHolder.digit4.setText(Byte.toString(record.Guess.Digits[3]));
        viewHolder.digit5.setText(Byte.toString(record.Guess.Digits[4]));

        final RecyclerViewAnalysisAdapter t = this;
        final int position = i;

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moves.remove(position);
                t.notifyDataSetChanged();

                activity.RefreshAnalysis(null);
            }
        });

        viewHolder.count1.setText(Byte.toString(record.Count.First));
        viewHolder.count2.setText(Byte.toString(record.Count.Second));
    }

    public void addMove(NumsMove move)
    {
        moves.add(move);
    }

    public void clearMoves() {
        moves.clear();
    }

    /**
     * ?????????? ???????? View ??????? ?? ???????? ?????? ? ??????? i
     */


    @Override
    public int getItemCount() {
        return moves.size();
    }

    /**
     * ?????????? ?????? ViewHolder, ????????? ?????? ?? ???????.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private Button digit1;
        private Button digit2;
        private Button digit3;
        private Button digit4;
        private Button digit5;

        private Button remove;

        private Button count1;
        private Button count2;

        public ViewHolder(View itemView) {
            super(itemView);
            digit1 = (Button) itemView.findViewById(R.id.button1);
            digit2 = (Button) itemView.findViewById(R.id.button2);
            digit3 = (Button) itemView.findViewById(R.id.button3);
            digit4 = (Button) itemView.findViewById(R.id.button4);
            digit5 = (Button) itemView.findViewById(R.id.button5);

            remove = (Button) itemView.findViewById(R.id.remove);

            count1 = (Button) itemView.findViewById(R.id.count1);
            count2 = (Button) itemView.findViewById(R.id.count2);
        }
    }
}

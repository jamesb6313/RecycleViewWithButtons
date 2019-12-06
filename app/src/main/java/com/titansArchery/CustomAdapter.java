package com.titansArchery;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context ctx;

    CustomAdapter(Context ctx) {

        inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    @Override
    @NonNull
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_item, parent, false);
        //MyViewHolder holder = new MyViewHolder(view);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.MyViewHolder holder, int position) {
        boolean red = true;

        holder.tvTargetName.setText(MainActivity.modelArrayList.get(position).getTargetName());
        holder.tvTargetScore.setText(String.valueOf(MainActivity.modelArrayList.get(position).getTargetScore()));


        holder.tvElapsedTime.setText(MainActivity.modelArrayList.get(position).getElapsedTime());
        if (!MainActivity.modelArrayList.get(position).getElapsedTime().equals("-1"))
            red = false;
        if (!red)
            holder.tvElapsedTime.setTextColor(Color.GREEN);
        holder.btn_minus.setEnabled(false);
        holder.btn_plus.setEnabled(false);
        //holder.btn_minus.setClickable(false);
        //holder.btn_plus.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return MainActivity.modelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button btn_plus, btn_minus;
        private TextView tvTargetName, tvTargetScore, tvElapsedTime;

        MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this); //add click for row to enable_disable row


            tvTargetName = itemView.findViewById(R.id.targetNumber);
            tvTargetScore = itemView.findViewById(R.id.targetScore);
            btn_plus = itemView.findViewById(R.id.plus);
            btn_minus = itemView.findViewById(R.id.minus);
            btn_minus.setEnabled(false);
            btn_plus.setEnabled(false);


            btn_plus.setTag(R.integer.btn_plus_view, itemView);
            btn_minus.setTag(R.integer.btn_minus_view, itemView);
            btn_plus.setOnClickListener(this);
            btn_minus.setOnClickListener(this);

            tvElapsedTime = itemView.findViewById(R.id.targetElapsedTime);
        }

        //onClick Listener for view
        @Override
        public void onClick(View v) {
            if (MainActivity.startRound) {
                //Add to Score
                if (v.getId() == btn_plus.getId()) {

                    View tempView = (View) btn_plus.getTag(R.integer.btn_plus_view);
                    TextView tv = tempView.findViewById(R.id.targetScore);  //add to target score

                    int number = Integer.parseInt(tv.getText().toString()) + 1;
                    tv.setText(String.valueOf(number));

                    TextView tv_time = tempView.findViewById(R.id.targetElapsedTime);
                    android.util.Log.i("time1: ", tv_time.getText().toString());

                    if (tv_time.getText().toString().equals("-1")) {
                        android.util.Log.i("time2: ", MainActivity.timerValue.getText().toString());
                        //TextView tv_curTime = tempview.findViewById(R.id.timerValue);
                        String curTime = MainActivity.timerValue.getText().toString();
                        android.util.Log.i("curTime: ", curTime);

                        tv_time.setText(curTime);
                        tv_time.setTextColor(Color.GREEN);
                        MainActivity.modelArrayList.get(getAdapterPosition()).setElapsedTime(curTime);
                    }

                    MainActivity.modelArrayList.get(getAdapterPosition()).setTargetScore(number);
                }  //Subtract from Score
                else if (v.getId() == btn_minus.getId()) {

                    View tempView = (View) btn_minus.getTag(R.integer.btn_minus_view);
                    TextView tv = tempView.findViewById(R.id.targetScore);  //minus from target score

                    int number = Integer.parseInt(tv.getText().toString()) - 1;
                    if (number <= 0) number = 0;        // don't allow negative number
                    tv.setText(String.valueOf(number));

                    MainActivity.modelArrayList.get(getAdapterPosition()).setTargetScore(number);
                }   //Enable_Disable Target Score Row
                else {

                    //Select Row to Enable Plus, Minus Buttons
                    int position = getAdapterPosition();

                    Button btn_plus = v.findViewById(R.id.plus);
                    Button btn_minus = v.findViewById(R.id.minus);

                    btn_minus.setEnabled(true);
                    btn_plus.setEnabled(true);
                    //btn_minus.setClickable(true);
                    //btn_plus.setClickable(true);

                    String curTarget = MainActivity.modelArrayList.get(position).getTargetName();
                    Log.i("MyINFO", "TargetName =  " + curTarget);
                    Toast.makeText(ctx, "Current Target =  " + curTarget, Toast.LENGTH_SHORT).show();

                    //
                }
            }
        }
    }
}

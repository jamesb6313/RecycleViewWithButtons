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

import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context ctx;

    public CustomAdapter(Context ctx) {

        inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.MyViewHolder holder, int position) {
        boolean red = true;

        holder.tvTargetName.setText(MainActivity.modelArrayList.get(position).getTargetName());
        holder.tvTargetScore.setText(String.valueOf(MainActivity.modelArrayList.get(position).getTargetScore()));


        holder.tvElapsedTime.setText(MainActivity.modelArrayList.get(position).getElapsedTime());
        if (!MainActivity.modelArrayList.get(position).getElapsedTime().equals("-1")) {
            red = false;
        }
        if (!red) {
            holder.tvElapsedTime.setTextColor(Color.GREEN);
        }
        holder.btn_minus.setEnabled(false);
        holder.btn_plus.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return MainActivity.modelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Button btn_plus, btn_minus;
        private TextView tvTargetName, tvTargetScore, tvElapsedTime;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this); //add click for row to enable_disable row


            tvTargetName = (TextView) itemView.findViewById(R.id.targetNumber);
            tvTargetScore = (TextView) itemView.findViewById(R.id.targetScore);
            btn_plus = (Button) itemView.findViewById(R.id.plus);
            btn_minus = (Button) itemView.findViewById(R.id.minus);

            btn_plus.setTag(R.integer.btn_plus_view, itemView);
            btn_minus.setTag(R.integer.btn_minus_view, itemView);
            btn_plus.setOnClickListener(this);
            btn_minus.setOnClickListener(this);

            tvElapsedTime = (TextView) itemView.findViewById(R.id.targetElapsedTime);
        }

        //onClick Listener for view
        @Override
        public void onClick(View v) {
            //Add to Score
            if (v.getId() == btn_plus.getId()) {

                View tempview = (View) btn_plus.getTag(R.integer.btn_plus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.targetScore);  //add to target score

                int number = Integer.parseInt(tv.getText().toString()) + 1;
                tv.setText(String.valueOf(number));
                MainActivity.modelArrayList.get(getAdapterPosition()).setTargetScore(number);
            }  //Subtract from Score
            else if (v.getId() == btn_minus.getId()) {

                View tempview = (View) btn_minus.getTag(R.integer.btn_minus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.targetScore);  //minus from target score

                int number = Integer.parseInt(tv.getText().toString()) - 1;
                tv.setText(String.valueOf(number));
                MainActivity.modelArrayList.get(getAdapterPosition()).setTargetScore(number);
            }   //Enable_Disable Target Score Row
            else {

                //Select Row to Enable Plus, Minus Buttons
                int position = getAdapterPosition();

                Button btnPlus = v.findViewById(R.id.plus);
                Button btnMinus = v.findViewById(R.id.minus);

                btnMinus.setEnabled(true);
                btnPlus.setEnabled(true);
                String curTarget = MainActivity.modelArrayList.get(position).getTargetName();
                Log.i("MyINFO","TargetName =  " + curTarget);
                Toast.makeText(ctx, "Current Target =  " + curTarget, Toast.LENGTH_SHORT).show();

                //
            }
        }
    }
}

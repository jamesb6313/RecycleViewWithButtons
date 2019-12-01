package com.titansArchery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

        holder.tvTargetName.setText(MainActivity.modelArrayList.get(position).getTargetName());
        holder.tvTargetScore.setText(String.valueOf(MainActivity.modelArrayList.get(position).getTargetScore()));

    }

    @Override
    public int getItemCount() {
        return MainActivity.modelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Button btn_plus, btn_minus;
        private TextView tvTargetName, tvTargetScore;

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

                int position = getAdapterPosition();
                TextView fruitName = (TextView) v.findViewById(R.id.targetNumber);  //name of target

                Log.i("MyINFO","the item position that was clicked is " + position);
                Log.i("MyINFO","fruit: (TextView) var fruitName.getText() " + fruitName.getText());

                String fname = MainActivity.modelArrayList.get(getAdapterPosition()).getTargetName();
                Log.i("MyINFO","fruit: model.getFruit " + fname);

            }
        }
    }
}

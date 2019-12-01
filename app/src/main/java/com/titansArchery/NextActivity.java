package com.titansArchery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    private TextView tvScoreSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        tvScoreSheet = (TextView) findViewById(R.id.scoreSheet);

        int targetCount = MainActivity.modelArrayList.size();   //NumberOfTargets = 24

        for (int i = 0; i < targetCount; i++) {
            String txt = tvScoreSheet.getText().toString();
            tvScoreSheet.setText(txt +
                    MainActivity.modelArrayList.get(i).getTargetName() + " -> " +
                    MainActivity.modelArrayList.get(i).getTargetScore() + "\n");
        }
    }
}

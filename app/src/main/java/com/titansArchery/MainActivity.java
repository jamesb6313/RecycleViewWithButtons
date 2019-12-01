package com.titansArchery;

//Base of shoot n scoot project taken from url below
//See: https://demonuts.com/recyclerview-button/



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static ArrayList<Model> modelArrayList;
    public static int NumberOfTargets = 24;
    private CustomAdapter customAdapter;
    private Button btnnext;
    //private String[] fruitlist = new String[]{"Apples", "Oranges", "Potatoes", "Tomatoes", "Grapes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        btnnext = (Button) findViewById(R.id.next);

        modelArrayList = getModel();
        customAdapter = new CustomAdapter(this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Model> getModel(){
        ArrayList<Model> list = new ArrayList<>();

        //int numHoles = R.integer.num_holes;
        for(int i = 0; i < NumberOfTargets; i++){

            Model model = new Model();
            model.setTargetScore(0);
            model.setTargetName("Target Number " + (i + 1));
            //model.setElapsedTime("-1");
            list.add(model);
        }
        return list;
    }
}

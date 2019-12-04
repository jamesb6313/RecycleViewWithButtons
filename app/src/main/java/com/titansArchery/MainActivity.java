package com.titansArchery;

/*
Main source code for this TitanArchersShootnScoot was taken from url below
See: https://demonuts.com/recyclerview-button/

//Gson - see references
See:
https://www.youtube.com/watch?v=jqv3Qkgop88 - shows how to add gson with Project Structure Settings
http://www.dev2qa.com/android-sharedpreferences-save-load-java-object-example/
https://github.com/google/gson

GitHub Repository:
https://github.com/jamesb6313/TitansShootnScoot.git
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//GSON Fix modules app/build.gradle add dependancy
//GSON resource defined in app/build.gradle
//GSON gson-2.8.2.jar - file needs to be added to app/libs
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<Model> modelArrayList;
    public static int NumberOfTargets = 24;

    public static TextView timerValue;
    private Button startButton;
    private Button pauseButton;
    private Handler customHandler = new Handler();
    public static final String TAG = "MyInfo";
    public static boolean startRound = false;

    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;

    //GSON
    Gson gson;

    String start_time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    long startTimeInMillis = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        Button btn_next = findViewById(R.id.next);


/*
        modelArrayList = getModel();
        CustomAdapter customAdapter = new CustomAdapter(this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
*/

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                startActivity(intent);
            }
        });
        //TODO - end

        mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mpref.edit();
        //GSON all the gson code - fixed
        gson = new Gson();

        timerValue = findViewById(R.id.timerValue);
        startButton = findViewById(R.id.btn_start);
        pauseButton = findViewById(R.id.btn_stop);

        try {
            Long l_value = mpref.getLong("data2", 0);
            startTimeInMillis = l_value;
            modelArrayList = getModel();

            Log.i(TAG , "init: mpref str_value = " + l_value);
            if (l_value == 0) {
                startButton.setEnabled(true);
                CustomAdapter customAdapter = new CustomAdapter(this);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


            } else {
                startButton.setEnabled(false);
                String str_value = mpref.getString("data1", "");
                setTitle("Titan's ScootnShoot: " + str_value);

                modelArrayList.clear();
                String json = mpref.getString("data3", "");

                //GSON all the gson code - fixed
                Type type = new TypeToken<ArrayList<Model>>() {}.getType();
                modelArrayList = gson.fromJson(json, type);

                CustomAdapter customAdapter = new CustomAdapter(this);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                Log.i(TAG,"onCreate() json " + json);

                customHandler.removeCallbacks(updateTimerThread);
                customHandler.postDelayed(updateTimerThread, 1000); // delay 1 second
            }
        } catch (Exception e) {
            Log.e(TAG,"onCreate() Error msg " + e.getMessage());
            mEditor.clear();

            customHandler.removeCallbacks(updateTimerThread);
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    startButton.setEnabled(false);
                    startRound = true;


                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    start_time = simpleDateFormat.format(calendar.getTime());
                    setTitle("Titan's ScootnShoot: " + start_time);

                    startTimeInMillis = calendar.getTimeInMillis();     //System.currentTimeMillis();

                    mEditor.putString("data1", start_time).apply();//commit();

                    mEditor.putLong("data2", startTimeInMillis).apply();

                    Log.i(TAG,"startButton.onClickListener() Start Time = " + start_time);

                    customHandler.removeCallbacks(updateTimerThread);
                    customHandler.postDelayed(updateTimerThread, 1000); // delay 1 second
                } catch (Exception e) {
                    Log.e(TAG,"startButton.onClickListener() Error msg " + e.getMessage());
                    customHandler.removeCallbacks(updateTimerThread);
                }
            }
        });

//FIXME: fix pauseButton - AlertDialog is main issue (new imports - solved??) - done

        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Stopping Timer will end round. Continue?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    customHandler.removeCallbacks(updateTimerThread);
                                    mEditor.clear().commit();
                                    Log.i(TAG,"stopButton.onClickListener() Editor Cleared");
                                    startRound = false; //todo maybe endRound or leave true

                                    dialog.cancel();
                                }
                            });
                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i(TAG,"stopButton.onClickListener() Do not close");
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert1 = builder1.create();
                    alert1.show();


                } catch (Exception e) {
                    Log.e(TAG,"stopButton.onClickListener() Error msg " + e.getMessage());
                }

            }
        });
    }

    private long calcTimeDiff() {
        long elapsedTime = 0L;

        try {
            calendar = Calendar.getInstance();
            elapsedTime = calendar.getTimeInMillis() - startTimeInMillis;

        } catch (Exception e) {
            Log.e(TAG,"calcTimeDiff() error msg " + e.getMessage());
        }
        return elapsedTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {

            String str_value = mpref.getString("data1", "");
            setTitle("Titan's ScootnShoot: " + str_value);

            String json = gson.toJson(modelArrayList);
            mEditor.putString("data3", json);
            Log.i(TAG,"onDestroy() json " + json);
            mEditor.apply();

            customHandler.removeCallbacks(updateTimerThread);
        } catch (Exception e) {
            Log.e(TAG,"onDestroy() error msg " + e.getMessage());
            mEditor.clear();
            customHandler.removeCallbacks(updateTimerThread);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            String str_value = mpref.getString("data1", "");
            setTitle("Titan's ScootnShoot: " + str_value);

            String json = gson.toJson(modelArrayList);
            mEditor.putString("data3", json);
            Log.i(TAG,"onPause() json " + json);
            mEditor.apply();

            customHandler.removeCallbacks(updateTimerThread);
        } catch (Exception e) {
            Log.e(TAG,"onPause() Error msg " + e.getMessage());
            mEditor.clear();
            customHandler.removeCallbacks(updateTimerThread);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Long l_value = mpref.getLong("data2", 0);

            startTimeInMillis = l_value;
            Log.i(TAG , "onResume() mpref l_value = " + l_value.toString());
            if (l_value != 0) {
                customHandler.removeCallbacks(updateTimerThread);
                customHandler.postDelayed(updateTimerThread, 1000); // delay 1 second
            }
        } catch (Exception e) {
            Log.e(TAG,"onResume() error msg " + e.getMessage());
            customHandler.removeCallbacks(updateTimerThread);
        }
    }

    private ArrayList<Model> getModel(){
        ArrayList<Model> list = new ArrayList<>();

        for(int i = 0; i < NumberOfTargets; i++){

            Model model = new Model();
            model.setTargetScore(0);
            model.setTargetName("Target Number " + (i + 1));
            model.setElapsedTime("-1");
            list.add(model);
        }
        return list;
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            Long eTime;

            eTime = calcTimeDiff();
            int secs = (int) (eTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
//            Log.i(TAG, "Elapsed time " + String.format("%03d", mins) + ":"
//                    + String.format("%02d", secs));
            String value = String.format("%03d", mins) + ":" + String.format("%02d", secs);
            timerValue.setText(value);
            customHandler.postDelayed(this, 1000);
        }
    };
}

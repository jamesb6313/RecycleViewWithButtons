package com.titansArchery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    //private TextView tvScoreSheet;
    private WebView wv_ScoreSheet;
    private String html, path, fn;
    private int scoreSheetLoop = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        wv_ScoreSheet = (WebView) findViewById(R.id.wv_ScoreSheet);
        int targetCount = MainActivity.modelArrayList.size();   //NumberOfTargets = 24

        int totalShots = 0;
        final StringBuilder sb_HTML = new StringBuilder();

        //Create Header of ScoreSheet which will be a table with 12 rows (2 targets across rows) and 6 columns
        //therefore hard code loop with 12 and not targetCount
        html = "<table border='1' bordercolor='green'><tr><b><th>Target</th><th>Score</th><th>Time</th><th>Target</th><th>Score</th><th>Time</th></b></tr>";
        sb_HTML.append(html);
        sb_HTML.append("<tr>");

        //hard code loop with 12 and not targetCount
        for (int i = 0; i < scoreSheetLoop; i++) {

            String tNum1, time1, tNum2, time2, tempStr;
            int score1, score2;

            tNum1 = Integer.toString(i+1);  //MainActivity.modelArrayList.get(i).getTargetName();
            score1 = MainActivity.modelArrayList.get(i).getTargetScore();
            tNum2 = Integer.toString(i+13); //MainActivity.modelArrayList.get(i + 12).getTargetName();
            score2 = MainActivity.modelArrayList.get(i + 12).getTargetScore();


            time1 = MainActivity.modelArrayList.get(i).getElapsedTime();
            if (!time1.equals("-1")) {
                try {
                    int colonPos = time1.indexOf(':');
                    String time1Min = time1.substring(0, colonPos);
                    int t1 = Integer.parseInt(time1Min);
                    time1 = Integer.toString(t1);
                } catch (Exception e) {
                    Log.e("MyInfo", "onCreate() ERROR msg: " + e.getMessage() + ", time1 = " + time1);
                }
            }

            time2 = MainActivity.modelArrayList.get(i + 12).getElapsedTime();
            if (!time2.equals("-1")) {
                try {
                    int colonPos = time2.indexOf(':');
                    String time2Min = time2.substring(0, colonPos);
                    int t2 = Integer.parseInt(time2Min);
                    time2 = Integer.toString(t2);
                } catch (Exception e) {
                    Log.e("MyInfo", "onCreate() ERROR msg: " + e.getMessage() + ", time2 = " + time2);
                }
            }


            //First three columns of the score sheet row
            tempStr = "<td>" + tNum1 + "</td>";
            sb_HTML.append(tempStr);
            tempStr = "<td>" + score1 + "</td>";
            sb_HTML.append(tempStr);
            tempStr = "<td>" + time1 + "</td>";
            sb_HTML.append(tempStr);

            //Last three columns of score sheet row
            tempStr = "<td>" + tNum2 + "</td>";
            sb_HTML.append(tempStr);
            tempStr = "<td>" + score2 + "</td>";
            sb_HTML.append(tempStr);
            tempStr = "<td>" + time2 + "</td>";
            sb_HTML.append(tempStr);
            sb_HTML.append("</tr>");

            totalShots += (score1 + score2);
        }

        sb_HTML.append("</table></br>Shots = ");
        sb_HTML.append(totalShots);
        sb_HTML.append(", Minutes = ");

        //String totalTime = MainActivity.timerValue.getText().toString();
        String totalMinutes = "0";
/*        try {
            int colonPos = totalTime.indexOf(':');
            totalMinutes = totalTime.substring(0, colonPos);
            int tt = Integer.parseInt(totalMinutes);
            totalMinutes = Integer.toString(tt);
        } catch (Exception e) {
            Log.e("MyInfo", "onCreate() ERROE msg: " + e.getMessage() + ", time2 = " + totalTime);
        }*/

        sb_HTML.append(totalMinutes);
        int mins = 0;
        try {
            mins = Integer.parseInt(totalMinutes);
        }
        catch(NumberFormatException nfe) {
            // Handle parse error.
            Log.e("MyINFO", "onCreater() score sheet processing Error msg " + nfe.getMessage());
        }

        int tScore = mins + totalShots;
        String tempstr = ", Score = " + tScore;
        sb_HTML.append(tempstr);

        html = sb_HTML.toString();

        wv_ScoreSheet.loadData(html , "text/html; charset=UTF-8", null);
    }
}

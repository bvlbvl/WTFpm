package de.belau_online.wtfpm;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private boolean button_toggleStarted = false;
    private Chronometer chronometer;
    private long timeWhenStopped = 0;
    private int numWTF = 0;
    private double numWTFps = 0;
    double elapsedMins = 0;
    Button button_toggleStartStop;
    Button button_reset;
    Button button_addWTF;
    TextView textView_numWTF;
    TextView textView_WTFpm;
    TextView textView_timeElapsed;
    String [] wtfList = {"WTF!", "WTF is this shit", "Dude, WTF?", "OMG!","What the..?"};
    String currentWTFText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = (Chronometer) findViewById(R.id.chronometer_timeElapsed);


        textView_numWTF = (TextView) findViewById(R.id.textView_numWTFs);
        textView_WTFpm = (TextView) findViewById(R.id.textView_WTFpm);
        textView_timeElapsed = (TextView) findViewById(R.id.textView_time_elapsed);
        currentWTFText =  getString(R.string.wtf);

        //react to start
        button_toggleStartStop = (Button) findViewById(R.id.button_toggleStartStop);
        button_toggleStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button_toggleStarted) {
                    showMyToast("chron stop");
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    button_toggleStartStop.setText(getString(R.string.start));
                    button_toggleStarted = false;
                }
                else {
                    showMyToast("chron start");
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();
                    button_toggleStartStop.setText(getString(R.string.pause));
                    button_toggleStarted = true;

                }
            }
        });

        button_reset = (Button) findViewById(R.id.button_reset);
        button_reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reset();
            }
        });

        button_addWTF = (Button)findViewById(R.id.button_addWTF);
        button_addWTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numWTF++;
                calcAndSetWTFps();
                currentWTFText = wtfList[new Random().nextInt(wtfList.length)];
                updateDisplay();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                calcAndSetWTFps();
            }
        } );

    }

    void showMyToast(CharSequence _text){
        Context context = getApplicationContext();
        CharSequence text = _text;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    void reset(){
        showMyToast("reset");
        chronometer.stop();
        timeWhenStopped = 0;
        chronometer.setBase(SystemClock.elapsedRealtime());
        numWTF=0;
        numWTFps = 0;
        button_toggleStarted = false;
        elapsedMins = 0;
        updateDisplay();
    }



    void calcAndSetWTFps() {
        String chronoText = chronometer.getText().toString();
        int fullMins = 0;
        int fullSecs = 0;
        int fullHours = 0;
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            fullMins = Integer.parseInt(array[0]);
            fullSecs = Integer.parseInt(array[1]);
        } else if (array.length == 3) {
            fullHours = Integer.parseInt(array[0]);
            fullMins =  Integer.parseInt(array[1]);
            fullSecs = Integer.parseInt(array[2]);
        }
        elapsedMins = 60*fullHours + fullMins + (double)fullSecs/60;
        if (elapsedMins == 0) {
            numWTFps = 0;
        }
        else {
            numWTFps = numWTF / elapsedMins;
        }
        updateDisplay();
    }

    void updateDisplay(){
        textView_numWTF.setText(String.valueOf(numWTF));
        textView_WTFpm.setText(String.format("%.1f WTFs/min", numWTFps));
        textView_timeElapsed.setText(String.format("%.1f min", elapsedMins));
        String buttonText = (button_toggleStarted?(getString(R.string.pause)):getString(R.string.start));
        button_toggleStartStop.setText(buttonText);
        button_addWTF.setText(currentWTFText);

    }

}


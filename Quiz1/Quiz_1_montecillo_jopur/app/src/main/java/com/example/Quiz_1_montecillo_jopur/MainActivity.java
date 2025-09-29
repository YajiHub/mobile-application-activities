package com.example.Quiz_1_montecillo_jopur;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView top;
    ImageView bottom;

    Button dynamic_color_button;
    public Timer timer;
    public TimerTask task;
    boolean is_on = false;
    int[][] combi;
    int current_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout layout = ((ConstraintLayout)findViewById(R.id.layout));

        combi = new int[][]{
                {Color.RED,Color.GREEN},
                {Color.YELLOW, Color.YELLOW},
                {Color.GREEN, Color.RED},
                {Color.YELLOW, Color.YELLOW},
                {Color.RED, Color.GREEN}
        };

        top = (ImageView) findViewById(R.id.top);
        top.setBackgroundColor(combi[0][0]);
        bottom = (ImageView) findViewById(R.id.bottom);
        bottom.setBackgroundColor(combi[0][1]);
        current_index = 0;





        dynamic_color_button = findViewById(R.id.dynamicButton);
        dynamic_color_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_on = !is_on;
                if (is_on) {
                    dynamic_color_button.setText(R.string.stop);
                    startTimer();
                } else {
                    dynamic_color_button.setText(R.string.start);
                    stopTimer();
                }
            }
        });

        myButtonListenerMethod();
    }



    public void startTimer(){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if(current_index == 0){ // at red green
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(combi[current_index+1][0]);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(combi[current_index+1][1]);
                    current_index++;
                }else if(current_index == 1){ // at yellow yellow
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(combi[current_index+1][0]);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(combi[current_index+1][1]);
                    current_index++;
                }else if(current_index == 2){ // at green red
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(combi[current_index+1][0]);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(combi[current_index+1][1]);
                    current_index++;
                } else if (current_index == 3){ // at yellow yellow
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(combi[current_index+1][0]);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(combi[current_index+1][1]);
                    current_index++;
                } else if(current_index == 4){ // at red green
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(Color.YELLOW);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(Color.YELLOW);
                    current_index=1;
                }
            //closing method
            }
        };
        timer.schedule(task, 0,1000);
    }


    public void stopTimer(){
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public void myButtonListenerMethod() {
        Button button = (Button) findViewById(R.id.changeButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_index == 0){ // at red green
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(combi[current_index+1][0]);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(combi[current_index+1][1]);
                    current_index++;
                }else if(current_index == 1){ // at yellow yellow
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(combi[current_index+1][0]);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(combi[current_index+1][1]);
                    current_index++;
                }else if(current_index == 2){ // at green red
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(combi[current_index+1][0]);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(combi[current_index+1][1]);
                    current_index++;
                } else if (current_index == 3){ // at yellow yellow
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(combi[current_index+1][0]);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(combi[current_index+1][1]);
                    current_index++;
                } else if(current_index == 4){ // at red green
                    top = (ImageView) findViewById(R.id.top);
                    top.setBackgroundColor(Color.YELLOW);
                    bottom = (ImageView) findViewById(R.id.bottom);
                    bottom.setBackgroundColor(Color.YELLOW);
                    current_index=1;
                }
            //closing of method
            }
        });


    }

}
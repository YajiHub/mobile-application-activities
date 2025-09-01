package com.example.redbluelight;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public Timer timer;
    public TimerTask task;
    boolean is_on = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout layout = ((ConstraintLayout)findViewById(R.id.layout));
        layout.setBackgroundColor(getResources().getColor(R.color.red));

        Button dynamic_color_button = findViewById(R.id.dynamicButton);
        dynamic_color_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_on = !is_on;
                if (is_on) {
                    startTimer();
                } else {
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
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                int color = ((ColorDrawable)
                        layout.getBackground()).getColor();

                if(color == Color.RED){
                    layout.setBackgroundColor(Color.BLUE);
                }else if(color == Color.BLUE){
                    layout.setBackgroundColor(Color.WHITE);
                }else{
                    layout.setBackgroundColor(Color.RED);
                }
            }
        };
        timer.schedule(task, 0, 1000);
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


    public void myButtonListenerMethod(){
        Button button = (Button) findViewById(R.id.changeButton);

        try{
            button.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
                            int color = ((ColorDrawable)
                                    layout.getBackground()).getColor();
                            if (color == Color.RED) {
                                layout.setBackgroundColor(Color.BLUE);
                            }
                            else {
                                layout.setBackgroundColor(Color.RED);
                            }

                        }
                    }
            );
        }catch (Exception e){
            Log.d("reason", e.getMessage());
        }

    }

}
package com.example.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.PorterDuff;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView dice1;
    ImageView dice2;
    TextView text1;
    TextView text2;
    private int[] frames;
    private int[] colors;
    private Handler animation_handler;
    int current_frame = 0;
    Runnable runnable;

    int dice1_num;
    int dice2_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dice1 = (ImageView) findViewById(R.id.diceImage);
        dice2 = (ImageView) findViewById(R.id.diceImage1);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);

        frames = new int[] {
                R.drawable.dice1,
                R.drawable.dice2,
                R.drawable.dice3,
                R.drawable.dice4,
                R.drawable.dice5,
                R.drawable.dice6
        };

        colors = new int[] {
                Color.BLACK,
                Color.RED,
                Color.BLUE,
                Color.GRAY,
                Color.CYAN,
                Color.WHITE
        };

        roll_dice();
    }

    public void dice_animation() {
        dice1.setImageResource(frames[current_frame]);
        dice2.setImageResource(frames[current_frame]);

        int color_index = current_frame % colors.length;
        dice1.setBackgroundColor(colors[color_index]);
        dice2.setBackgroundColor(colors[(color_index + 1) % colors.length]);

        current_frame = (current_frame + 1) % frames.length;
        animation_handler.postDelayed(runnable, 100);
    }

    public void destroy_animation() {
        animation_handler.removeCallbacks(runnable);
    }

    public void show_final_result() {
        text1.setText(Integer.toString(dice1_num));
        text2.setText(Integer.toString(dice2_num));
        text1.setVisibility(View.VISIBLE);
        text2.setVisibility(View.VISIBLE);

        dice1.setColorFilter(null);
        dice2.setColorFilter(null);

        switch (dice1_num) {
            case 1: dice1.setImageResource(R.drawable.dice1); break;
            case 2: dice1.setImageResource(R.drawable.dice2); break;
            case 3: dice1.setImageResource(R.drawable.dice3); break;
            case 4: dice1.setImageResource(R.drawable.dice4); break;
            case 5: dice1.setImageResource(R.drawable.dice5); break;
            case 6: dice1.setImageResource(R.drawable.dice6); break;
        }
        switch (dice2_num) {
            case 1: dice2.setImageResource(R.drawable.dice1); break;
            case 2: dice2.setImageResource(R.drawable.dice2); break;
            case 3: dice2.setImageResource(R.drawable.dice3); break;
            case 4: dice2.setImageResource(R.drawable.dice4); break;
            case 5: dice2.setImageResource(R.drawable.dice5); break;
            case 6: dice2.setImageResource(R.drawable.dice6); break;
        }
    }

    public void roll_dice() {
        Button button = findViewById(R.id.rollButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation_handler = new Handler();
                current_frame = 0;
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        dice_animation();
                    }
                };
                animation_handler.post(runnable);

                Random random = new Random();
                dice1_num = random.nextInt(6) + 1;
                dice2_num = random.nextInt(6) + 1;

                text1.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.INVISIBLE);
                TextView tap_text = findViewById(R.id.text);
                tap_text.setVisibility(View.INVISIBLE);

                Timer timer1 = new Timer();
                TimerTask task1 = new TimerTask() {
                    @Override
                    public void run() {
                        destroy_animation();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show_final_result();
                            }
                        });
                    }
                };
                timer1.schedule(task1, 2000);
            }
        });
    }
}

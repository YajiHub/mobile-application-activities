package com.example.Quiz_2_montecillo_jopur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    String main_selected;
    String dice_selected;
    Boolean is_animation_running = false;

    ImageView dice1;
    ImageView dice2;
    ImageView dice3;
    ImageView dice4;
    ImageView diceCenter;
    private int[] frames;
    private int[] colors;
    private Handler animation_handler;
    int current_frame = 0;
    Runnable runnable;
    Spinner main_spinner;
    Spinner dice_spinner;

    int dice1_num;
    int dice2_num;
    int dice3_num;
    int dice4_num;
    int diceCenter_num;

    int current_index = 0;

    TextView dice1_text;
    TextView dice2_text;
    TextView dice3_text;
    TextView dice4_text;
    TextView diceCenter_text;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_spinner = (Spinner) findViewById(R.id.main_spinner);
        dice_spinner = (Spinner) findViewById(R.id.dice_spinner);

        ArrayAdapter<CharSequence> main_adapter = ArrayAdapter.createFromResource(this,
                R.array.main_spinner, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> dice_adapter = ArrayAdapter.createFromResource(this,
                R.array.dice_spinner, android.R.layout.simple_spinner_item);

        main_spinner.setAdapter(main_adapter);
        dice_spinner.setAdapter(dice_adapter);
        dice_spinner.setSelection(0);

        dice1_text = findViewById(R.id.dice1_text);
        dice2_text = findViewById(R.id.dice2_text);
        dice3_text = findViewById(R.id.dice3_text);
        dice4_text = findViewById(R.id.dice4_text);
        diceCenter_text = findViewById(R.id.diceCenter_text);

        dice1_text.setVisibility(View.INVISIBLE);
        dice2_text.setVisibility(View.INVISIBLE);
        dice3_text.setVisibility(View.INVISIBLE);
        dice4_text.setVisibility(View.INVISIBLE);
        diceCenter_text.setVisibility(View.INVISIBLE);



        main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                main_selected = parent.getItemAtPosition(position).toString();
                main_spinner_selection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dice_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dice_selected = parent.getItemAtPosition(position).toString();
                dice_spinner_selection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dice1 = (ImageView) findViewById(R.id.diceImage);
        dice2 = (ImageView) findViewById(R.id.diceImage1);
        dice3 = (ImageView) findViewById(R.id.diceImage2);
        dice4 = (ImageView) findViewById(R.id.diceImage3);
        diceCenter = (ImageView) findViewById(R.id.diceImage4);


        dice1.setVisibility(View.INVISIBLE);
        dice2.setVisibility(View.INVISIBLE);
        dice3.setVisibility(View.INVISIBLE);
        dice4.setVisibility(View.INVISIBLE);
        diceCenter.setVisibility(View.VISIBLE);



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

    public void main_spinner_selection(){
        if(current_index == 0){
            current_index++;
        }else{
//            Toast.makeText(getApplicationContext(),main_selected, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, bmi.class);
            startActivity(intent);
        }

    }

    public void dice_spinner_selection(){
        dice1_text.setVisibility(View.INVISIBLE);
        dice2_text.setVisibility(View.INVISIBLE);
        dice3_text.setVisibility(View.INVISIBLE);
        dice4_text.setVisibility(View.INVISIBLE);
        diceCenter_text.setVisibility(View.INVISIBLE);
        TextView total = (TextView) findViewById(R.id.total);
        String text = "Total: ";
        total.setText(text);
        if(is_animation_running){
            destroy_animation();
        }
        dice1.setImageResource(R.drawable.dicegeneral);
        dice2.setImageResource(R.drawable.dicegeneral);
        dice3.setImageResource(R.drawable.dicegeneral);
        dice4.setImageResource(R.drawable.dicegeneral);
        diceCenter.setImageResource(R.drawable.dicegeneral);
        dice1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        dice2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        dice3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        dice4.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        diceCenter.setBackgroundColor(getResources().getColor(android.R.color.transparent));

//        Toast.makeText(getApplicationContext(),dice_selected, Toast.LENGTH_SHORT).show();

        switch (dice_selected){
            case "1":
                dice1.setVisibility(View.INVISIBLE);
                dice2.setVisibility(View.INVISIBLE);
                dice3.setVisibility(View.INVISIBLE);
                dice4.setVisibility(View.INVISIBLE);
                diceCenter.setVisibility(View.VISIBLE);
                break;
            case "2":
                dice1.setVisibility(View.INVISIBLE);
                dice2.setVisibility(View.INVISIBLE);
                dice3.setVisibility(View.VISIBLE);
                dice4.setVisibility(View.VISIBLE);
                diceCenter.setVisibility(View.INVISIBLE);
                break;
            case "3":
                dice1.setVisibility(View.VISIBLE);
                dice2.setVisibility(View.VISIBLE);
                dice3.setVisibility(View.INVISIBLE);
                dice4.setVisibility(View.INVISIBLE);
                diceCenter.setVisibility(View.VISIBLE);
                break;
            case "4":
                dice1.setVisibility(View.VISIBLE);
                dice2.setVisibility(View.VISIBLE);
                dice3.setVisibility(View.VISIBLE);
                dice4.setVisibility(View.VISIBLE);
                diceCenter.setVisibility(View.INVISIBLE);
                break;
        }


    }

    public void dice_animation() {
        is_animation_running = true;
        dice1.setImageResource(frames[current_frame]);
        dice2.setImageResource(frames[current_frame]);
        dice3.setImageResource(frames[current_frame]);
        dice4.setImageResource(frames[current_frame]);
        diceCenter.setImageResource(frames[current_frame]);

        int color_index = current_frame % colors.length;
        dice1.setBackgroundColor(colors[color_index]);
        dice2.setBackgroundColor(colors[(color_index + 1) % colors.length]);
        dice3.setBackgroundColor(colors[(color_index + 2) % colors.length]);
        dice4.setBackgroundColor(colors[(color_index + 3) % colors.length]);
        diceCenter.setBackgroundColor(colors[(color_index + 4) % colors.length]);

        current_frame = (current_frame + 1) % frames.length;
        animation_handler.postDelayed(runnable, 100);
    }

    public void destroy_animation() {
        animation_handler.removeCallbacks(runnable);
        is_animation_running = false;
    }

    public void show_final_result() {
        TextView total = (TextView) findViewById(R.id.total);
        String sum_text = "" + total.getText();
        int total_num;
        dice1.setColorFilter(null);
        dice2.setColorFilter(null);
        dice3.setColorFilter(null);
        dice4.setColorFilter(null);
        diceCenter.setColorFilter(null);

        switch (dice_selected){
            case "1":
                switch (diceCenter_num) {
                    case 1: diceCenter.setImageResource(R.drawable.dice1); break;
                    case 2: diceCenter.setImageResource(R.drawable.dice2); break;
                    case 3: diceCenter.setImageResource(R.drawable.dice3); break;
                    case 4: diceCenter.setImageResource(R.drawable.dice4); break;
                    case 5: diceCenter.setImageResource(R.drawable.dice5); break;
                    case 6: diceCenter.setImageResource(R.drawable.dice6); break;
                }
                sum_text += diceCenter_num;
                diceCenter_text.setText("" + diceCenter_num);
                diceCenter_text.setVisibility(View.VISIBLE);
                break;
            case "2":
                switch (dice3_num) {
                    case 1: dice3.setImageResource(R.drawable.dice1); break;
                    case 2: dice3.setImageResource(R.drawable.dice2); break;
                    case 3: dice3.setImageResource(R.drawable.dice3); break;
                    case 4: dice3.setImageResource(R.drawable.dice4); break;
                    case 5: dice3.setImageResource(R.drawable.dice5); break;
                    case 6: dice3.setImageResource(R.drawable.dice6); break;
                }
                switch (dice4_num) {
                    case 1: dice4.setImageResource(R.drawable.dice1); break;
                    case 2: dice4.setImageResource(R.drawable.dice2); break;
                    case 3: dice4.setImageResource(R.drawable.dice3); break;
                    case 4: dice4.setImageResource(R.drawable.dice4); break;
                    case 5: dice4.setImageResource(R.drawable.dice5); break;
                    case 6: dice4.setImageResource(R.drawable.dice6); break;
                }
                total_num = dice3_num + dice4_num;
                sum_text += total_num;
                dice3_text.setText("" + dice3_num);
                dice3_text.setVisibility(View.VISIBLE);
                dice4_text.setText("" + dice4_num);
                dice4_text.setVisibility(View.VISIBLE);
                break;
            case "3":
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
                switch (diceCenter_num) {
                    case 1: diceCenter.setImageResource(R.drawable.dice1); break;
                    case 2: diceCenter.setImageResource(R.drawable.dice2); break;
                    case 3: diceCenter.setImageResource(R.drawable.dice3); break;
                    case 4: diceCenter.setImageResource(R.drawable.dice4); break;
                    case 5: diceCenter.setImageResource(R.drawable.dice5); break;
                    case 6: diceCenter.setImageResource(R.drawable.dice6); break;
                }
                total_num = dice1_num + dice2_num + diceCenter_num;
                sum_text += total_num;
                dice1_text.setText("" + dice1_num);
                dice1_text.setVisibility(View.VISIBLE);
                dice2_text.setText("" + dice2_num);
                dice2_text.setVisibility(View.VISIBLE);
                diceCenter_text.setText("" + diceCenter_num);
                diceCenter_text.setVisibility(View.VISIBLE);
                break;
            case "4":
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
                switch (dice3_num) {
                    case 1: dice3.setImageResource(R.drawable.dice1); break;
                    case 2: dice3.setImageResource(R.drawable.dice2); break;
                    case 3: dice3.setImageResource(R.drawable.dice3); break;
                    case 4: dice3.setImageResource(R.drawable.dice4); break;
                    case 5: dice3.setImageResource(R.drawable.dice5); break;
                    case 6: dice3.setImageResource(R.drawable.dice6); break;
                }
                switch (dice4_num) {
                    case 1: dice4.setImageResource(R.drawable.dice1); break;
                    case 2: dice4.setImageResource(R.drawable.dice2); break;
                    case 3: dice4.setImageResource(R.drawable.dice3); break;
                    case 4: dice4.setImageResource(R.drawable.dice4); break;
                    case 5: dice4.setImageResource(R.drawable.dice5); break;
                    case 6: dice4.setImageResource(R.drawable.dice6); break;
                }
                total_num = dice1_num + dice2_num + dice3_num + dice4_num;
                sum_text += total_num;
                dice1_text.setText("" + dice1_num);
                dice1_text.setVisibility(View.VISIBLE);
                dice2_text.setText("" + dice2_num);
                dice2_text.setVisibility(View.VISIBLE);
                dice3_text.setText("" + dice3_num);
                dice3_text.setVisibility(View.VISIBLE);
                dice4_text.setText("" + dice4_num);
                dice4_text.setVisibility(View.VISIBLE);
                break;
        }

        total.setText(sum_text);
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
                        dice1_text.setVisibility(View.INVISIBLE);
                        dice2_text.setVisibility(View.INVISIBLE);
                        dice3_text.setVisibility(View.INVISIBLE);
                        dice4_text.setVisibility(View.INVISIBLE);
                        diceCenter_text.setVisibility(View.INVISIBLE);
                        TextView total = (TextView) findViewById(R.id.total);
                        String text = "Total: ";
                        total.setText(text);
                        dice_animation();
                    }
                };
                animation_handler.post(runnable);

                Random random = new Random();
                dice1_num = random.nextInt(6) + 1;
                dice2_num = random.nextInt(6) + 1;
                dice3_num = random.nextInt(6) + 1;
                dice4_num = random.nextInt(6) + 1;
                diceCenter_num = random.nextInt(6) + 1;

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

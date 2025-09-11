package com.example.diceroller;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView dice1;
    ImageView dice2;
    TextView text1;
    TextView text2;

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


        roll_dice();
    }

    public void roll_dice(){
        Button button = findViewById(R.id.rollButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int dice1_num;
                int dice2_num;

                Random random = new Random();
                dice1_num = random.nextInt(6) + 1;
                dice2_num = random.nextInt(6) + 1;

                ImageView dice_image = dice1;

                text1.setText(Integer.toString(dice1_num));
                text2.setText(Integer.toString(dice2_num));
                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);

                TextView tap_text = findViewById(R.id.text);
                tap_text.setVisibility(View.INVISIBLE);

                switch (dice1_num) {
                    case 1:
                        dice_image.setImageResource(R.drawable.dice1);
                        break;
                    case 2:
                        dice_image.setImageResource(R.drawable.dice2);
                        break;
                    case 3:
                        dice_image.setImageResource(R.drawable.dice3);
                        break;
                    case 4:
                        dice_image.setImageResource(R.drawable.dice4);
                        break;
                    case 5:
                        dice_image.setImageResource(R.drawable.dice5);
                        break;
                    case 6:
                        dice_image.setImageResource(R.drawable.dice6);
                        break;

                }

                switch (dice2_num) {
                    case 1:
                        dice2.setImageResource(R.drawable.dice1);
                        break;
                    case 2:
                        dice2.setImageResource(R.drawable.dice2);
                        break;
                    case 3:
                        dice2.setImageResource(R.drawable.dice3);
                        break;
                    case 4:
                        dice2.setImageResource(R.drawable.dice4);
                        break;
                    case 5:
                        dice2.setImageResource(R.drawable.dice5);
                        break;
                    case 6:
                        dice2.setImageResource(R.drawable.dice6);
                        break;

                }
            }
        });
    }
}
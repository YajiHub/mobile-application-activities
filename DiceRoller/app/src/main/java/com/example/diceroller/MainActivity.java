package com.example.diceroller;

import androidx.appcompat.app.AppCompatActivity;


import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roll_dice();
    }

    public void roll_dice(){
        Button button = findViewById(R.id.rollButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int num = random.nextInt(6) + 1;
                ImageView dice_image = (ImageView) findViewById(R.id.diceImage);
                TextView diceResult = (TextView) findViewById(R.id.text);
                diceResult.setText(Integer.toString(num));

                switch (num) {
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
            }
        });
    }
}
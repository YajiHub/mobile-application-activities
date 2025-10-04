package com.example.Midterm_Practical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BaseNumber extends AppCompatActivity {
    String main_selected;

    Spinner main_spinner;

    int current_index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_number);

        main_spinner = (Spinner) findViewById(R.id.main_spinner);

        ArrayAdapter<CharSequence> main_adapter = ArrayAdapter.createFromResource(this,
                R.array.main_spinner, android.R.layout.simple_spinner_item);

        main_spinner.setAdapter(main_adapter);


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


    }


    public void main_spinner_selection(){
        if(current_index == 0){
            current_index++;
        }else{
            switch(main_selected){
                case "Basic Calculator":
                    Intent intent = new Intent(BaseNumber.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case "Base Number Calculator":
                    break;
                case "Unit Converter":
                    Intent unit = new Intent(BaseNumber.this, UnitConverter.class);
                    startActivity(unit);
                    finish();
                    break;
            }
        }

    }

}
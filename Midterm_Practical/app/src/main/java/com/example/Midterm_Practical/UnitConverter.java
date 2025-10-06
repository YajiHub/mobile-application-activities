package com.example.Midterm_Practical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class UnitConverter extends AppCompatActivity {

    String main_selected;
    Spinner main_spinner;

    String converter_type = "";
    Spinner converter_spinner;

    String from_selected = "";
    Spinner from_spinner;

    String to_selected = "";
    Spinner to_spinner;

    int current_index = 0;
    int converter_index = 0;

    EditText input;
    EditText to_edit_text;
    ImageButton swapButton;

    double from;
    double result;

    // TextWatcher for live conversion
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            convert();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_converter);

        input = findViewById(R.id.editText);
        input.setEnabled(true);
        to_edit_text = findViewById(R.id.to_edit_text);
        to_edit_text.setEnabled(false);
        to_edit_text.setText("");
        swapButton = findViewById(R.id.swap_button);

        // Main spinner for navigation
        main_spinner = findViewById(R.id.main_spinner);
        ArrayAdapter<CharSequence> main_adapter = ArrayAdapter.createFromResource(this,
                R.array.main_spinner, android.R.layout.simple_spinner_item);
        main_spinner.setAdapter(main_adapter);
        main_spinner.setSelection(2);

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

        // Converter type spinner
        converter_spinner = findViewById(R.id.converter_spinner);
        ArrayAdapter<CharSequence> converter_adapter = ArrayAdapter.createFromResource(this,
                R.array.converter_types, android.R.layout.simple_spinner_item);
        converter_spinner.setAdapter(converter_adapter);

        converter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                converter_type = parent.getItemAtPosition(position).toString();
                updateConverterSpinners();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set initial selection and trigger the spinner update
        converter_spinner.setSelection(0);

        // From spinner
        from_spinner = findViewById(R.id.from_spinner);
        from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from_selected = parent.getItemAtPosition(position).toString();
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // To spinner
        to_spinner = findViewById(R.id.to_spinner);
        to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to_selected = parent.getItemAtPosition(position).toString();
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Swap button
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapUnits();
            }
        });

        input.addTextChangedListener(textWatcher);
    }

    private void updateConverterSpinners() {
        int arrayResource = 0;

        switch (converter_type) {
            case "Temperature":
                arrayResource = R.array.temperature_units;
                break;
            case "Currency":
                arrayResource = R.array.currency_units;
                break;
            case "Weight":
                arrayResource = R.array.weight_units;
                break;
            default:
                return; // Exit if converter type not recognized
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResource, android.R.layout.simple_spinner_item);

        from_spinner.setAdapter(adapter);
        to_spinner.setAdapter(adapter);

        // Only clear inputs if not the first load
        if (converter_index > 0) {
            input.setText("");
            to_edit_text.setText("");
        }
        converter_index++;

        // Set default selections
        from_spinner.setSelection(0);
        to_spinner.setSelection(0);
    }

    private void swapUnits() {
        int fromPosition = from_spinner.getSelectedItemPosition();
        int toPosition = to_spinner.getSelectedItemPosition();

        String inputValue = input.getText().toString();
        String outputValue = to_edit_text.getText().toString();

        // Swap spinner selections
        from_spinner.setSelection(toPosition);
        to_spinner.setSelection(fromPosition);

        // Swap input values
        input.setText(outputValue);
    }

    public void main_spinner_selection() {
        if (current_index == 0) {
            current_index++;
        } else {
            switch (main_selected) {
                case "Basic Calculator":
                    Intent intent = new Intent(UnitConverter.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case "Base Number Calculator":
                    Intent base = new Intent(UnitConverter.this, BaseNumber.class);
                    startActivity(base);
                    finish();
                    break;
                case "Unit Converter":
                    break;
            }
        }
    }

    public void convert() {
        String from_text = input.getText().toString();
        if (from_text.isEmpty() || from_text.equals("-")) {
            to_edit_text.setText("");
            return;
        }

        // Check if converter type and units are selected
        if (converter_type == null || converter_type.isEmpty() ||
                from_selected == null || from_selected.isEmpty() ||
                to_selected == null || to_selected.isEmpty()) {
            to_edit_text.setText("");
            return;
        }

        try {
            from = Double.parseDouble(input.getText().toString());

            // Validate input based on converter type
            if (converter_type.equals("Weight") || converter_type.equals("Currency")) {
                if (from < 0) {
                    to_edit_text.setText("Invalid");
                    toastMsg("Negative values not allowed for " + converter_type);
                    return;
                }
            }

            switch (converter_type) {
                case "Temperature":
                    convertTemperature();
                    break;
                case "Currency":
                    convertCurrency();
                    break;
                case "Weight":
                    convertWeight();
                    break;
                default:
                    to_edit_text.setText("");
                    return;
            }

            String string_result = formatResult(result);
            to_edit_text.setText(string_result);

        } catch (NumberFormatException e) {
            to_edit_text.setText("");
        }
    }

    private void convertTemperature() {
        if (from_selected == null || to_selected == null) return;

        switch (from_selected) {
            case "Celsius":
                switch (to_selected) {
                    case "Kelvin":
                        result = from + 273.15;
                        break;
                    case "Fahrenheit":
                        result = (from * 9 / 5) + 32;
                        break;
                    case "Celsius":
                        result = from;
                        break;
                }
                break;
            case "Kelvin":
                switch (to_selected) {
                    case "Celsius":
                        result = from - 273.15;
                        break;
                    case "Fahrenheit":
                        result = (from * 1.8) - 459.67;
                        break;
                    case "Kelvin":
                        result = from;
                        break;
                }
                break;
            case "Fahrenheit":
                switch (to_selected) {
                    case "Celsius":
                        result = ((5 * (from - 32.0)) / 9.0);
                        break;
                    case "Kelvin":
                        result = 273.5 + ((from - 32.0) * (5.0 / 9.0));
                        break;
                    case "Fahrenheit":
                        result = from;
                        break;
                }
                break;
        }
    }

    private void convertCurrency() {
        if (from_selected == null || to_selected == null) return;

        // Simple conversion rates (as of example rates)
        // First convert to PHP (base), then to target currency
        double inPHP = 0;

        // Convert from source to PHP
        switch (from_selected) {
            case "PHP":
                inPHP = from;
                break;
            case "USD":
                inPHP = from * 56.0; // 1 USD = 56 PHP
                break;
            case "EUR":
                inPHP = from * 61.0; // 1 EUR = 61 PHP
                break;
            case "JPY":
                inPHP = from * 0.38; // 1 JPY = 0.38 PHP
                break;
            case "GBP":
                inPHP = from * 71.0; // 1 GBP = 71 PHP
                break;
        }

        // Convert from PHP to target
        switch (to_selected) {
            case "PHP":
                result = inPHP;
                break;
            case "USD":
                result = inPHP / 56.0;
                break;
            case "EUR":
                result = inPHP / 61.0;
                break;
            case "JPY":
                result = inPHP / 0.38;
                break;
            case "GBP":
                result = inPHP / 71.0;
                break;
        }
    }

    private void convertWeight() {
        if (from_selected == null || to_selected == null) return;

        // First convert to grams (base), then to target unit
        double inGrams = 0;

        // Convert from source to grams
        switch (from_selected) {
            case "Milligram (mg)":
                inGrams = from / 1000.0;
                break;
            case "Gram (g)":
                inGrams = from;
                break;
            case "Kilogram (kg)":
                inGrams = from * 1000.0;
                break;
            case "Pound (lb)":
                inGrams = from * 453.592;
                break;
            case "Ounce (oz)":
                inGrams = from * 28.3495;
                break;
        }

        // Convert from grams to target
        switch (to_selected) {
            case "Milligram (mg)":
                result = inGrams * 1000.0;
                break;
            case "Gram (g)":
                result = inGrams;
                break;
            case "Kilogram (kg)":
                result = inGrams / 1000.0;
                break;
            case "Pound (lb)":
                result = inGrams / 453.592;
                break;
            case "Ounce (oz)":
                result = inGrams / 28.3495;
                break;
        }
    }

    private String formatResult(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            // Show up to 4 decimal places, remove trailing zeros
            return String.format("%.4f", value).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }

    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
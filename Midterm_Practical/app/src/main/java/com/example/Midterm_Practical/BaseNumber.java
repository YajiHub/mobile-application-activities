package com.example.Midterm_Practical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BaseNumber extends AppCompatActivity {
    String main_selected;
    Spinner main_spinner;

    String from_selected;
    Spinner from_spinner;

    String to_selected;
    Spinner to_spinner;

    int current_index = 0;

    EditText inputNumber;
    TextView resultText;
    TextView allConversionsText;
    Button convertButton;
    Button showAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_number);

        // Initialize views
        inputNumber = findViewById(R.id.input_number);
        resultText = findViewById(R.id.result_text);
        allConversionsText = findViewById(R.id.all_conversions_text);
        convertButton = findViewById(R.id.convert_button);
        showAllButton = findViewById(R.id.show_all_button);

        // Main spinner
        main_spinner = findViewById(R.id.main_spinner);
        ArrayAdapter<CharSequence> main_adapter = ArrayAdapter.createFromResource(this,
                R.array.main_spinner, android.R.layout.simple_spinner_item);
        main_spinner.setAdapter(main_adapter);
        main_spinner.setSelection(1);

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

        // From spinner
        from_spinner = findViewById(R.id.from_spinner);
        ArrayAdapter<CharSequence> from_adapter = ArrayAdapter.createFromResource(this,
                R.array.base_spinner, android.R.layout.simple_spinner_item);
        from_spinner.setAdapter(from_adapter);
        from_spinner.setSelection(0);

        from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from_selected = parent.getItemAtPosition(position).toString();
                clearResults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // To spinner
        to_spinner = findViewById(R.id.to_spinner);
        ArrayAdapter<CharSequence> to_adapter = ArrayAdapter.createFromResource(this,
                R.array.base_spinner, android.R.layout.simple_spinner_item);
        to_spinner.setAdapter(to_adapter);
        to_spinner.setSelection(0);

        to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to_selected = parent.getItemAtPosition(position).toString();
                clearResults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Convert button
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertNumber();
            }
        });

        // Show all conversions button
        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllConversions();
            }
        });
    }

    private void convertNumber() {
        String input = inputNumber.getText().toString().trim();

        if (input.isEmpty()) {
            toastMsg("Please enter a number");
            return;
        }

        try {
            // Convert input to decimal first
            long decimalValue = convertToDecimal(input, from_selected);

            // Convert decimal to target base
            String result = convertFromDecimal(decimalValue, to_selected);

            resultText.setText("Result: " + result);
            allConversionsText.setText("");

        } catch (Exception e) {
            toastMsg("Invalid input for " + from_selected);
            resultText.setText("Error");
        }
    }

    private void showAllConversions() {
        String input = inputNumber.getText().toString().trim();

        if (input.isEmpty()) {
            toastMsg("Please enter a number");
            return;
        }

        try {
            // Convert input to decimal first
            long decimalValue = convertToDecimal(input, from_selected);

            StringBuilder allResults = new StringBuilder();
            allResults.append("All Conversions from ").append(from_selected).append(":\n\n");

            // Convert to all bases
            if (!from_selected.equals("Decimal")) {
                String decimal = convertFromDecimal(decimalValue, "Decimal");
                allResults.append("Decimal: ").append(decimal).append("\n\n");
            }

            if (!from_selected.equals("Binary")) {
                String binary = convertFromDecimal(decimalValue, "Binary");
                allResults.append("Binary: ").append(binary).append("\n\n");
            }

            if (!from_selected.equals("Octal")) {
                String octal = convertFromDecimal(decimalValue, "Octal");
                allResults.append("Octal: ").append(octal).append("\n\n");
            }

            if (!from_selected.equals("Hexadecimal")) {
                String hex = convertFromDecimal(decimalValue, "Hexadecimal");
                allResults.append("Hexadecimal: ").append(hex).append("\n\n");
            }

            allConversionsText.setText(allResults.toString());
            resultText.setText("");

        } catch (Exception e) {
            toastMsg("Invalid input for " + from_selected);
            allConversionsText.setText("Error");
        }
    }

    private long convertToDecimal(String input, String fromBase) {
        input = input.toUpperCase().trim();

        switch (fromBase) {
            case "Decimal":
                return Long.parseLong(input);

            case "Binary":
                // Validate binary input
                if (!input.matches("[01]+")) {
                    throw new NumberFormatException("Invalid binary number");
                }
                return binaryToDecimal(input);

            case "Octal":
                // Validate octal input
                if (!input.matches("[0-7]+")) {
                    throw new NumberFormatException("Invalid octal number");
                }
                return octalToDecimal(input);

            case "Hexadecimal":
                // Validate hexadecimal input
                if (!input.matches("[0-9A-F]+")) {
                    throw new NumberFormatException("Invalid hexadecimal number");
                }
                return hexToDecimal(input);

            default:
                return 0;
        }
    }

    private String convertFromDecimal(long decimal, String toBase) {
        switch (toBase) {
            case "Decimal":
                return String.valueOf(decimal);

            case "Binary":
                return decimalToBinary(decimal);

            case "Octal":
                return decimalToOctal(decimal);

            case "Hexadecimal":
                return decimalToHex(decimal);

            default:
                return "";
        }
    }

    // Binary to Decimal
    private long binaryToDecimal(String binary) {
        long decimal = 0;
        int power = 0;

        for (int i = binary.length() - 1; i >= 0; i--) {
            int digit = binary.charAt(i) - '0';
            decimal += digit * Math.pow(2, power);
            power++;
        }

        return decimal;
    }

    // Decimal to Binary
    private String decimalToBinary(long decimal) {
        if (decimal == 0) return "0";

        StringBuilder binary = new StringBuilder();

        while (decimal > 0) {
            binary.insert(0, decimal % 2);
            decimal = decimal / 2;
        }

        return binary.toString();
    }

    // Octal to Decimal
    private long octalToDecimal(String octal) {
        long decimal = 0;
        int power = 0;

        for (int i = octal.length() - 1; i >= 0; i--) {
            int digit = octal.charAt(i) - '0';
            decimal += digit * Math.pow(8, power);
            power++;
        }

        return decimal;
    }

    // Decimal to Octal
    private String decimalToOctal(long decimal) {
        if (decimal == 0) return "0";

        StringBuilder octal = new StringBuilder();

        while (decimal > 0) {
            octal.insert(0, decimal % 8);
            decimal = decimal / 8;
        }

        return octal.toString();
    }

    // Hexadecimal to Decimal
    private long hexToDecimal(String hex) {
        long decimal = 0;
        int power = 0;

        for (int i = hex.length() - 1; i >= 0; i--) {
            char c = hex.charAt(i);
            int digit;

            if (c >= '0' && c <= '9') {
                digit = c - '0';
            } else {
                digit = c - 'A' + 10;
            }

            decimal += digit * Math.pow(16, power);
            power++;
        }

        return decimal;
    }

    // Decimal to Hexadecimal
    private String decimalToHex(long decimal) {
        if (decimal == 0) return "0";

        StringBuilder hex = new StringBuilder();
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        while (decimal > 0) {
            int remainder = (int)(decimal % 16);
            hex.insert(0, hexChars[remainder]);
            decimal = decimal / 16;
        }

        return hex.toString();
    }

    private void clearResults() {
        resultText.setText("");
        allConversionsText.setText("");
    }

    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void main_spinner_selection() {
        if (current_index == 0) {
            current_index++;
        } else {
            switch (main_selected) {
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
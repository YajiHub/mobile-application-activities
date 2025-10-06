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

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    String main_selected;
    Spinner main_spinner;

    private EditText displayText;
    private TextView equationText;
    private Button addButton, subtractButton, multiplyButton, divideButton, equalButton, clearButton, deleteButton;
    private Button num1Button, num2Button, num3Button, num4Button;
    private Button num5Button, num6Button, num7Button, num8Button, num9Button, zeroButton, dotButton;
    private Button leftParenButton, rightParenButton, negativeButton;

    private String currentEquation = "0";
    private boolean lastWasOperator = false;
    private boolean lastWasEquals = false;

    int current_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_spinner = findViewById(R.id.main_spinner);

        ArrayAdapter<CharSequence> main_adapter = ArrayAdapter.createFromResource(this,
                R.array.main_spinner, android.R.layout.simple_spinner_item);

        main_spinner.setAdapter(main_adapter);

        displayText = findViewById(R.id.editText);
        equationText = findViewById(R.id.equation_text);
        clearButton = findViewById(R.id.clear_text);

        addButton = findViewById(R.id.add);
        subtractButton = findViewById(R.id.sub);
        multiplyButton = findViewById(R.id.mul);
        divideButton = findViewById(R.id.div);
        equalButton = findViewById(R.id.submit);

        num1Button = findViewById(R.id.num1);
        num2Button = findViewById(R.id.num2);
        num3Button = findViewById(R.id.num3);
        num4Button = findViewById(R.id.num4);
        num5Button = findViewById(R.id.num5);
        num6Button = findViewById(R.id.num6);
        num7Button = findViewById(R.id.num7);
        num8Button = findViewById(R.id.num8);
        num9Button = findViewById(R.id.num9);
        zeroButton = findViewById(R.id.zero);
        dotButton = findViewById(R.id.dot);
        deleteButton = findViewById(R.id.delete);
        leftParenButton = findViewById(R.id.left_paren);
        rightParenButton = findViewById(R.id.right_paren);
        negativeButton = findViewById(R.id.negative);

        // Initialize display
        updateDisplay();

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

        // Number button listeners
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                appendToEquation(button.getText().toString());
            }
        };

        zeroButton.setOnClickListener(numberClickListener);
        num1Button.setOnClickListener(numberClickListener);
        num2Button.setOnClickListener(numberClickListener);
        num3Button.setOnClickListener(numberClickListener);
        num4Button.setOnClickListener(numberClickListener);
        num5Button.setOnClickListener(numberClickListener);
        num6Button.setOnClickListener(numberClickListener);
        num7Button.setOnClickListener(numberClickListener);
        num8Button.setOnClickListener(numberClickListener);
        num9Button.setOnClickListener(numberClickListener);

        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendDecimal();
            }
        });

        // Operator listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOperator("+");
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOperator("-");
            }
        });

        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOperator("×");
            }
        });

        divideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOperator("÷");
            }
        });

        // Parentheses
        leftParenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendLeftParen();
            }
        });

        rightParenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendRightParen();
            }
        });

        // Negative/Positive toggle
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleNegative();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastChar();
            }
        });

        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });
    }

    private void appendToEquation(String digit) {
        if (lastWasEquals) {
            currentEquation = "0";
            lastWasEquals = false;
        }

        if (currentEquation.equals("0")) {
            currentEquation = digit;
        } else {
            currentEquation += digit;
        }
        lastWasOperator = false;
        updateDisplay();
    }

    private void appendDecimal() {
        if (lastWasEquals) {
            currentEquation = "0.";
            lastWasEquals = false;
        } else {
            String lastNumber = getLastNumber();
            if (!lastNumber.contains(".")) {
                if (lastWasOperator || currentEquation.endsWith("(")) {
                    currentEquation += "0.";
                } else {
                    currentEquation += ".";
                }
            }
        }
        lastWasOperator = false;
        updateDisplay();
    }

    private void appendOperator(String operator) {
        if (lastWasEquals) {
            lastWasEquals = false;
        }

        if (currentEquation.equals("0")) {
            return;
        }

        char lastChar = currentEquation.charAt(currentEquation.length() - 1);

        if (lastWasOperator && !currentEquation.endsWith("(")) {
            // Replace last operator
            currentEquation = currentEquation.substring(0, currentEquation.length() - 1) + operator;
        } else if (lastChar != '(') {
            currentEquation += operator;
        }

        lastWasOperator = true;
        updateDisplay();
    }

    private void appendLeftParen() {
        if (lastWasEquals) {
            currentEquation = "0";
            lastWasEquals = false;
        }

        if (currentEquation.equals("0")) {
            currentEquation = "(";
        } else {
            char lastChar = currentEquation.charAt(currentEquation.length() - 1);
            if (isOperator(lastChar) || lastChar == '(') {
                currentEquation += "(";
            } else {
                currentEquation += "×(";
            }
        }
        lastWasOperator = false;
        updateDisplay();
    }

    private void appendRightParen() {
        if (lastWasEquals || currentEquation.equals("0")) {
            return;
        }

        int openCount = 0;
        int closeCount = 0;
        for (char c : currentEquation.toCharArray()) {
            if (c == '(') openCount++;
            if (c == ')') closeCount++;
        }

        if (openCount > closeCount && !lastWasOperator) {
            currentEquation += ")";
            lastWasOperator = false;
            updateDisplay();
        }
    }

    private void toggleNegative() {
        if (lastWasEquals || currentEquation.equals("0")) {
            return;
        }

        String lastNumber = getLastNumber();
        int lastNumStart = currentEquation.lastIndexOf(lastNumber);

        if (lastNumStart > 0 && currentEquation.charAt(lastNumStart - 1) == '-') {
            // Check if this minus is a negative sign (not subtraction)
            if (lastNumStart == 1 || currentEquation.charAt(lastNumStart - 2) == '(' ||
                    isOperator(currentEquation.charAt(lastNumStart - 2))) {
                // Remove the negative sign
                currentEquation = currentEquation.substring(0, lastNumStart - 1) +
                        currentEquation.substring(lastNumStart);
            } else {
                // Add negative with parentheses
                currentEquation = currentEquation.substring(0, lastNumStart) + "(-" +
                        currentEquation.substring(lastNumStart) + ")";
            }
        } else {
            // Add negative sign
            if (lastNumStart == 0) {
                currentEquation = "-" + currentEquation;
            } else {
                currentEquation = currentEquation.substring(0, lastNumStart) + "(-" +
                        currentEquation.substring(lastNumStart) + ")";
            }
        }
        updateDisplay();
    }

    private void deleteLastChar() {
        if (lastWasEquals) {
            clearAll();
            return;
        }

        if (currentEquation.length() > 1) {
            currentEquation = currentEquation.substring(0, currentEquation.length() - 1);
            char lastChar = currentEquation.charAt(currentEquation.length() - 1);
            lastWasOperator = isOperator(lastChar);
        } else {
            currentEquation = "0";
            lastWasOperator = false;
        }
        updateDisplay();
    }

    private void clearAll() {
        currentEquation = "0";
        lastWasOperator = false;
        lastWasEquals = false;
        equationText.setText("");
        updateDisplay();
    }

    private void calculateResult() {
        try {
            String equation = currentEquation;
            // Replace display operators with calculation operators
            equation = equation.replace("×", "*").replace("÷", "/");

            double result = evaluateExpression(equation);

            equationText.setText(currentEquation + " =");
            currentEquation = formatResult(result);
            lastWasEquals = true;
            lastWasOperator = false;
            updateDisplay();
        } catch (Exception e) {
            displayText.setText("Error");
            equationText.setText(currentEquation);
        }
    }

    private double evaluateExpression(String expression) throws Exception {
        return evaluate(expression.trim());
    }

    private double evaluate(String expression) throws Exception {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == ' ') continue;

            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(num.toString()));
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            } else if (isOperator(c)) {
                // Handle negative numbers
                if (c == '-' && (i == 0 || expression.charAt(i-1) == '(' ||
                        isOperator(expression.charAt(i-1)))) {
                    StringBuilder num = new StringBuilder("-");
                    i++;
                    while (i < expression.length() &&
                            (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        num.append(expression.charAt(i++));
                    }
                    i--;
                    numbers.push(Double.parseDouble(num.toString()));
                    continue;
                }

                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOperator(char operator, double b, double a) throws Exception {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new Exception("Division by zero");
                return a / b;
        }
        return 0;
    }

    private String getLastNumber() {
        StringBuilder num = new StringBuilder();
        for (int i = currentEquation.length() - 1; i >= 0; i--) {
            char c = currentEquation.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                num.insert(0, c);
            } else if (c == '-' && (i == 0 || currentEquation.charAt(i-1) == '(' ||
                    isOperator(currentEquation.charAt(i-1)))) {
                num.insert(0, c);
            } else {
                break;
            }
        }
        return num.toString().isEmpty() ? "0" : num.toString();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷' || c == '*' || c == '/';
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%.8f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }

    private void updateDisplay() {
        displayText.setText(currentEquation);
    }

    public void main_spinner_selection(){
        if(current_index == 0){
            current_index++;
        }else{
            switch(main_selected){
                case "Basic Calculator":
                    break;
                case "Base Number Calculator":
                    Intent intent = new Intent(MainActivity.this, BaseNumber.class);
                    startActivity(intent);
                    finish();
                    break;
                case "Unit Converter":
                    Intent unit = new Intent(MainActivity.this, UnitConverter.class);
                    startActivity(unit);
                    finish();
                    break;
            }
        }
    }
}
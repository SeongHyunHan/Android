package com.example.dell.calbmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edtAmount;
    private TextView txtTotal;
    private RadioGroup grpHst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCalculate = (Button) findViewById(R.id.btnCalculate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        edtAmount= (EditText) findViewById(R.id.edtAmount);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        grpHst = (RadioGroup) findViewById(R.id.grpHst);
    }

    private void calculate() {
        try{
            double amount = Double.parseDouble(edtAmount.getText().toString());
            int selected = grpHst.getCheckedRadioButtonId();

            switch (selected){
                case R.id.radHst:
                    amount *= 1.13;
                    break;
                case R.id.radNoHst:
                    // Do nothing
            }
            String format = "Total is %.2f";
            txtTotal.setText(String.format(format, amount));
        }catch (NumberFormatException ex){
            Toast.makeText(MainActivity.this, "Please Enter a Valid Amount!", Toast.LENGTH_LONG).show();
            edtAmount.setError("Please Enter a Valid Amount");
        }


    }
}

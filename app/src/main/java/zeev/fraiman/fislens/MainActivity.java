package zeev.fraiman.fislens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextF, editTextD, editTextH;
    private TextView textViewResult;
    private RayView rayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextF = findViewById(R.id.editTextF);
        editTextD = findViewById(R.id.editTextD);
        editTextH = findViewById(R.id.editTextH);
        textViewResult = findViewById(R.id.textViewResult);
        rayView = findViewById(R.id.rayView);

        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplay();
            }
        });
    }

    private void calculateAndDisplay() {
        // Get input values
        float f = Float.parseFloat(editTextF.getText().toString());
        float d = Float.parseFloat(editTextD.getText().toString());
        float h = Float.parseFloat(editTextH.getText().toString());

        // Lens formula: 1/f = 1/d + 1/fPrime
        float fPrime = 1 / (1 / f - 1 / d);

        // Size formula: h/d = hPrime/fPrime
        float hPrime = (fPrime * h) / d;

        // Display results
        textViewResult.setText("Image distance (f') = " + fPrime + "\nImage height (h') = " + hPrime);

        // Pass data to RayView to draw rays
        rayView.setData(f, d, h, hPrime, fPrime);
    }
}

package ro.pub.cs.systems.eim.practicaltest01var02mainactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PracticalTest01Var02SecondaryActivity extends AppCompatActivity {

    private EditText operation = null;
    private Button correct = null, incorrect = null;

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.correct:
                    setResult(RESULT_OK, null);
                    break;
                case R.id.incorrect:
                    setResult(RESULT_CANCELED, null);
                    break;
            }
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var02_secondary);

        operation = findViewById(R.id.operation);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey("operation")) {
            String op = intent.getStringExtra("operation");
            operation.setText(String.valueOf(op));
        }

        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);

        correct.setOnClickListener(new ButtonClickListener());
        incorrect.setOnClickListener(new ButtonClickListener());
    }
}

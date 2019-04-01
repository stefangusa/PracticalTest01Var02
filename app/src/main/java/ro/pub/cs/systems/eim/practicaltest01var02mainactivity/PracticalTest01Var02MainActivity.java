package ro.pub.cs.systems.eim.practicaltest01var02mainactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01Var02MainActivity extends AppCompatActivity {

    private EditText firstNo = null;
    private EditText secondNo = null;
    private Button plus = null;
    private Button minus = null;
    private EditText result = null;
    private Button next = null;

    private int SECONDARY_ACTIVITY_REQUEST_CODE = -1;
    private String serviceStatus = "STOP";

    private IntentFilter intentFilter = new IntentFilter();

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int first, second;

            if (firstNo.getText() != null && !firstNo.getText().toString().equals(""))
                first = Integer.parseInt(firstNo.getText().toString());
            else first = 0;
            if (secondNo.getText() != null && !secondNo.getText().toString().equals(""))
                second = Integer.parseInt(secondNo.getText().toString());
            else second = 0;

            intentFilter.addAction("sum");
            intentFilter.addAction("dif");

            switch(view.getId()) {
                case R.id.plus:
                    result.setText(String.valueOf(first) + " + " + String.valueOf(second) + " = " + String.valueOf(first+second));
                    break;
                case R.id.minus:
                    result.setText(String.valueOf(first) + " - " + String.valueOf(second) + " = " + String.valueOf(first-second));
                    break;

                case R.id.next:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var02SecondaryActivity.class);

                    intent.putExtra("operation", result.getText().toString());
                    startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }

            if (serviceStatus.equals("STOP")) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra("firstNumber", first);
                intent.putExtra("secondNumber", second);
                getApplicationContext().startService(intent);
                serviceStatus = "START";
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var02_main);

        firstNo = findViewById(R.id.first);
        secondNo = findViewById(R.id.second);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        result = findViewById(R.id.result);
        next = findViewById(R.id.next);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("first")) {
                firstNo.setText(savedInstanceState.getString("first"));
            }
            if (savedInstanceState.containsKey("second")) {
                secondNo.setText(savedInstanceState.getString("second"));
            }
            if (savedInstanceState.containsKey("result")) {
                result.setText(savedInstanceState.getString("result"));
            }
        }

        plus.setOnClickListener(new ButtonClickListener());
        minus.setOnClickListener(new ButtonClickListener());
        next.setOnClickListener(new ButtonClickListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("first", firstNo.getText().toString());
        savedInstanceState.putString("second", secondNo.getText().toString());
        savedInstanceState.putString("result", result.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("first")) {
            firstNo.setText(savedInstanceState.getString("first"));
        }
        if (savedInstanceState.containsKey("second")) {
            secondNo.setText(savedInstanceState.getString("second"));
        }
        if (savedInstanceState.containsKey("result")) {
            result.setText(savedInstanceState.getString("result"));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }
}

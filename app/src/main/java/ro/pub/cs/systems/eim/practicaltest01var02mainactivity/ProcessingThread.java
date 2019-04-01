package ro.pub.cs.systems.eim.practicaltest01var02mainactivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Random;

public class ProcessingThread extends Thread {

    private Context context = null;
    private boolean isRunning = true;

    private Random random = new Random();

    private int sum;
    private int minus;

    private boolean sum_sent = false, dif_sent = false;

    private String[] actions = {"sum", "dif"};

    public ProcessingThread(Context context, int firstNumber, int secondNumber) {
        this.context = context;

        sum = firstNumber + secondNumber;
        minus = firstNumber - secondNumber;
    }

    @Override
    public void run() {
        Log.d("[ProcessingThread]", "Thread has started!");
        while (isRunning) {
            sendMessage();
            sleep();
        }
        Log.d("[ProcessingThread]", "Thread has stopped!");
    }

    private void sendMessage() {
        Intent intent = new Intent();
        String action = actions[random.nextInt(actions.length)];

        if (action.equals("sum")) {
            sum_sent = true;
            intent.setAction(action);
            intent.putExtra("message", String.valueOf(sum));
        }
        else {
            dif_sent = true;
            intent.setAction(action);
            intent.putExtra("message", String.valueOf(minus));
        }
        context.sendBroadcast(intent);

        if (sum_sent && dif_sent)
                stopThread();
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}


package com.aqoong.lib.ecgmonitorsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.aqoong.lib.ecgmonitor.MonitorView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private MonitorView vMonitor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vMonitor = findViewById(R.id.monitor);



        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if (message.what == 0){
                    vMonitor.updateData(0, (float)(Math.random() * 3));
                    return true;
                }
                return false;
            }
        });



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
              handler.sendEmptyMessage(0);
            }
        },0, 20);
    }
}

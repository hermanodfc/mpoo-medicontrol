package com.adht.android.medicontrol.alarme.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import com.adht.android.medicontrol.infra.ui.MainActivity;

public class Alarm extends BroadcastReceiver {
        private String nome;
        private int request;

    @Override
    public void onReceive(Context context, Intent intent) {
        nome = intent.getStringExtra("ALARME_NOME");
        request = intent.getIntExtra("REQUEST", 0);



        Toast.makeText(context, "Alarme", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(10000);
        Intent intent2 = new Intent(context, AlarmeToqueActivity.class);
        intent2.putExtra("ALARME_NOME2", nome);
        intent2.putExtra("ALARME_REQUEST2", request);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);
    }
}

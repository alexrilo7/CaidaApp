package com.example.caidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.caidapp.Enumerado.TipoAlarmaEnum;

import java.util.ArrayList;

import ServicioSensor.ServicioSensor;

public class Telegram extends WearableActivity {

    private TextView mTextView;
    private Spinner spinnerTelegram;
    private Intent intent;
    private SharedPreferences preferences;
    private ArrayAdapter<Object> adapterTelegram;
    private Button btnComenzar;
    private boolean cond = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telegram);

        mTextView = (TextView) findViewById(R.id.text);
        spinnerTelegram = (Spinner) findViewById(R.id.spinnerTelegram);
        btnComenzar = (Button) findViewById(R.id.btnComenzar);
        intent = new Intent(this, ServicioSensor.class);
        preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);

        ArrayList<String> arrayTelegram = new ArrayList<String>();
        arrayTelegram.add("Elija un contacto: ");
        arrayTelegram.add(preferences.getString("username", ""));
        adapterTelegram = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, arrayTelegram.toArray());
        spinnerTelegram.setAdapter(adapterTelegram);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void start(View view) {
        if (cond) {
            intent.putExtra("contactoTelegram", spinnerTelegram.getItemAtPosition(spinnerTelegram.getSelectedItemPosition()).toString());
            intent.putExtra("TipoMensaje", TipoAlarmaEnum.TELEGRAM.name());

            if (spinnerTelegram.getSelectedItemPosition() == 0) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Telegram.this);
                alertDialog.setTitle("Error al seleccionar contacto.");
                alertDialog.setMessage("Elija un contacto de la lista por favor.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            } else {
                if (preferences.getLong("chatId", 0) == 0) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Telegram.this);
                    alertDialog.setTitle("No hay conversación con este contacto.");
                    alertDialog.setMessage("Si quiere avisar a este contacto siga las siguientes instruccines: \n 1. Abra la aplicación de Telegram. \n 2.Abra una conversación con el contacto al que quieras avisar y envía invitación para abrir conversación con el bot.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent open7 = getPackageManager().getLaunchIntentForPackage("org.telegram.messenger");
                                    startActivity(open7);
                                }
                            });
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                } else {
                    startService(intent);
                    btnComenzar.setText("Detener");
                    cond = false;
                }
            }
        } else {
            cond = true;
            stopService(intent);
            btnComenzar.setText("Comenzar");
        }
    }

}


package com.example.caidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import ServicioSensor.ServicioSensor;

public class AgregarContactoTelegram extends WearableActivity {

    private TextView mTextView;
    private EditText contactoTelegramTextField;
    private Button btnAgregar;
    private SharedPreferences preferences;
    private TelegramBot bot;
    private Intent intent;
    private long chatId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto_telegram);
        contactoTelegramTextField = (EditText) findViewById(R.id.contactoTelegramTextField);
        username = contactoTelegramTextField.getText().toString();
        intent = new Intent(this, ServicioSensor.class);
        bot = new TelegramBot("971842579:AAHuRm4MT3qaOuSMjWgWq_MrmulXsNi1jPI");
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null) {
                    long chatId = update.message().chat().id();
                    SendResponse response = bot.execute(new SendMessage(chatId, "Hola, Bienvenido a caidApp!!"));
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        mTextView = (TextView) findViewById(R.id.text);

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void setDatosPersonales(View view) {
        String username = contactoTelegramTextField.getText().toString();
        //ArrayList messageParts = smsManager.divideMessage("Abre este enlace y envía un mensaje para conectarte conmigo: https://t.me/CaidAppBot .");
        //smsManager.sendMultipartTextMessage(phone, null, messageParts, null, null);

        if (preferences.getLong("chatId", 0) == 0) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AgregarContactoTelegram.this);
            alertDialog.setTitle("Instrucciones.");
            alertDialog.setMessage("1. Abra la aplicación de Telegram. \n 2.Cree un grupo de Telegram. \n 3. Añada el/los contactos que quiera avisar al grupo. \n 4.Añada el bot al grupo. ").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent open7 = getPackageManager().getLaunchIntentForPackage("org.telegram.messenger");
                        startActivity(open7);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("username", username);
            edit.commit();
        } else {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("username", username);
            edit.commit();
        }
    }
}

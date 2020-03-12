package com.example.caidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.caidapp.Enumerado.TipoAlarmaEnum;

import ServicioSensor.ServicioSensor;

public class Email extends WearableActivity {

    private TextView mTextView;
    private EditText emailTextField;
    private Intent intent;
    private Button btnComenzar;
    private boolean cond = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        mTextView = (TextView) findViewById(R.id.text);
        emailTextField = (EditText) findViewById(R.id.emailTextField);
        btnComenzar = (Button) findViewById(R.id.btnComenzar);
        intent = new Intent(this, ServicioSensor.class);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void start(View view) {
        if (cond) {
            intent.putExtra("destinatario", emailTextField.getText().toString());
            intent.putExtra("tipoMensaje", TipoAlarmaEnum.EMAIL.name());
            startService(intent);
            cond = false;
            btnComenzar.setText("Detener");
        } else {
            stopService(intent);
            btnComenzar.setText("Comenzar");
            cond = true;
        }

    }


}

package com.example.caidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    private ImageButton btnIniciar;
    private ImageButton btnTelegram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        btnIniciar = (ImageButton) findViewById(R.id.btnIniciar);
        btnTelegram = (ImageButton) findViewById(R.id.btnTelegram);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void iniciarApp(View view) {
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        Intent in = new Intent(this, TipoComunicacion.class);
        startActivity(in);
    }

    public void agregarContactoTelegram(View view) {
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        Intent in = new Intent(this, AgregarContactoTelegram.class);
        startActivity(in);
    }
}

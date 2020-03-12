package com.example.caidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.caidapp.Enumerado.TipoAlarmaEnum;

import java.util.ArrayList;

public class TipoComunicacion extends WearableActivity {

    private TextView mTextView;
    private Spinner spinnerTipoComunicacion;
    private Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_comunicacion);
        spinnerTipoComunicacion = (Spinner) findViewById(R.id.spinnerTipoComunicacion);
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        ArrayList<String> arrayTipoComunicacion = new ArrayList<String>();
        arrayTipoComunicacion.add("Elige una opción: ");
        arrayTipoComunicacion.add(TipoAlarmaEnum.MENSAJE.name());
        arrayTipoComunicacion.add(TipoAlarmaEnum.TELEGRAM.name());
        arrayTipoComunicacion.add(TipoAlarmaEnum.EMAIL.name());
        ArrayAdapter<String> adapterTipoComunicacion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayTipoComunicacion);
        spinnerTipoComunicacion.setAdapter(adapterTipoComunicacion);
        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void siguienteStep(View view) {
        elegirSiguienteStep(spinnerTipoComunicacion.getItemAtPosition(spinnerTipoComunicacion.getSelectedItemPosition()).toString());
//        if (spinnerTipoComunicacion.getSelectedItemPosition() == 0) {
//            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TipoComunicacion.this);
//            alertDialog.setTitle("Error al seleccionar opción.");
//            alertDialog.setMessage("Elija una opción de la lista por favor.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            AlertDialog alert = alertDialog.create();
//            alert.show();
//        }
//        else if(spinnerTipoComunicacion.getItemAtPosition(spinnerTipoComunicacion.getSelectedItemPosition()).toString().equals(TipoAlarmaEnum.MENSAJE.name())) {
//            Intent in = new Intent(this, Sms.class);
//            startActivity(in);
//        }
//        else if(spinnerTipoComunicacion.getItemAtPosition(spinnerTipoComunicacion.getSelectedItemPosition()).toString().equals(TipoAlarmaEnum.TELEGRAM.name())) {
//            Intent in = new Intent(this, Telegram.class);
//            startActivity(in);
//        }
//        else if(spinnerTipoComunicacion.getItemAtPosition(spinnerTipoComunicacion.getSelectedItemPosition()).toString().equals(TipoAlarmaEnum.EMAIL.name())) {
//            Intent in = new Intent(this, Email.class);
//            startActivity(in);
//        }

    }

    private TipoAlarmaEnum stringToTipoAlarmaEnum(String tipoComunicacion) {

        switch (tipoComunicacion.toUpperCase()) {
            case "EMAIL":
                return TipoAlarmaEnum.EMAIL;
            case "MENSAJE":
                return TipoAlarmaEnum.MENSAJE;
            case "TELEGRAM":
                return TipoAlarmaEnum.TELEGRAM;
            default:
                return TipoAlarmaEnum.DEFAULT;
        }
    }

    public void elegirSiguienteStep(String tipoComunicacion) {
        try {
            switch (stringToTipoAlarmaEnum(tipoComunicacion)) {
                case EMAIL:
                    Intent in = new Intent(this, Email.class);
                    startActivity(in);
                    break;
                case MENSAJE:
                    Intent intent = new Intent(this, Sms.class);
                    startActivity(intent);
                    break;
                case TELEGRAM:
                    Intent intent1 = new Intent(this, Telegram.class);
                    startActivity(intent1);
                    break;
                default:
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TipoComunicacion.this);
                    alertDialog.setTitle("Error al seleccionar opción.");
                    alertDialog.setMessage("Elija una opción de la lista por favor.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

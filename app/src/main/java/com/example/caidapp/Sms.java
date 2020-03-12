package com.example.caidapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.caidapp.Enumerado.TipoAlarmaEnum;

import java.util.ArrayList;

import ServicioSensor.ServicioSensor;

public class Sms extends WearableActivity {

    private TextView mTextView;
    private Spinner spinner;
    private Intent intent;
    private Button btnComenzar;
    private boolean cond = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        mTextView = (TextView) findViewById(R.id.text);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnComenzar = (Button) findViewById(R.id.btnComenzar);
        intent = new Intent(this, ServicioSensor.class);
        if (ActivityCompat.checkSelfPermission(
                Sms.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Sms.this, Manifest
                        .permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Sms.this, new String[]
                    {Manifest.permission.SEND_SMS,}, 1000);
        }
        if (ContextCompat.checkSelfPermission(
                Sms.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                Sms.this, Manifest
                        .permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Sms.this, new String[]
                    {Manifest.permission.READ_CONTACTS}, 1000);
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
        } else {
            Object[] array = obtenerDatos();
            ArrayAdapter<Object> adapter;
            adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, array);

            spinner.setAdapter(adapter);

        }

        // Enables Always-on
        setAmbientEnabled();
    }

    public Object[] obtenerDatos() {
        String[] proyeccion = new String[]{ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                proyeccion,
                selectionClause,
                null,
                sortOrder);
        ArrayList<Object> array = new ArrayList<Object>();

        array.add("Seleccione un contacto: ");
        while (c.moveToNext()) {
            array.add(c.getString(0) + " - " + c.getString(1));
        }

        return array.toArray();
    }

    public void start(View view) {
        if (cond) {
            intent.putExtra("Telefono", spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
            intent.putExtra("TipoMensaje", TipoAlarmaEnum.MENSAJE.name());

            if (spinner.getSelectedItemPosition() == 0) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sms.this);
                alertDialog.setTitle("Error al seleccionar contacto.");
                alertDialog.setMessage("Elija un contacto de la lista por favor.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            } else {
                //Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34619322191"));
                if (ActivityCompat.checkSelfPermission(Sms.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Sms.this, new String[]
                            {Manifest.permission.CALL_PHONE}, 1000);
                }
           //     startActivity(i);
                startService(intent);
                btnComenzar.setText("Detener");
                cond = false;
            }
        } else {
            stopService(intent);
            btnComenzar.setText("Comenzar");
            cond = true;
        }

    }
}

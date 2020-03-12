package ServicioSensor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import com.example.caidapp.Buffer;
import com.example.caidapp.Hilos.Consumidor;
import com.example.caidapp.Hilos.Productor;

public class ServicioSensor extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Buffer contenedor;
    private Productor productor;
    private Consumidor consumidor;
    private SharedPreferences preferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess) {

        Toast.makeText(getApplicationContext(), "iniciado", Toast.LENGTH_LONG).show();
        contenedor = new Buffer();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        productor = new Productor(contenedor, 1);
        consumidor = new Consumidor(contenedor, 1, intent.getStringExtra("Telefono"), intent.getStringExtra("TipoMensaje"), intent.getStringExtra("destinatario"), intent.getStringExtra("contactoTelegram"), preferences.getLong("chatId", 0));

        productor.start();
        consumidor.start();
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        consumidor.setCond(false);
        productor.setCond(false);
        Toast.makeText(getApplicationContext(), "detenido", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        productor.setPoner(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

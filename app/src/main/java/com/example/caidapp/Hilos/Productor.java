package com.example.caidapp.Hilos;

import android.widget.Toast;

import com.example.caidapp.Buffer;
import com.example.caidapp.Sms;

public class Productor extends Thread
{

    private final Buffer contenedor;
    private final int idproductor;
    private final int TIEMPOESPERA = 150;
    private float[] poner = new  float[3];
    private boolean cond = true;
    /**
     * Constructor de la clase
     * @param contenedor Contenedor común a los consumidores y el productor
     * @param idproductor Identificador del productor
     */
    public Productor(Buffer contenedor, int idproductor)
    {
        this.contenedor = contenedor;
        this.idproductor = idproductor;
    }

    @Override
    /**
     * Implementación del hilo
     */
    public synchronized void run()
    {
        while(cond)
        {
            if(!contenedor.isCaida()) {
                if (poner[0] != 0f || poner[1] != 0f || poner[2] != 0f) {
                    contenedor.put(poner[0], poner[1], poner[2]);
                    System.out.println("El productor " + idproductor + " pone: " + poner);
                }
                try {
                    Thread.sleep(TIEMPOESPERA);
                } catch (InterruptedException e) {
                    System.err.println("Productor " + idproductor + ": Error en run -> " + e.getMessage());
                }
            }
            else {
                cond = false;
            }
        }
    }

    public void setPoner(float valorX, float valorY, float valorZ){
        this.poner[0] = valorX;
        this.poner[1] = valorY;
        this.poner[2] = valorZ;
    }

    public float[] getPoner(){
        return poner;
    }

    public boolean isCond() {
        return cond;
    }

    public void setCond(boolean cond) {
        this.cond = cond;
    }
}

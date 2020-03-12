package com.example.caidapp;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class Buffer {
    private ArrayList<Float> contenidoX = new ArrayList<Float>();
    private ArrayList<Float> contenidoY = new ArrayList<Float>();
    private ArrayList<Float> contenidoZ = new ArrayList<Float>();
    private boolean[] bufferLleno = new boolean[3];
    private boolean alarma = Boolean.FALSE;
    private boolean caida;

    public boolean isCaida() {
        return caida;
    }

    public void setCaida(boolean caida) {
        this.caida = caida;
    }

    /**
     * Obtiene de forma concurrente o síncrona el elemento que hay en el contenedor
     *
     * @return Contenido el contenedor
     */

    public ArrayList<Float> getContenidoX() {
        return contenidoX;
    }

    public ArrayList<Float> getContenidoY() {
        return contenidoY;
    }

    public ArrayList<Float> getContenidoZ() {
        return contenidoZ;
    }

    /**
     * Introduce de forma concurrente o síncrona un elemento en el contenedor
     *
     * @param value Elemento a introducir en el contenedor
     */
    public synchronized void put(float valueX, float valueY, float valueZ) {
        while (bufferLleno[0] && bufferLleno[1] && bufferLleno[2]) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Contenedor: Error en put -> " + e.getMessage());
            }
        }

        contenidoX.add(valueX);
        contenidoY.add(valueY);
        contenidoZ.add(valueZ);
        if (arrayLleno(contenidoX) && arrayLleno(contenidoY) && arrayLleno(contenidoZ)) {
            alarma = Boolean.TRUE;
            notifyAll();
        }


    }

    private boolean arrayLleno(ArrayList<Float> contenido) {
        if (contenido.size() == 10) {
            return true;
        }
        return false;
    }


    public boolean getAlarma() {
        return alarma;
    }

    public void setAlarma(boolean alarma) {
        this.alarma = alarma;
    }

    public synchronized boolean reglasPart(ArrayList<Float> contenidoX, ArrayList<Float> contenidoY, ArrayList<Float> contenidoZ) {
        while(!alarma) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        float mediaX, mediaY, mediaZ, desviacionX, desviacionY, desviacionZ;
        mediaX = calcularMedia(contenidoX);
        mediaY = calcularMedia(contenidoY);
        mediaZ = calcularMedia(contenidoZ);
        desviacionX = calcularDesviacion(mediaX, contenidoX);
        desviacionY = calcularDesviacion(mediaY, contenidoY);
        desviacionZ = calcularDesviacion(mediaZ, contenidoZ);
        contenidoX.removeAll(contenidoX);
        contenidoY.removeAll(contenidoY);
        contenidoZ.removeAll(contenidoZ);
        if (desviacionY <= 7.85251 && mediaX <= -1.915888 && mediaX <= -5.167052) {
            alarma = false;
            notifyAll();
            return false;
        } else if (mediaY <= -1.393019 && mediaX <= -0.892325) {
            alarma = false;
            notifyAll();
            return false;
        } else if (desviacionZ > 8.739516 && desviacionZ <= 12.484265 && desviacionX > 5.647066 && desviacionZ > 9.057386 && desviacionX <= 9.697082) {
            alarma = false;
            notifyAll();
            return true;
        } else if (desviacionZ > 12.037231 && mediaX <= 0.051301) {
            alarma = false;
            notifyAll();
            return false;
        } else if (desviacionZ > 9.471781 && desviacionX > 4.391388 && mediaY > 0.222906) {
            alarma = false;
            notifyAll();
            return true;
        } else if (desviacionX <= 10.873608 && desviacionZ > 3.431882 && mediaZ > -8.16825 && desviacionY > 7.873445 && desviacionX <= 8.617842 && desviacionY <= 10.071214) {
            alarma = false;
            notifyAll();
            return true;
        } else if (desviacionY <= 10.758516 && mediaX <= -1.782843 && desviacionY <= 6.744042) {
            alarma = false;
            notifyAll();
            return false;
        } else if (mediaX > -6.697631 && mediaY <= 4.694718 && desviacionY <= 10.503266 && desviacionY > 3.759877 && desviacionY <= 6.818535 && mediaZ > -3.746459) {
            alarma = false;
            notifyAll();
            return true;
        } else if (desviacionY <= 10.758516 && mediaX > -6.128296 && mediaY <= 4.694718 && desviacionY > 4.023786) {
            alarma = false;
            notifyAll();
            return true;
        } else if (desviacionY <= 10.758516) {
            alarma = false;
            notifyAll();
            return false;
        }
        alarma = false;
        notifyAll();
        return true;
    }

    public float calcularMedia(ArrayList<Float> array) {
        float suma = 0f;
        for (float valor : array) {
            suma = suma + valor;
        }
        return suma / array.size();
    }

    public float calcularDesviacion(float media, ArrayList<Float> array) {
        float suma = 0;
        float desviacion;
        for (int i = 0; i < array.size(); i++) {
            suma = suma + (float) Math.pow(array.get(i) - media, 2);
        }
        desviacion = (float) Math.sqrt(suma / (array.size() - 1));
        return desviacion;
    }
}
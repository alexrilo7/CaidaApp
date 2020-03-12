package com.example.caidapp.Hilos;

import android.os.StrictMode;
import android.telephony.SmsManager;

import com.example.caidapp.Buffer;
import com.example.caidapp.Enumerado.TipoAlarmaEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Consumidor extends Thread {
    private final Buffer contenedor;
    private final int idconsumidor;
    private final String telefono;
    private final String tipoMensaje;
    private final String destinatario;
    private final String contactoTelegram;
    private final String apiToken = "971842579:AAHuRm4MT3qaOuSMjWgWq_MrmulXsNi1jPI";
    private SmsManager smsManager = SmsManager.getDefault();
    private boolean cond = true;
    private long chatId;

    /**
     * Constructor de la clase
     *
     * @param contenedor   Contenedor común a los consumidores y el productor
     * @param idconsumidor Identificador del consumidor
     */
    public Consumidor(Buffer contenedor, int idconsumidor, String telefono, String tipoMensaje, String destinatario, String contactoTelegram, long chatId) {
        this.contenedor = contenedor;
        this.idconsumidor = idconsumidor;
        this.telefono = telefono;
        this.tipoMensaje = tipoMensaje;
        this.destinatario = destinatario;
        this.contactoTelegram = contactoTelegram;
        this.chatId = chatId;
    }

    @Override
    /**
     * Implementación de la hebra
     */
    public synchronized void run() {
        while (cond) {
            if (contenedor.reglasPart(contenedor.getContenidoX(), contenedor.getContenidoY(), contenedor.getContenidoZ())) {
                envioAlarma();
            }
        }
    }


    public void envioAlarma() {
        cond = false;
        contenedor.setCaida(true);
        if (TipoAlarmaEnum.MENSAJE.name().equals(tipoMensaje)) {
            smsManager.sendTextMessage("+1 555-521-5554", null, "Su familiar ha sufrido una caída", null, null);

        } else if (TipoAlarmaEnum.EMAIL.name().equals(tipoMensaje)) {
            enviarConGmail(destinatario, "Caída Familiar", "Su familiar ha sufrido una caída");
        } else if (TipoAlarmaEnum.TELEGRAM.name().equals(tipoMensaje)) {
            envioTelegram();
        } else {
            enviarConGmail(destinatario, "Caída Familiar", "Su familiar ha sufrido una caída");
        }
    }

    private void enviarConGmail(String destinatario, String asunto, String body) {
        String remitente = "alejandroriloferreiro";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.user", remitente);
        properties.put("mail.smtp.clave", "alex19971734");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(remitente));
            message.addRecipients(javax.mail.Message.RecipientType.TO, destinatario);
            message.setSubject(asunto);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, "alex19971734");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
//
    public String getChatId(String userNameDestinatario) {

        String urlString = "https://api.telegram.org/bot" + apiToken + "/getUpdates";

        urlString = String.format(urlString, apiToken);
        String chatId = "";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        try {
            is = new BufferedInputStream(conn.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while (true) {
            try {
                if (!((inputLine = br.readLine()) != null)) break;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            sb.append(inputLine);
        }
        String response = sb.toString();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray jsonArray = new JSONArray(obj.getString("result"));
            for (int i = 0; i < jsonArray.length(); i++) {
                String json = jsonArray.getJSONObject(i).getJSONObject("message").getJSONObject("from").getString("username");
                if (userNameDestinatario.equals(json)) {
                    chatId = jsonArray.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getString("id");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String chatId = response.split(":")[7].split(",")[0];
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.disconnect();
        return chatId;
    }


    private void envioTelegram() {
        getChatId("alexrilo");
        String urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + chatId + "&text=Su familiar ha sufrido una caida.";
        urlString = String.format(urlString, apiToken, chatId);
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = null;
            try {
                is = new BufferedInputStream(conn.getInputStream());
                is.close();
                conn.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public boolean isCond() {
        return cond;
    }

    public void setCond(boolean cond) {
        this.cond = cond;
    }
}
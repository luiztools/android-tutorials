package com.luiztools.geolocalizacao;

import org.json.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClienteGeoIP {

    private static String readStream(InputStream in){
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;

        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return total.toString();
    }

    public static String request(String stringUrl){
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return readStream(in);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        return "";
    }

    public static Localizacao retornarLocalizacaoPorIp(String ip)
            throws IOException, JSONException {

        String content = request("http://freegeoip.net/json/" + ip);
        JSONObject obj = new JSONObject(content);
        String pais = obj.getString("country_name");
        String estado = obj.getString("region_name");
        String cidade = obj.getString("city");
        return new Localizacao(pais, estado, cidade);
    }
}

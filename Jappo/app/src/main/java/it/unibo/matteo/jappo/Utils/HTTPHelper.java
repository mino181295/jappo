package it.unibo.matteo.jappo.Utils;

import android.text.StaticLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HTTPHelper {

    public static final String REST_BACKEND = "http://jappo.pe.hu/php/bridge.php";

    public static String connectPost(String urlToConnect, HashMap<String, String> params) {
        HttpURLConnection httpUrlConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;
        try {
            URL url = new URL(urlToConnect);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setConnectTimeout(5000);

            OutputStream output = httpUrlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            output.close();

            int responseCode = httpUrlConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                InputStream inputStream = httpUrlConnection.getInputStream();
                rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (Exception e) {
                }
            }
            if (httpUrlConnection != null)
                httpUrlConnection.disconnect();
        }
        return response.toString();
    }

    public static String getPostDataString(HashMap<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first){
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

}

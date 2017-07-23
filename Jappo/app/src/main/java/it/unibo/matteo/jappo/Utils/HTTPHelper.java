package it.unibo.matteo.jappo.Utils;

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
    public static final String REST_BACKEND_UPLOAD = "http://jappo.pe.hu/php/upload_image.php";
    public static final String REST_BACKEND_DOWNLOAD = "http://jappo.pe.hu/php/download_image.php";

    /**
     * Post request to a specific url with an {@link HashMap} of parameters.
     * @param urlToConnect url where the function makes the request
     * @param params params passed in the post request
     * @return the string response of the server
     */
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

    /**
     * Post upload of an encoded Image {@link android.graphics.Bitmap} in {@link android.util.Base64}
     * @param imageName name of the image in the server
     * @param imageEncoded string stream of the {@link android.graphics.Bitmap} encoded
     */
    public static void uploadImageBase64(String imageName, String imageEncoded){
        HashMap<String, String> params = new HashMap<>();
        params.put("encoded_string", imageEncoded);
        params.put("image_name", imageName);

        connectPost(REST_BACKEND_UPLOAD, params);
    }

    /**
     * Function where I can download the image from the server with a set name
     * @param imageName name of the image in the server
     * @return
     */
    public static String downloadImageBase64(String imageName){
        HashMap<String, String> params = new HashMap<>();
        params.put("image_name", imageName);

        String response = connectPost(REST_BACKEND_DOWNLOAD, params);
        return response;
    }
}

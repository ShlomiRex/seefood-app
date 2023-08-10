package com.shlomirex.seefood;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

public class SendPhotoTask extends AsyncTask<APIRequestParams, Void, APIResult> {

    private String apiServerHost = "192.168.1.107";
    private int apiServerPort = 5000;

    private String fileName = "file";
    private final String boundary =  "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    @Override
    protected APIResult doInBackground(APIRequestParams... params) {
        APIRequestParams param = params[0];

        Bitmap photo = param.photo;
        Context context = param.context;
        String file = "api";

        APIResult result;

        try {
            //TODO: Change URL to the server's URL
            URL url = new URL("http", apiServerHost, apiServerPort, "/api/");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            Log.i("sendAPIRequest", "Opened connection to " + apiServerHost + ":" + apiServerPort + "/" + file);

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "image/jpeg");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Cache-Control", "no-cache");
            String boundary = "*****";
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            OutputStream outputStream = con.getOutputStream();
            DataOutputStream request = new DataOutputStream(outputStream);
            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    fileName + "\";filename=\"" +
                    fileName + "\"" + this.crlf);
            request.writeBytes(this.crlf);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] file_bytes = stream.toByteArray();
            photo.recycle();

            request.write(file_bytes);
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary +
                    this.twoHyphens + this.crlf);

            request.flush();
            request.close();

            // Send request
            con.connect();
            Log.i("sendAPIRequest", "Sent request to API server");

            // Get response
            int code = con.getResponseCode();
            Log.d("sendAPIRequest", "Response code: " + code);
            Log.d("sendAPIRequest", "Response message: " + con.getResponseMessage());

            if (code != 200) {
                Log.e("sendAPIRequest", "API server returned code " + code);
                //Toast.makeText(context, "Internal error", Toast.LENGTH_LONG).show();
                throw new RuntimeException("API server returned code " + code);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = reader.readLine();
//            while ((line = reader.readLine()) != null) {
//                Log.d("sendAPIRequest", line);
//            }
            reader.close();

            // Parse result
            result = new APIResult(line);
            con.disconnect();

        } catch (MalformedURLException e) {
            Log.e("sendAPIRequest", "Malformed URL, host: " + apiServerHost + ", port: " + apiServerPort + ", file: " + file);
            //Toast.makeText(context, "Internal error", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.e("sendAPIRequest", "Failed to open connection to " + apiServerHost + ":" + apiServerPort + "/" + file);
            //Toast.makeText(context, "Could not connect to API server", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }

        return result;
    }
}

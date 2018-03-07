package ca326.com.activities;

import android.util.Log;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class FileUpload {

    public static final String URL= "http://animationdoodle2017.com/videos/videoUpload.php";

    private int responseFromServer;

    public String uploadFile(String file) {

        Log.i("file","is " + file);

        String fileName = file;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        Log.i("file","check 1 is " + file);
        File selectedFile = new File(file);
        if (!selectedFile.isFile()) {
            System.out.println("file does not exist");
            Log.i("file","check 2 is " + file);
            return null;
        }
        Log.i("file","check 3 is " + file);

        try {
            Log.i("file","check 4 is " + file);
            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            URL url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("myFile", fileName);
            Log.i("file","check 10 is " + file);
            dos = new DataOutputStream(conn.getOutputStream());
            Log.i("file","check 11 is " + file);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            System.out.println("file does not exist");

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            Log.i("file","check 6 is " + file);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            responseFromServer = conn.getResponseCode();
            Log.i("file","check 15 is " + responseFromServer);
            Log.i("responseeeeeeeeeee ","response " + responseFromServer);

            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            Log.i("file","check 7 is " + file);
            ex.printStackTrace();
        } catch (Exception e) {
            Log.i("file","check 8 is " + file);
            e.printStackTrace();
        }

        if (responseFromServer == 200) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
            } catch (IOException e) {
            }
            return sb.toString();
        }else {
            return "Upload unsuccessfull";
        }
    }
}

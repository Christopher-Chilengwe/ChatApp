package chat_quickshare.model;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import chat_quickshare.tabwithviewpager.CustomHttpClient;

/**
 * Created by Belal on 11/22/2015.
 */
public class UploadVoice {

    public static final String UPLOAD_URL= "";
    HttpURLConnection con;
    private int serverResponseCode;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String uploadVideo(String file, String siD, String rId) {

        String fileName = file;
        String s = siD;
        String r = rId;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(file);
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist");
            return null;
        }




        try {



          

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();

            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }







        if (serverResponseCode == 200) {
            StringBuilder sb = new StringBuilder();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                try{
                    JSONObject jobj=new JSONObject(sb.toString());
                    String fnam=jobj.getString("name");
                    new FileNameUpdate(s,r,fnam).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rd.close();
            } catch (IOException ioex) {
            }
            return sb.toString();
        }else {
            return "Could not upload";
        }
    }
    class FileNameUpdate extends AsyncTask<String,String,String>
    {

        String respo;
        String stemp;
        String rtemp;
        String ftemp;
        FileNameUpdate(String stemp1,String rtemp1,String ftemp1)
        {
            stemp=stemp1;
            rtemp=rtemp1;
            ftemp=ftemp1;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            respo= CustomHttpClient.urlincoding("http://demo.lannettechnology.net/test/chatappdemo/audiofileName.php?sid="+stemp+"&rid="+rtemp+"&namefl="+ftemp+"");
            return respo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
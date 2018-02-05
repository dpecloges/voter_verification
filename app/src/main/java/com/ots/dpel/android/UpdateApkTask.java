package com.ots.dpel.android;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateApkTask extends AsyncTask<String, Integer, File> {

    private static final String TAG = UpdateApkTask.class.getName();
    private Context context;
    private ProgressListener listener;

    public void setContext(Context ctx, ProgressListener listener) {
        context = ctx;
        this.listener = listener;
    }

    protected void onProgressUpdate(Integer... progress) {
        if (listener != null) {
            listener.onUpdate(progress[0]);
        }
    }

    protected void onPostExecute(File file) {
        if (listener != null) {
            listener.onDownloadComplete(file);
        }
    }

    @Override
    protected File doInBackground(String... arg0) {
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            file.mkdirs();
            File outputFile = new File(file, "update.apk");
            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                publishProgress(len);
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();

            return outputFile;

        } catch (Exception e) {
            Log.e(TAG, "Update error! " + e.getMessage());
        }
        return null;
    }

    interface ProgressListener {
        void onUpdate(int progress);

        void onDownloadComplete(File outputFile);
    }
}
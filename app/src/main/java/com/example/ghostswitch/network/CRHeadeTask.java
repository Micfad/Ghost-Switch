package com.example.ghostswitch.network;

        import android.content.Context;
        import android.os.AsyncTask;
        import android.util.Log;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.lang.ref.WeakReference;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class CRHeadeTask extends AsyncTask<Void, Void, Boolean> {

    public interface CheckHeaderCallback {
        void onHeaderCheckResult(boolean isValid);
        void onHeaderCheckError(String error);
    }

    private WeakReference<Context> contextRef;
    private CheckHeaderCallback callback;
    private String ip;
    private static final String USER_AGENT = "Ghost-Switch";
    private static final String TAG = "CheckHeaderTask";

    public CRHeadeTask(Context context, CheckHeaderCallback callback, String ip) {
        this.contextRef = new WeakReference<>(context);
        this.callback = callback;
        this.ip = ip;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Context context = contextRef.get();
        if (context == null) {
            return false;
        }

        try {
            URL url = new URL("http://" + ip + "/router_connect");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setDoOutput(true); // Enable output for POST request
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                Log.d(TAG, "Response: " + result.toString());
                return true;
            } else {
                Log.e(TAG, "Server returned non-OK status: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error checking header", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (callback != null) {
            if (result) {
                callback.onHeaderCheckResult(true);
            } else {
                callback.onHeaderCheckError("Failed to validate header");
            }
        }
    }
}

package JavaClass;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Test extends IOException{

    public static String getContent(String path) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(path);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                return readStream(inputStream);
            } else {
                return "Error: " + responseCode;
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.e("JOPA", result.toString());
        return result.toString();
    }

}

//
//<TableLayout
//    xmlns:android="http://schemas.android.com/apk/res/android"
//            xmlns:app="http://schemas.android.com/apk/res-auto"
//            xmlns:tools="http://schemas.android.com/tools"
//            android:layout_width="match_parent"
//            android:layout_height="match_parent"
//            tools:context=".MainActivity">
//
//<TableRow>
//<Button
//            android:layout_width="200dp"
//                    android:layout_height="match_parent"
//                    android:text="Знакомства"
//                    android:textSize="26dp"
//                    android:backgroundTint="@android:color/holo_green_light"
//                    android:onClick="sendMessage"/>
//<Button
//            android:layout_width="210dp"
//                    android:layout_height="wrap_content"
//                    android:text="Дом"
//                    android:backgroundTint="@android:color/holo_green_light"
//                    android:textSize="26dp"
//                    />
//</TableRow>
//<TableRow>
//<Button
//            android:layout_width="200dp"
//                    android:layout_height="wrap_content"
//                    android:text="Природа"
//                    android:backgroundTint="@android:color/holo_green_light"
//                    android:textSize="26dp"
//                    />
//<Button
//            android:layout_width="match_parent"
//                    android:layout_height="wrap_content"
//                    android:text="Растения"
//                    android:backgroundTint="@android:color/holo_green_light"
//                    android:textSize="26dp"
//                    />
//</TableRow>
//<TableRow>
//<Button
//            android:layout_width="wrap_content"
//                    android:layout_height="match_parent"
//                    android:text="Семья"
//                    android:backgroundTint="@android:color/holo_green_light"
//                    android:textSize="26dp"
//                    android:layout_span="2"/>
//</TableRow>
//</TableLayout>
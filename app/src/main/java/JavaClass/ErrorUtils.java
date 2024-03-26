package JavaClass;

import android.app.AlertDialog;
import android.content.Context;

public class ErrorUtils {

    public static void showErrorDialog(Runnable context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) context);
        builder.setTitle("Ошибка")
                .setMessage(errorMessage)
                .setPositiveButton("ОК", null)
                .show();
    }
}
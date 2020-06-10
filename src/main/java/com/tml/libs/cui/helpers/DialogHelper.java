package com.tml.libs.cui.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tml.libs.cui.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TML on 04/01/2017.
 */
public class DialogHelper {
    public static void ShowAlertDialog(Context c, String title, String message,
                                       String okText, String cancelText,
                                       DialogInterface.OnClickListener okClickListener,
                                       DialogInterface.OnClickListener cancelClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(okText, okClickListener)
                .setNegativeButton(cancelText, cancelClickListener).create().show();
    }

    public static void ShowAlertDialog(Context c, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder
                .setTitle(title)
                .setMessage(message)
                .create().show();
    }

    public interface InputDialogListener {
        void onConfirm(List<String> values);
        void onCancel();
    }



    /**
     *
     * @param context
     * @param title
     * @param inputDialogListener
     */

    public static void showInputDialog(Context context, String title, final InputDialogListener inputDialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> values = new ArrayList<String>();
                values.add(input.getText().toString());
                inputDialogListener.onConfirm(values);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputDialogListener.onCancel();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }





}

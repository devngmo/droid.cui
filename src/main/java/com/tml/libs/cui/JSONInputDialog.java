package com.tml.libs.cui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TML on 24/01/2017.
 */


public class JSONInputDialog extends Dialog {
    public interface JSONDialogListener {
        void onConfirm(String jsonResult);
        void onCancel();
    }

    protected JSONInputDialog(Context context) {
        super(context);
    }

    JSONDialogListener mListener;
    JSONObject input = null;
    JSONObject output = null;
    protected JSONInputDialog(Context context, String jsonStrInput, JSONDialogListener listener) {
        super(context);

        try {
            output = new JSONObject();
            input = new JSONObject(jsonStrInput);

        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }

        mListener = listener;
    }

    protected JSONInputDialog(Context context, JSONObject jsonObjInput, JSONDialogListener listener) {
        super(context);
        output = new JSONObject();
        input = jsonObjInput;
        mListener = listener;
    }

    protected JSONInputDialog(Context context, int themeResID) {
        super(context, themeResID);
    }
    protected JSONInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    LinearLayout pnlContent;
    Button btnOK, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_fragment_okcancel);
        pnlContent = (LinearLayout)findViewById(R.id.dlg_fragment_okcancel_content);
        btnOK = (Button)findViewById(R.id.dlg_fragment_okcancel_btn_ok);
        btnCancel = (Button)findViewById(R.id.dlg_fragment_okcancel_btn_cancel);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJSONOutput();
                mListener.onConfirm(output.toString());
            }
        });

        createViewFromJSON();

    }

    private void createViewFromJSON() {
        try {
            if (input.has("title"))
                setTitle(input.getString("title"));

            JSONArray fieldArray = input.getJSONArray("fields");
            for (int i = 0; i < fieldArray.length(); i++) {
                JSONObject fieldData = fieldArray.getJSONObject(i);
                output.put(fieldData.getString("name"), "");

                View v = createViewByField(fieldData);
                if (v != null) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    pnlContent.addView(v, params);
                }
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    void updateJSONOutput()  {
        try {
            for (int i = 0; i < pnlContent.getChildCount(); i++) {
                updateOutputField(pnlContent.getChildAt(i), output);
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

     void updateOutputField(View v, JSONObject output) throws JSONException {
         if (v.getTag() == null) return;
         if (v.getTag() instanceof JSONObject) {
             JSONObject fieldData = (JSONObject) v.getTag();
             String fieldType = fieldData.getString("fieldType");
             if (fieldType.equals("text"))
                output.put(fieldData.getString("name"), ((EditText)v).getText().toString().trim());
             else if (fieldType.equals("spinner")) {
                 output.put(fieldData.getString("name"), "" + ((Spinner)v).getSelectedItemPosition());
             }
             else if (fieldType.equals("checkbox")) {
                 output.put(fieldData.getString("name"), "" + ((CheckBox)v).isChecked());
             }
         }
    }

     View createViewByField(JSONObject fieldData) throws JSONException {
        String fieldType = fieldData.getString("fieldType");
        if (fieldType.equals("text")) {
            EditText txt = new EditText(getContext());
            if (fieldData.has("text"))
                txt.setText(fieldData.getString("text"));
            txt.setTag(fieldData);
            return txt;
        }
        else if (fieldType.equals("spinner")) {
            Spinner spinner = new Spinner(getContext());
            ArrayAdapter<String> aa = createSpinnerAdapter(fieldData);
            spinner.setAdapter(aa);
            return spinner;
        }
        else if (fieldType.equals("checkbox")) {
            CheckBox cbo = new CheckBox(getContext());
            cbo.setChecked(false);
            if (fieldData.has("checked"))
                cbo.setChecked(fieldData.getBoolean("checked"));
            cbo.setTag(fieldData);
            return cbo;
        }
        return null;
    }

    private ArrayAdapter<String> createSpinnerAdapter(JSONObject fieldData) throws JSONException {
        JSONArray arValues = fieldData.getJSONArray("values");
        String[] items = new String[arValues.length()];
        for (int i = 0; i < arValues.length(); i++) {
            items[i] = arValues.getString(i);
        }
        return new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                items
        );

    }
}

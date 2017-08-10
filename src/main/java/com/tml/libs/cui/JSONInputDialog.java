package com.tml.libs.cui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.renderscript.Int4;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tml.libs.cui.cardlist.CardListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TML on 24/01/2017.
 */


public class JSONInputDialog extends Dialog {
    private static final int HZ_CELL_SIZE = 56;
    private static final int HZ_CELL_MARGIN = 8;
    public static final String USERPASS_FIELD_ID = "id";
    public static final String USERPASS_FIELD_PASS = "pass";

    public static JSONInputDialog createOneTextInputDialog(Context c, String title, String message, String fieldName, JSONDialogListener listener) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", title);
            obj.put("msg", message);
            JSONArray fields = new JSONArray();
            JSONObject fieldText = new JSONObject();
            fieldText.put("fieldType", "text");
            fieldText.put("name", fieldName);
            fieldText.put("text", "");
            fields.put(fieldText);
            obj.put("fields", fields);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONInputDialog dlg = new JSONInputDialog(c, obj, listener);
        return dlg;
    }

    public static JSONObject createParamsUserPass(String title, String message) {
        JSONObject obj= new JSONObject();
        try {
            obj.put("title", title);
            obj.put("msg", message);
            JSONArray fields = new JSONArray();
            JSONObject fieldID = new JSONObject();
            fieldID.put("fieldType", "text");
            fieldID.put("name", USERPASS_FIELD_ID);
            fieldID.put("text", "name");
            fields.put(fieldID);

            JSONObject fieldPass = new JSONObject();
            fieldPass.put("fieldType", "text");
            fieldPass.put("name", USERPASS_FIELD_PASS);
            fieldPass.put("text", "pass");
            fields.put(fieldPass);

            obj.put("fields", fields);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static JSONObject createTextField(String fieldName) {
        return createTextField(fieldName, fieldName, "");
    }

    public static JSONObject createTextField(String fieldName, String value) {
        return createTextField(fieldName, fieldName, value);
    }

    public static JSONObject createTextField(String fieldName, String fieldCaption, String value) {
        return createTextField(fieldName, fieldCaption, value, 0);
    }
    public static JSONObject createTextField(String fieldName, String fieldCaption, String value, int flags) {
        JSONObject f= null;
        try {
            f = new JSONObject();
            f.put("fieldType", "text");
            f.put("name", fieldName);
            f.put("text", value);
            f.put("flags", flags);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static void showYesNoDialog(Context c, String title, String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public interface JSONDialogListener {
        boolean onConfirm(JSONObject jsonResult);
        void onCancel();
    }

    protected JSONInputDialog(Context context) {
        super(context);
    }

    JSONDialogListener mListener;
    JSONObject input = null;
    JSONObject output = null;
    public JSONInputDialog(Context context, String jsonStrInput, JSONDialogListener listener) {
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

    public JSONInputDialog(Context context, JSONObject jsonObjInput, JSONDialogListener listener) {
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

    TextView txtTitle;
    TextView txtMsg;

    LinearLayout pnlContent;
    Button btnOK, btnCancel;
    Map<JSONObject, View> fieldViewMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_fragment_okcancel);
        txtMsg = (TextView) findViewById(R.id.txt_message);
        txtTitle= (TextView)findViewById(R.id.txt_title);
        pnlContent = (LinearLayout)findViewById(R.id.dlg_fragment_okcancel_content);
        btnOK = (Button)findViewById(R.id.dlg_fragment_okcancel_btn_ok);
        btnCancel = (Button)findViewById(R.id.dlg_fragment_okcancel_btn_cancel);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJSONOutput();
                boolean handled = mListener.onConfirm(output);
                if (handled)
                    dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        createViewFromJSON();

    }

    private void createViewFromJSON() {
        try {
            if (input.has("title")) {
                txtTitle.setText(input.getString("title"));
                txtTitle.setVisibility(View.VISIBLE);
            }
            else {
                txtTitle.setVisibility(View.GONE);
            }
            if (input.has("msg")) {
                txtMsg.setText(input.getString("msg"));
                txtMsg.setVisibility(View.VISIBLE);
            }
            else {
                txtMsg.setVisibility(View.GONE);
            }

            JSONArray fieldArray = input.getJSONArray("fields");
            for (int i = 0; i < fieldArray.length(); i++) {
                JSONObject fieldData = fieldArray.getJSONObject(i);
                output.put(fieldData.getString("name"), "");

                TextView txt = addTextView(fieldData.getString("name"));
                txt.setPadding(2, 20, 2, 2);
                View v = createViewByField(fieldData);

                if (v != null) {
                    fieldViewMap.put(fieldData, v);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    pnlContent.addView(v, lp);
                }
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private TextView addTextView(String text) {
        TextView v = new TextView(getContext());
        v.setText(text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pnlContent.addView(v, lp);
        return v;
    }

    void updateJSONOutput()  {
        try {
//            for (int i = 0; i < pnlContent.getChildCount(); i++) {
//                updateOutputField(pnlContent.getChildAt(i), output);
//            }
            for (JSONObject fieldData : fieldViewMap.keySet()
                 ) {
                String fieldType = fieldData.getString("fieldType");
                View view = fieldViewMap.get(fieldData);
                if (fieldType.equals("text")) {
                    output.put(fieldData.getString("name"), ((EditText)view).getText().toString().trim());
                }
                else if (fieldType.equals("spinner")) {
                    output.put(fieldData.getString("name"), ((Spinner)view).getSelectedItemPosition());
                }
                else if (fieldType.equals("checkbox")) {
                    output.put(fieldData.getString("name"), ((CheckBox)view).isChecked());
                }
                else if (fieldType.equals("stylelistpicker")) {
                    output.put(fieldData.getString("name"), "" + (String)view.getTag());
                }
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

//     void updateOutputField(View v, JSONObject output) throws JSONException {
//         if (v.getTag() == null) return;
//         if (v.getTag() instanceof JSONObject) {
//             JSONObject fieldData = (JSONObject) v.getTag();
//             String fieldType = fieldData.getString("fieldType");
//             if (fieldType.equals("text"))
//                output.put(fieldData.getString("name"), ((EditText)v).getText().toString().trim());
//             else if (fieldType.equals("spinner")) {
//                 output.put(fieldData.getString("name"), "" + ((Spinner)v).getSelectedItemPosition());
//             }
//             else if (fieldType.equals("checkbox")) {
//                 output.put(fieldData.getString("name"), "" + ((CheckBox)v).isChecked());
//             }
//             else if (fieldType.equals("colorlistpicker")) {
//                 output.put(fieldData.getString("name"), "" + ((CheckBox)v).isChecked());
//             }
//         }
//    }

     View createViewByField(JSONObject fieldData) throws JSONException {
        String fieldType = fieldData.getString("fieldType");
        if (fieldType.equals("text")) {
            EditText txt = new EditText(getContext());
            if (fieldData.has("flags"))
                txt.setInputType(fieldData.getInt("flags"));
            if (fieldData.has("text"))
                txt.setText(fieldData.getString("text"));
            txt.setTag(fieldData);
            txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
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
        else if (fieldType.equals("stylelistpicker")) {
            LayoutInflater inflater = getLayoutInflater();
            final FrameLayout hzCells = (FrameLayout)inflater.inflate(R.layout.hz_cells, null, false);
            LinearLayout cellContainer = (LinearLayout)hzCells.findViewById(R.id.hz_cells_item_container);
            final View selIndicator = hzCells.findViewById(R.id.hz_sel_indicator);
            String[] styles = fieldData.getString("stylelist").split(",");

            final FrameLayout hzContainer = (FrameLayout)hzCells.findViewById(R.id.hz_container);

            hzCells.setTag(styles[0]);
            if (fieldData.has("selectedindex"))
                hzCells.setTag(styles[fieldData.getInt("selectedindex")]);

            for (int i = 0; i < styles.length; i++) {
                TextView cell = new TextView(getContext());
                cell.setText("Abc");
                String[] colors = styles[i].split(":");
                cell.setBackgroundColor((int)Long.parseLong(colors[0], 16));
                cell.setTextColor((int)Long.parseLong(colors[1], 16));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(HZ_CELL_SIZE, HZ_CELL_SIZE);
                lp.setMargins(HZ_CELL_MARGIN,HZ_CELL_MARGIN,HZ_CELL_MARGIN,HZ_CELL_MARGIN);
                cellContainer.addView(cell, lp);
                cell.setTag(styles[i]);
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)selIndicator.getLayoutParams();
                        lp.setMargins(v.getLeft()-HZ_CELL_MARGIN,0,0,0);
                        hzContainer.updateViewLayout(selIndicator, lp);
                        hzCells.setTag(v.getTag());
                    }
                });
            }
            return  hzCells;
        }
        return null;
    }

    private int[] hexl2il(String[] items) {
        int[] ivals = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            ivals[i] = Integer.decode(items[i]);
        }
        return ivals;
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

    public static class Builder {
        Context c;
        JSONInputDialog dlg;
        JSONObject setting  = new JSONObject();
        JSONDialogListener listener;
        JSONArray arFields = new JSONArray();
        public void setListener(JSONDialogListener listener) {
            this.listener = listener;
        }

        public void addTextField(String fieldName, String defaultValue) {
            JSONObject fieldText = new JSONObject();
            try {
                fieldText.put("text", defaultValue);
                fieldText.put("fieldType", "text");
                fieldText.put("name", fieldName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arFields.put(fieldText);
        }

        public void addSpinner(String fieldName, String defaultValue, String[] values) {
            JSONObject fieldText = new JSONObject();
            try {
                fieldText.put("text", defaultValue);
                fieldText.put("fieldType", "spinner");
                fieldText.put("name", fieldName);
                JSONArray arValues = new JSONArray();
                for (String v : values)
                    arValues.put(v);
                fieldText.put("values", arValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arFields.put(fieldText);
        }

        public void addMultiselect(String groupName, String fieldName, String[] values) {
            JSONObject fieldText = new JSONObject();
            try {
                fieldText.put("fieldType", "multiselect");
                fieldText.put("name", fieldName);
                JSONArray arValues = new JSONArray();
                for (String v : values)
                    arValues.put(v);
                fieldText.put("values", arValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arFields.put(fieldText);
        }

        public Builder(Context c) {
            this.c = c;
            try {
                setting.put("title", "title");
                setting.put("msg", "");
                setting.put("fields", arFields);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public JSONInputDialog create() {
            dlg = new JSONInputDialog(c, setting, listener);
            return dlg;
        }
    }
}

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tml.libs.cui.cardlist.CardListAdapter;
import com.tml.libs.cui.cardlist.CardListItemModel;
import com.tml.libs.cui.cardlist.CardText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        JSONObject f= null;
        try {
            f = new JSONObject();
            f.put("fieldType", "text");
            f.put("name", fieldName);
            f.put("text", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * use json.getString("Options") to get list of selected indices. ex: 0,1,2
     *
     * @param ctx
     * @param title
     * @param options
     * @param allowMultipleChoice
     * @param listener
     * @return
     */
    public static Dialog createOptionSelectionDialog(Context ctx, String title, String[] options, boolean allowMultipleChoice, JSONDialogListener listener) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", title);
            obj.put("msg", "");
            JSONArray fields = new JSONArray();

            JSONObject fieldOptionsList = new JSONObject();
            fieldOptionsList.put("fieldType", "option.list");
            fieldOptionsList.put("name", "Options");
            fieldOptionsList.put("label", "");
            fieldOptionsList.put("hide.label", true);
            fieldOptionsList.put("multiple", allowMultipleChoice);
            JSONArray arOptions = new JSONArray();
            for (String opt : options)
                arOptions.put(opt);
            fieldOptionsList.put("values", arOptions);
            fields.put(fieldOptionsList);

            obj.put("fields", fields);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONInputDialog dlg = new JSONInputDialog(ctx, obj, listener);
        return dlg;
    }

    public static void createMessageBoxOK(Context c, String title, String msg, OnClickListener okListener) {
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setTitle(title);
        b.setCancelable(false);
        b.setMessage(msg);
        b.setPositiveButton("OK", okListener);
        b.create().show();
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
                if (fieldData.has("label"))
                    txt.setText(fieldData.getString("label"));

                if (fieldData.has("hide.label")) {
                    if (fieldData.getBoolean("hide.label"))
                        txt.setVisibility(View.GONE);
                }

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

    Map<String, List<String>> listSelectionMap = new HashMap<>();


    void updateJSONOutput()  {
        try {
//            for (int i = 0; i < pnlContent.getChildCount(); i++) {
//                updateOutputField(pnlContent.getChildAt(i), output);
//            }
            for (JSONObject fieldData : fieldViewMap.keySet()
                 ) {
                String fieldType = fieldData.getString("fieldType");
                View view = fieldViewMap.get(fieldData);

                if (fieldType.equals("option.list")) {
                    String selectedIDs = "";
                    for (String id : listSelectionMap.get(fieldData.getString("name"))) {
                        if (selectedIDs.length() > 0)
                            selectedIDs += ",";
                        selectedIDs += id;
                    }
                    output.put(fieldData.getString("name"), selectedIDs);
                }
                else if (fieldType.equals("text")) {
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
        else if (fieldType.equals("option.list")) {
            RecyclerView rv = new RecyclerView(getContext());
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(createOptionListAdapter(fieldData));
            return rv;
//            ListView lv = new ListView(getContext());
//            ArrayAdapter<String> aa = createListViewAdapter(fieldData);
//            lv.setAdapter(aa);
//            lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//            return lv;
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



    private RecyclerView.Adapter createOptionListAdapter(final JSONObject fieldData) {

        CardListAdapter<CardText> rva = null;
        try {
            final List<String> options = new ArrayList<>();
            final String fieldName = fieldData.getString("name");
            final boolean multiSelect = fieldData.getBoolean("multiple");
            JSONArray arValues = null;


            listSelectionMap.put(fieldName, new ArrayList<String>());
            arValues = fieldData.getJSONArray("values");
            for (int i = 0; i < arValues.length(); i++) {
                options.add((i+1) + ". " + arValues.getString(i));
            }

            rva = new CardListAdapter<CardText>(R.layout.lvi_card_small,
                    new CardListAdapter.CardListModelProvider<CardText>() {
                @Override
                public int size() {
                    return options.size();
                }

                @Override
                public CardText getModel(int position) {
                    CardText card = new CardText("", options.get(position));
                    return card;
                }
            }, new CardListAdapter.CardClickListener() {
                @Override
                public void onClickCard(int index) {
                    List<String> ls = listSelectionMap.get(fieldName);
                    if (multiSelect) {
                        if (ls.contains("" + index))
                            ls.remove("" + index);
                        else
                            ls.add("" + index);
                    }
                    else {
                        ls.clear();
                        ls.add("" + index);

                    }
                }
                @Override
                public boolean onLongClickCard(int index) {
                    return false;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        rva.showSelection = true;
        rva.setVisualStyle(CardListItemModel.VSNAME_NORMAL, getContext().getResources().getDrawable(R.drawable.card_round_box_normal));
        rva.setVisualStyle(CardListItemModel.VSNAME_SELECTED, getContext().getResources().getDrawable(R.drawable.card_yellow));
        return rva;
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

    private ArrayAdapter<String> createListViewAdapter(JSONObject fieldData) throws JSONException {
        JSONArray arValues = fieldData.getJSONArray("values");
        String[] items = new String[arValues.length()];
        for (int i = 0; i < arValues.length(); i++) {
            items[i] = (i + 1) + ". " + arValues.getString(i);
        }
        return new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                items
        );

    }
}

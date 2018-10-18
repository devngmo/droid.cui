package com.tml.libs.cui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.tml.libs.cui.JSONEditorDocument;
import com.tml.libs.cui.JSONInputDialog;
import com.tml.libs.cui.R;
import com.tml.libs.cui.cardlist.CardListAdapter;
import com.tml.libs.cui.cardlist.CardText;
import com.tml.libs.cui.cardlist.FragmentCardList;
import com.tml.libs.cutils.StaticLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JSONEditorWebFragment extends Fragment {
    public static final String ARG_JSON_DATA = "data";
    public static final String ARG_JSON_SCHEMA = "schema";
    public static final String ARG_USE_SCHEMA = "use.schema";
    public static final String ARG_ROOT_DISPLAY_NAME = "root.display.name";
    JSONEditorDocument doc;
    WebView wv;
    public JSONEditorDocument getDoc() {
        return doc;
    }

    public JSONEditorWebFragment() {
    }

    public static JSONEditorWebFragment newInstance(String data, String schema, boolean useSchema) {
        JSONEditorWebFragment fragment = new JSONEditorWebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JSON_DATA, data);
        args.putString(ARG_JSON_SCHEMA, schema);
        args.putBoolean(ARG_USE_SCHEMA, useSchema);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doc = new JSONEditorDocument();
            try {
                doc.setData(new JSONObject(getArguments().getString(ARG_JSON_DATA)));
                String schema = getArguments().getString(ARG_JSON_SCHEMA);
                if (schema != null)
                    doc.setSchema(new JSONObject(schema));
                doc.setUseSchema(getArguments().getBoolean(ARG_USE_SCHEMA));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jsoneditor_web, container, false);
        wv = root.findViewById(R.id.jsonEditorWeb);
        reloadWebView();
        return root;
    }

    private void reloadWebView() {
        wv.loadDataWithBaseURL("", "", "", "", null);
    }
}

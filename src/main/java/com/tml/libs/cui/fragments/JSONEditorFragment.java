package com.tml.libs.cui.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tml.libs.cui.JSONEditorDocument;
import com.tml.libs.cui.JSONInputDialog;
import com.tml.libs.cui.R;
import com.tml.libs.cui.cardlist.CardListAdapter;
import com.tml.libs.cui.cardlist.CardListItemModel;
import com.tml.libs.cui.cardlist.CardText;
import com.tml.libs.cui.cardlist.FragmentCardList;
import com.tml.libs.cutils.JSONUtils;
import com.tml.libs.cutils.StaticLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JSONEditorFragment extends Fragment {
    public boolean isCurrentNodeWasObject() {
        return curNode instanceof JSONObject;
    }

    public class BrowseTreeNode {
        public String id;
        public Object object;
        private static final String NODEID_ROOT = "__root__";
        private static final String NODEID_PREFIX_ARRAY_ITEM = "__arit";

        public BrowseTreeNode(String id, Object object) {
            this.id = id;
            this.object = object;
        }
    }
    public static final String ARG_JSON_DATA = "data";
    public static final String ARG_JSON_SCHEMA = "schema";
    public static final String ARG_USE_SCHEMA = "use.schema";
    public static final String ARG_ROOT_DISPLAY_NAME = "root.display.name";

    CardListAdapter<CardText> rvA;
    Object curNode;
    List<String> curNodeKeys = new ArrayList<>();
    List<BrowseTreeNode> nodePath = new ArrayList<>();

    TextView txtPath;
    String rootDisplayName = "root";

    JSONEditorDocument doc;
    public JSONEditorDocument getDoc() {
        return doc;
    }

    public JSONEditorFragment() {
    }

    public static JSONEditorFragment newInstance(String data, String schema, boolean useSchema, String rootDisplayName) {
        JSONEditorFragment fragment = new JSONEditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JSON_DATA, data);
        args.putString(ARG_JSON_SCHEMA, schema);
        args.putBoolean(ARG_USE_SCHEMA, useSchema);
        args.putString(ARG_ROOT_DISPLAY_NAME, rootDisplayName);
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
                rootDisplayName = getArguments().getString(ARG_ROOT_DISPLAY_NAME);
                nodePath.clear();
                openChild(new BrowseTreeNode(BrowseTreeNode.NODEID_ROOT, doc.getData()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jsoneditor, container, false);

        txtPath = root.findViewById(R.id.browseNodePath);
        FragmentCardList frag = FragmentCardList.newInstance(createAdapter());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rv_marker, frag).commit();


        updateBroweNodePathText();
        return root;
    }

    private RecyclerView.Adapter<CardListAdapter.CardItemHolder> createAdapter() {
        rvA = new CardListAdapter<CardText>(
                R.layout.card_medium_text,
                new CardListAdapter.CardListModelProvider<CardText>() {
                    @Override
                    public int size() {
                        if (curNode instanceof  JSONObject)
                            return curNodeKeys.size();
                        return ((JSONArray)curNode).length();
                    }

                    @Override
                    public CardText getModel(int position) {
                        try {
                            if (curNode instanceof JSONObject) {
                                JSONObject curObject = (JSONObject)curNode;
                                String key = curNodeKeys.get(position);
                                Object fieldObject = curObject.get(key);
                                return createCardTextFromObject(fieldObject, key, position);
                            }
                            else {
                                // array
                                JSONArray curArray = (JSONArray)curNode;
                                Object item = curArray.get(position);
                                return createCardTextFromObject(item, null, position);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                },
                new CardListAdapter.CardClickListener() {
                    @Override
                    public void onClickCard(int index) {
                        onClickField(index);
                    }

                    @Override
                    public boolean onLongClickCard(int index) {
                        return false;
                    }
                }
        );

        rvA.setVisualStyle(CardListItemModel.VSNAME_NORMAL, getResources().getDrawable(R.drawable.card_round_box_normal));
        return rvA;
    }

    private CardText createCardTextFromObject(Object o, String key, int index) throws JSONException {
        CardText c = new CardText("", "");
        String prefix = key + ": ";
        if (key == null)
            prefix = "";

        String desc = "";
        if (o instanceof JSONObject) {
            desc = o.toString();

        } else if (o instanceof JSONArray) {
            desc = o.toString();
        } else {
            desc = "" + o;
        }
        if (desc.length() > 40)
            desc = desc.substring(0, 40) + "...";
        c.setText(prefix + desc);
        return c;
    }

    private void onClickField(int index) {

        try {
            if (curNode instanceof JSONObject) {
                String key = curNodeKeys.get(index);
                JSONObject o = (JSONObject)curNode;
                Object item = o.get(key);
                if (item instanceof JSONObject) {
                    openChild(new BrowseTreeNode(key, item));
                } else if (item instanceof JSONArray) {
                    openChild(new BrowseTreeNode(key, item));
                } else {
                    startEditSelectedField(key);
                }
            }
            else if (curNode instanceof  JSONArray) {
                Object o = ((JSONArray)curNode).get(index);
                if (o instanceof JSONObject)
                    openChild(new BrowseTreeNode(BrowseTreeNode.NODEID_PREFIX_ARRAY_ITEM + index, o));
                else if (o instanceof JSONArray)
                    openChild(new BrowseTreeNode(BrowseTreeNode.NODEID_PREFIX_ARRAY_ITEM + index, o));
                else
                    startEditArrayTextElement(index);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startEditArrayTextElement(final int index) throws JSONException {
        String oldText = (String)((JSONArray)curNode).get(index);
        JSONInputDialog.createOneTextInputDialog(getActivity(), "Edit Element " + index, "Old: " + oldText,
                "Text", new JSONInputDialog.JSONDialogListener() {
                    @Override
                    public boolean onConfirm(JSONObject jsonResult) {
                        try {
                            String val = jsonResult.getString("Text");
                            ((JSONArray)curNode).put(index, val);
                            rvA.notifyDataSetChanged();

                            StaticLogger.D(JSONEditorFragment.this, doc.getData().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    private void startEditSelectedField(final String key) throws JSONException {
        JSONObject o = (JSONObject)curNode;
        JSONInputDialog.createOneTextInputDialog(getActivity(), key, "Old: " + o.getString(key),
                key, new JSONInputDialog.JSONDialogListener() {
            @Override
            public boolean onConfirm(JSONObject jsonResult) {
                try {
                    String val = jsonResult.getString(key);
                    ((JSONObject)curNode).put(key, val);
                    rvA.notifyDataSetChanged();

                    StaticLogger.D(JSONEditorFragment.this, doc.getData().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public void onCancel() {

            }
        }).show();
    }

    BrowseTreeNode curBrowseTreeNode() {
        return nodePath.get(nodePath.size()-1);
    }

    void onCurrentNodeChanged() {
        curNodeKeys = new ArrayList<>();

        updateBroweNodePathText();

        if (curNode instanceof JSONObject) {
            JSONObject o = (JSONObject)curNode;
            StaticLogger.D(this, "onCurrentNodeChanged " + curBrowseTreeNode().id);

            Iterator<String> it = o.keys();
            while (it.hasNext()) {
                String key = it.next();
                curNodeKeys.add(key);
            }
        }
        else if (curNode instanceof JSONArray) {

        }
        if (rvA != null)
            rvA.notifyDataSetChanged();
    }

    private void updateBroweNodePathText() {
        String path = "";
        if (txtPath == null) return;
        for (BrowseTreeNode n : nodePath
             ) {
            if (n.id.equals(BrowseTreeNode.NODEID_ROOT)) {
                path = rootDisplayName;
            }
            else {
                if (n.id.startsWith(BrowseTreeNode.NODEID_PREFIX_ARRAY_ITEM))
                    path += ">" + n.id.substring(BrowseTreeNode.NODEID_PREFIX_ARRAY_ITEM.length());
                else
                    path += ">" + n.id;
            }
        }
        txtPath.setText(path);
    }

    public void openChild(BrowseTreeNode bn) {
        nodePath.add(bn);
        curNode = bn.object;
        onCurrentNodeChanged();
    }

    public boolean handleKeyBack() {
        if (nodePath.size() > 1) {
            nodePath.remove(nodePath.size()-1);
            curNode = nodePath.get(nodePath.size()-1).object;
            onCurrentNodeChanged();
            return true;
        }
        else
            return false;
    }

    public void openChildInCurrentObject(String keyName) throws JSONException {
        if (curNode instanceof  JSONObject)
            openChild(new BrowseTreeNode(keyName, ((JSONObject)curNode).get(keyName)));
    }

    public void openChildInCurrentArray(int index) throws JSONException {
        if (curNode instanceof  JSONArray)
            openChild(new BrowseTreeNode(BrowseTreeNode.NODEID_PREFIX_ARRAY_ITEM + index, ((JSONArray)curNode).get(index)));
    }

    public void addNewValue(String name, String value) throws JSONException {
        if (curNode instanceof JSONObject) {
            ((JSONObject) curNode).put(name, value);
            curNodeKeys.add(name);
        }
        else if (curNode instanceof  JSONArray) {
            ((JSONArray) curNode).put(value);
        }
    }

    public BrowseTreeNode addNewObject(String name) throws JSONException {
        JSONObject obj = new JSONObject();
        if (curNode instanceof JSONObject) {
            ((JSONObject)curNode).put(name, obj);
            curNodeKeys.add(name);
            return new BrowseTreeNode(name, obj);
        }
        else {
            ((JSONArray)curNode).put(obj);
            int index = ((JSONArray)curNode).length()-1;
            return new BrowseTreeNode(BrowseTreeNode.NODEID_PREFIX_ARRAY_ITEM + index, obj);
        }
    }

    public void notifyDataChanged() {
        rvA.notifyDataSetChanged();
    }

    public BrowseTreeNode addNewArray(String name) throws JSONException {
        JSONArray newAr = new JSONArray();
        if (curNode instanceof JSONObject) {
            ((JSONObject)curNode).put(name, newAr);
            curNodeKeys.add(name);

            return new BrowseTreeNode(name, newAr);
        }
        else {
            ((JSONArray)curNode).put(newAr);
            int index = ((JSONArray)curNode).length()-1;
            return new BrowseTreeNode( BrowseTreeNode.NODEID_PREFIX_ARRAY_ITEM + index, newAr);
        }
    }
}

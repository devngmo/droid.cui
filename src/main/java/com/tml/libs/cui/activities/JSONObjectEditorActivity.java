package com.tml.libs.cui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.tml.libs.cui.JSONEditorDocument;
import com.tml.libs.cui.JSONInputDialog;
import com.tml.libs.cui.R;
import com.tml.libs.cui.fragments.JSONEditorFragment;
import com.tml.libs.cutils.StaticLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONObjectEditorActivity extends AppCompatActivity {
    public  interface EditorMenuListener {
        /**
         *
         * @param sender
         * @param doc
         * @return True if this Activity should Finish after Save
         */
        boolean onSave(JSONObjectEditorActivity sender, JSONEditorDocument doc);
    }

    private static final String ARG_INSTANCE_UID = "instance.uid";

    static int instanceUID = 0;
    EditorMenuListener listener = null;

    static Map<String, EditorMenuListener> listenerRegisterMap = new HashMap<>();

    public void setListener(EditorMenuListener listener) {
        this.listener = listener;
    }


    JSONEditorFragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonobject);

        frag = JSONEditorFragment.newInstance(
                getIntent().getExtras().getString(JSONEditorFragment.ARG_JSON_DATA),
                getIntent().getExtras().getString(JSONEditorFragment.ARG_JSON_SCHEMA),
                getIntent().getExtras().getBoolean(JSONEditorFragment.ARG_USE_SCHEMA),
                getIntent().getExtras().getString(JSONEditorFragment.ARG_ROOT_DISPLAY_NAME)
        );
        String instanceUID = getIntent().getExtras().getString(ARG_INSTANCE_UID);
        listener = listenerRegisterMap.get(instanceUID);
        listenerRegisterMap.remove(instanceUID);

        if (listener == null)
            StaticLogger.E(this, "Listener was not set");

        getSupportFragmentManager().beginTransaction().replace(R.id.content, frag).commit();
    }


    public static void start(Context context, String data, String schema, boolean useSchema, String rootDisplayName, EditorMenuListener listener) {
        Intent starter = new Intent(context, JSONObjectEditorActivity.class);
        starter.putExtra(JSONEditorFragment.ARG_JSON_DATA, data);
        starter.putExtra(JSONEditorFragment.ARG_JSON_SCHEMA, schema);
        starter.putExtra(JSONEditorFragment.ARG_USE_SCHEMA, useSchema);
        starter.putExtra(JSONEditorFragment.ARG_ROOT_DISPLAY_NAME, rootDisplayName);
        starter.putExtra(ARG_INSTANCE_UID, "" + instanceUID);

        listenerRegisterMap.put("" + instanceUID, listener);
        instanceUID++;
        context.startActivity(starter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.json_object_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            boolean shouldFinish = listener.onSave(this, frag.getDoc());
            if (shouldFinish)
                finish();
            return true;
        }
        else if (id == R.id.action_add_value) {
            if (frag.isCurrentNodeWasObject()) {
                JSONInputDialog.createManyTextInputDialog(this, "Add New Value", "",
                        new String[]{"Name", "Value"},
                        new String[]{"new name", "new value"}, new JSONInputDialog.JSONDialogListener() {
                            @Override
                            public boolean onConfirm(JSONObject jsonResult) {
                                try {
                                    frag.addNewValue(jsonResult.getString("Name"), jsonResult.getString("Value"));
                                    frag.notifyDataChanged();
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
            else {
                JSONInputDialog.createOneTextInputDialog(this, "Add String to Array", "",
                        "Text", new JSONInputDialog.JSONDialogListener() {
                            @Override
                            public boolean onConfirm(JSONObject jsonResult) {
                                try {
                                    frag.addNewValue(null, jsonResult.getString("Text"));
                                    frag.notifyDataChanged();
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
            return true;
        }
        else if (id == R.id.action_add_array) {
            JSONInputDialog.createOneTextInputDialog(this, "Add New Array", "",
                    "Name", new JSONInputDialog.JSONDialogListener() {
                        @Override
                        public boolean onConfirm(JSONObject jsonResult) {
                            try {
                                JSONEditorFragment.BrowseTreeNode b = frag.addNewArray(jsonResult.getString("Name"));
                                frag.openChild(b);
                                frag.notifyDataChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }

                        @Override
                        public void onCancel() {

                        }
                    }).show();
            return true;
        }
        else if (id == R.id.action_add_object) {
            if (frag.isCurrentNodeWasObject()) {
                JSONInputDialog.createOneTextInputDialog(this, "Add New Object", "",
                        "Name", new JSONInputDialog.JSONDialogListener() {
                            @Override
                            public boolean onConfirm(JSONObject jsonResult) {
                                try {
                                    JSONEditorFragment.BrowseTreeNode b = frag.addNewObject(jsonResult.getString("Name"));
                                    frag.openChild(b);
                                    frag.notifyDataChanged();
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
            else {
                JSONEditorFragment.BrowseTreeNode b = null;
                try {
                    b = frag.addNewObject("");
                    frag.openChild(b);
                    frag.notifyDataChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (frag.handleKeyBack())
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

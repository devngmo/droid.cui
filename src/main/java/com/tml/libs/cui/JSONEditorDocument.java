package com.tml.libs.cui;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by TML on 18/01/2018.
 */

public class JSONEditorDocument {
    JSONObject data;
    JSONObject schema = null;


    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setSchema(JSONObject schema) {
        this.schema = schema;
    }

    boolean useSchema = false;

    public boolean isUseSchema() {
        return useSchema;
    }

    public void setUseSchema(boolean useSchema) {
        this.useSchema = useSchema;
    }

    public JSONEditorDocument() {

    }
}

package com.example.gymdiary;


import org.json.JSONObject;

import java.util.List;

public interface ServerCallback {
    void onSuccess(List<JSONObject> list);
    void onSuccess(JSONObject object);
    void onSuccess(String response);
    void onFail();
}

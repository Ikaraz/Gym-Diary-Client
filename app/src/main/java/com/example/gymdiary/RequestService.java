package com.example.gymdiary;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestService {
    private static Context context;
    private static RequestService instance;
    private RequestQueue requestQueue;
    private String herokuUrl = "https://gym-diary.herokuapp.com";


    private RequestService(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestService getInstance(Context context) {
        if (instance == null) {
            instance = new RequestService(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }




    //Request Methods Exercises

    public void findExerciseByUserIdAndDate(int userId, String date, final ServerCallback callback) {
        final List<JSONObject> exercisesList = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, herokuUrl+"/users/"+userId+"/exercises/date/"+ date, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        exercisesList.add(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.onSuccess(exercisesList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
    public void insertOneExercise(final int userId, final String name, final int reps, final double weight, final String date, final ServerCallback callback){
        StringRequest request = new StringRequest(Request.Method.POST, herokuUrl+"/users/"+userId+"/exercises", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                String data = "{\"name\":\""+name+"\", \"reps\":"+reps+", \"weight\":"+weight+", \"date\":\""+date+"\"}";
                return data.getBytes();
            }
        };
        requestQueue.add(request);
        requestQueue.start();
    }
    public void updateOneExercise(final int userId, final int id, final String name, final int reps, final double weight, final String date, final ServerCallback callback){
        StringRequest request = new StringRequest(Request.Method.PUT, herokuUrl+"/users/"+userId+"/exercises", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                String data = "{\"id\":"+id+", \"name\":\""+name+"\", \"reps\":"+reps+", \"weight\":"+weight+", \"date\":\""+date+"\"}";
                return data.getBytes();
            }
        };
        requestQueue.add(request);
        requestQueue.start();
    }
    public void deleteOneExercise(int id, final ServerCallback callback){
        StringRequest request = new StringRequest(Request.Method.DELETE, herokuUrl+"/users/exercises/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
    public void findExerciseByUserIdAndName(int userId, String name, final ServerCallback callback){
        final List<JSONObject> list = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, herokuUrl+"/users/"+userId+"/exercises/" + name, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        list.add(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.onSuccess(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }







    //Request Methods User
    void findUserByEmail(String email, final ServerCallback callback){
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, herokuUrl+"/users/" + email, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    public void insertOneUser(final String name, final String email, final String password, final ServerCallback callback){
        StringRequest request = new StringRequest(Request.Method.POST, herokuUrl+"/users", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {

                String data = "{\"name\":\""+name+"\", \"email\":\""+email+"\", \"password\":\""+password+"\"}";
                return data.getBytes();
            }
        };
        requestQueue.add(request);
        requestQueue.start();
    }
    //TODO: create activities to update and delete user
    public void updateOneUser(final int id, final String name, final String email, final String password, final ServerCallback callback){
        StringRequest request = new StringRequest(Request.Method.PUT, herokuUrl+"/users", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                String data = "{\"id\":\""+id+"\", \"name\":\""+name+"\", \"email\":\""+email+"\", \"password\":\""+password+"\"}";
                return data.getBytes();
            }
        };
        requestQueue.add(request);
        requestQueue.start();
    }
    public void deleteOneUser(int id, final ServerCallback callback){
        StringRequest request = new StringRequest(Request.Method.DELETE, herokuUrl+"/users/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onFail();
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }


}

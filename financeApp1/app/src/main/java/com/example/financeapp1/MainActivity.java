package com.example.financeapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);

        textView = findViewById(R.id.text);
        jsonParse("egal");
    }

    public void jsonParse(String input){
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + "MSFT" + "&apikey=TF4MI722MXFUINJH";
        JSONArray jsonArray = new JSONArray();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("Global Quote");
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("symbol", jsonObject.getString("01. symbol"));
                    String beispiel = jsonObject1.getString("symbol");
                    textView.setText(beispiel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        mQueue.add(request);
    }
}

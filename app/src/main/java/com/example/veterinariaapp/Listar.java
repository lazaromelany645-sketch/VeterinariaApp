package com.example.veterinariaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Listar extends AppCompatActivity {


    ListView lstMascotas;

    private final String URL = "http://localhost:3000/mascotas";

    RequestQueue requestQueue;

    public void loadUI() {
        lstMascotas = findViewById(R.id.lstMascota);
    }

    public void renderData(JSONArray jsonArray) {
        try {
            ArrayAdapter arrayAdapter;
            ArrayList<String> listaMascotas = new ArrayList<>();

            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listaMascotas.add(jsonObject.getString("nombre"));
            }

            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaMascotas);
            lstMascotas.setAdapter(arrayAdapter);

        } catch (Exception error) {
            Log.e("Error en el Json", error.toString());
        }
    }

    private void getData() {
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("Datos recibidos", jsonArray.toString());
                        renderData(jsonArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Error WS", volleyError.toString());
                    }
                }
        );


        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();
        getData();
    }
}
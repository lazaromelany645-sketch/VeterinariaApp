package com.example.veterinariaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Registro extends AppCompatActivity {

    private final String URL = "http://localhost:3000/mascotas";

    EditText edtNombreMascota, edtTipoDeMascota, edtRazaDeMascota, edtColorDeMascota, edtPesoDeLaMascota, edtGeneroDeLaMascota;
    Button btnGuardar;

    RequestQueue requestQueue;

    public void loadUI() {
        edtNombreMascota = findViewById(R.id.edtNombreMascota);
        edtTipoDeMascota = findViewById(R.id.edtTipoDeMascota);
        edtRazaDeMascota = findViewById(R.id.edtRazaDeMascota);
        edtColorDeMascota = findViewById(R.id.edtColorDeMascota);
        edtPesoDeLaMascota = findViewById(R.id.edtPesoDeLaMascota);
        edtGeneroDeLaMascota = findViewById(R.id.edtGeneroDeLaMascota);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void sendDaraWS() {
        requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("nombre", edtNombreMascota.getText().toString());
            jsonObject.put("tipo", edtTipoDeMascota.getText().toString());
            jsonObject.put("raza", edtRazaDeMascota.getText().toString());
            jsonObject.put("color", edtColorDeMascota.getText().toString());
            jsonObject.put("peso", edtPesoDeLaMascota.getText().toString());
            jsonObject.put("genero", edtGeneroDeLaMascota.getText().toString());
        } catch (Exception exception) {
            Log.e("Error JSON envió", exception.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("ID obtenido", jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Error de envió", volleyError.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
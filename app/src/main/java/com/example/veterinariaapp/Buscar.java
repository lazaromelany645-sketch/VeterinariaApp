package com.example.veterinariaapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Buscar extends AppCompatActivity {

    EditText edtIdBuscado, edtNombreEdit, edtTipoEdit, edtRazaEdit, edtColorEdit, edtPesoEdit, edtGeneroEdit;
    Button btnBuscado, btnActualizarMascota, btnEliminarMascota;

    RequestQueue requestQueue;
    private final String URL = "http://localhost:3000/mascotas/";

    public void loadUI() {
        edtIdBuscado = findViewById(R.id.edtIdBuscado);
        edtNombreEdit = findViewById(R.id.edtNombreEdit);
        edtTipoEdit = findViewById(R.id.edtTipoEdit);
        edtRazaEdit = findViewById(R.id.edtRazaEdit);
        edtColorEdit = findViewById(R.id.edtColorEdit);
        edtPesoEdit = findViewById(R.id.edtPesoEdit);
        edtGeneroEdit = findViewById(R.id.edtGeneroEdit);
        btnBuscado = findViewById(R.id.btnBuscado);
        btnActualizarMascota = findViewById(R.id.btnActualizarMascota);
        btnEliminarMascota = findViewById(R.id.btnEliminarMascota);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();

        btnBuscado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchById();
            }
        });

        btnActualizarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmUpdate();
            }
        });

        btnEliminarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
            }
        });
    }

    private void formClear() {
        edtIdBuscado.setText("");
        edtNombreEdit.setText("");
        edtTipoEdit.setText("");
        edtRazaEdit.setText("");
        edtColorEdit.setText("");
        edtPesoEdit.setText("");
        edtGeneroEdit.setText("");
    }

    private void searchById() {
        String idMascota = edtIdBuscado.getText().toString().trim();

        if (idMascota.isEmpty()) {
            edtIdBuscado.setError("Escriba el ID");
            edtIdBuscado.requestFocus();
        } else {
            requestQueue = Volley.newRequestQueue(this);
            String enpoind =  URL + idMascota;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    enpoind,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.e("Respesta WS", jsonObject.toString());
                            try {
                                edtNombreEdit.setText(jsonObject.getString("nombre"));
                                edtTipoEdit.setText(jsonObject.getString("tipo"));
                                edtRazaEdit.setText(jsonObject.getString("raza"));
                                edtColorEdit.setText(jsonObject.getString("color"));
                                edtPesoEdit.setText(jsonObject.getString("peso"));
                                edtGeneroEdit.setText(jsonObject.getString("genero"));
                            } catch (JSONException error) {
                                Log.e("Error JSON: ", error.toString());
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            formClear();
                            edtIdBuscado.requestFocus();
                            Toast.makeText(getApplicationContext(), "La mascota no existe", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
        }
    }

    private void confirmUpdate() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Mantemieminto de la mascota");
        dialog.setMessage("¿Procedemos con la actualizacion?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("Cancelar", null);

        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateMascota();
            }
        });

        dialog.show();
    }

    private void updateMascota() {
        requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("nombre", edtNombreEdit.getText().toString().trim());
            jsonObject.put("tipo", edtTipoEdit.getText().toString().trim());
            jsonObject.put("raza", edtRazaEdit.getText().toString().trim());
            jsonObject.put("color", edtColorEdit.getText().toString().trim());
            jsonObject.put("peso", edtPesoEdit.getText().toString().trim());
            jsonObject.put("genero", edtGeneroEdit.getText().toString().trim());
        } catch (JSONException error) {
            Log.e("Error JSON", error.toString());
        }

        String endpoint = URL + edtIdBuscado.getText().toString().trim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                endpoint,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getApplicationContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void confirmDelete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Se eliminara este vehiculo");
        dialog.setMessage("¿Procedemos con la actualizacion?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("Cancelar", null);

        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMascota();
            }
        });

        dialog.show();
    }

    private void deleteMascota() {
        requestQueue = Volley.newRequestQueue(this);

        String endpoint = URL + edtIdBuscado.getText().toString().trim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                endpoint,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getApplicationContext(), "La mascota elimino correctamente", Toast.LENGTH_SHORT).show();
                        Log.d("Se elimino correctamente", jsonObject.toString());
                        formClear();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                        Log.e("Error WS", volleyError.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
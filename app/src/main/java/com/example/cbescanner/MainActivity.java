package com.example.cbescanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    EditText txtResultado;
    EditText txtCantidad;
    EditText txtDescripcion;
    EditText txtDate;
    ImageButton btnSave;
    ImageButton btnCancel;
    Button btnBuscar;
    ListView lvListaCR;
    Button btnSincronizar;
    ArrayList<String> lngList;
    ProgressBar pbSinc;
    ArrayAdapter<String> adapter;
    Integer id_user;
    Integer id_producto;

    private SyncTask syncTask;
    private SearchTask searchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan         = findViewById(R.id.btnScan);
        txtResultado    = findViewById(R.id.txtResultado);
        txtCantidad     = findViewById(R.id.txtCantidad);
        txtDescripcion  = findViewById(R.id.txtDescripcion);
        txtDate         = findViewById(R.id.txtDate);
        btnSave         = findViewById(R.id.btnSave);
        btnCancel       = findViewById(R.id.btnCancel);
        btnBuscar       = findViewById(R.id.btnBuscar);
        lvListaCR       = findViewById(R.id.lvListaCR);
        btnSincronizar  = findViewById(R.id.btnSincronizar);
        pbSinc          = findViewById(R.id.pbSinc);
        lngList         = new ArrayList();

        // Obtenemos el id del usuario enviado desde el login
        id_user = Integer.valueOf(Objects.requireNonNull(getIntent().getStringExtra("id")));

        //TODO: DELETE - carga de ejemplo
        //Log.d("llenamos la lista de prueba","Listo");
        //for (int i = 60; i < 61; i++) {
          //  lngList.add("1234567890"+i+" - "+i+" - producto "+i);
        //}

        btnSincronizar.setEnabled(false);//cambiar a false antes de liberar
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lngList);
        lvListaCR.setAdapter(adapter);

        btnScan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               IntentIntegrator integrador = new IntentIntegrator(MainActivity.this);
               integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
               integrador.setPrompt("Lector - CDP");
               integrador.setCameraId(0);
               integrador.setBeepEnabled(true);
               integrador.setBarcodeImageEnabled(true);
               integrador.initiateScan();
           }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = getID_producto();
                String cb   = txtResultado.getText().toString().trim();
                String desc = txtDescripcion.getText().toString().trim();
                String cant = txtCantidad.getText().toString().trim();
                String date = txtDate.getText().toString().trim();

                if (!cb.isEmpty()) {
                    if (!desc.isEmpty()) {
                        if (!cant.isEmpty()) {
                            lngList.add(cb + " - " + cant + " - " + desc + " - " + date + " - " + id);
                            adapter.notifyDataSetChanged();
                            //vaciamos los EditText
                            txtResultado.setText("");
                            txtDescripcion.setText("");
                            txtCantidad.setText("");
                            txtDate.setText("");
                            setID_producto(null);
                            btnSincronizar.setEnabled(true);
                        }
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vaciamos los EditText
                txtResultado.setText("");
                txtDescripcion.setText("");
                txtCantidad.setText("");
            }
        });

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar el proceso de sincronización en un hilo separado utilizando SyncTask
                syncTask = new SyncTask(MainActivity.this);
                new Thread(syncTask).start();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSincronizar.setEnabled(false);
                btnBuscar.setEnabled(false);
                // Iniciar el proceso de busqueda en un hilo separado utilizando SyncTask
                searchTask = new SearchTask(MainActivity.this);
                new Thread(searchTask).start();
            }
        });
    }

    // Add getter methods for the required variables used in SyncTask
    public ArrayList<String> getLngList() {
        return lngList;
    }

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public Button getBtnSincronizar() {
        return btnSincronizar;
    }

    public Button getBtnScan() { return btnScan; }

    public ProgressBar getpbSinc() {
        return pbSinc;
    }

    public String getTxtResultado() {
        return txtResultado.getText().toString();
    }

    public void setTxtCantidad(String cantidad) {
        this.txtCantidad.setText(cantidad);
    }

    public void setTxtDescripcion(String descripcion) {
        this.txtDescripcion.setText(descripcion);
    }

    public void setTxtDescripcionBlocked(boolean isEnable) {
        txtDescripcion.setEnabled(isEnable);
    }

    public void setTxtDate(String date) {
        txtDate.setText(date);
    }

    public Button getBtnBuscar () {
        return btnBuscar;
    }

    public Integer getID_producto() {
        return id_producto;
    }

    public void setID_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    // TODO: guardar el id del producto y hacer validaciones para saber si se actualiza o se genera uno nuevo

    public Integer getId_user() {
        return id_user;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this,"Lectura Cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,result.getContents(), Toast.LENGTH_LONG).show();
                txtCantidad.setText("");
                txtDescripcion.setText("");
                txtDate.setText("");
                txtResultado.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

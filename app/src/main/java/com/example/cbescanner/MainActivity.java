package com.example.cbescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    EditText txtResultado;
    EditText txtCantidad;
    EditText txtDescripcion;
    ImageButton btnSave;
    ImageButton btnCancel;
    ListView lvListaCR;
    Button btnSincronizar;
    ArrayList<String> lngList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        txtResultado = findViewById(R.id.txtResultado);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        lvListaCR = findViewById(R.id.lvListaCR);
        btnSincronizar = findViewById(R.id.btnSincronizar);
        lngList  = new  ArrayList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lngList);

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
                String cb = txtResultado.getText().toString();
                String desc = txtDescripcion.getText().toString();
                String cant = txtCantidad.getText().toString();

                if (!cb.isEmpty()) {
                    if (!desc.isEmpty()) {
                        if (!cant.isEmpty()) {
                            lngList.add(cb + " - " + cant + " - " + desc);
                            adapter.notifyDataSetChanged();
                            //vaciamos los EditText
                            txtResultado.setText("");
                            txtDescripcion.setText("");
                            txtCantidad.setText("");
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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this,"Lectura Cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,result.getContents(), Toast.LENGTH_LONG).show();
                txtResultado.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
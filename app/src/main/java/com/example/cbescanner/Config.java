package com.example.cbescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Config extends AppCompatActivity {

    Button btnValidate;
    Button btnRegresar;
    EditText txtDB;
    EditText txtIp;
    EditText txtContrasena;
    EditText txtUsuario;
    ProgressBar pbConfig;


    private ConfigTask configTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_db);

        btnValidate = findViewById(R.id.btnValidate);
        btnRegresar = findViewById(R.id.btnRegresar);
        txtDB = findViewById(R.id.txtDB);
        txtIp = findViewById(R.id.txtIp);
        txtContrasena = findViewById(R.id.txtContrasena);
        txtUsuario = findViewById(R.id.txtUsuario);
        pbConfig = findViewById(R.id.pbConfig);

        // Mostrar los datos de la configuración
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        txtDB.setText(preferences.getString("db", ""));
        txtIp.setText(preferences.getString("ip", ""));
        txtContrasena.setText(preferences.getString("contrasena", ""));
        txtUsuario.setText(preferences.getString("usuario", ""));

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configTask = new ConfigTask(Config.this);
                configTask.run();
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Config.this, Login.class);
                startActivity(intent);
            }
        });
    }

    public ProgressBar getPbConfig() {
        return pbConfig;
    }

    public Button getBtnValidate() {
        return btnValidate;
    }

    public Button getBtnRegresar() {
        return btnRegresar;
    }

    public EditText getTxtDB() {
        return txtDB;
    }

    public EditText getTxtIp() {
        return txtIp;
    }

    public EditText getTxtContrasena() {
        return txtContrasena;
    }

    public EditText getTxtUsuario() {
        return txtUsuario;
    }
}

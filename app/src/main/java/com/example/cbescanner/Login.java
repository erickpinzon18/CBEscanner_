package com.example.cbescanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Login extends AppCompatActivity {

    Button btnLogin;
    Button btnConfig;
    EditText txtUsuario;
    EditText txtContrasena;
    ProgressBar pbLogin;
    private LoginTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnLogin = findViewById(R.id.btnLogin);
        btnConfig = findViewById(R.id.btnConfig);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        pbLogin = findViewById(R.id.pbLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validar que los campos no estén vacíos
                if (!txtUsuario.getText().toString().equals("") && !txtContrasena.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Iniciando sesión", Toast.LENGTH_LONG).show();
                    // Hilo para login con BD
                    loginTask = new LoginTask(Login.this);
                    new Thread(loginTask).start();
                } else {
                    Toast.makeText(getApplicationContext(), "Rellena todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Login.this, Config.class);
                    Login.this.startActivity(intent);
                } catch (Exception e) {
                    Log.e("Error", Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(getApplicationContext(), "Error al abrir configuración", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public ProgressBar getPbLogin() {
        return pbLogin;
    }

    public EditText getTxtUsuario() {
        return txtUsuario;
    }

    public EditText getTxtContrasena() {
        return txtContrasena;
    }

    public Button getBtnLogin() {
        return btnLogin;
    }
}

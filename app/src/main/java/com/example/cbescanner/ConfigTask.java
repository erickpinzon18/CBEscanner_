package com.example.cbescanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ConfigTask implements Runnable {

    private Config config;
    private DBConnectionManager dbConnectionManager;

    public ConfigTask(Config activity) {
        this.config = activity;
    }

    @Override
    public void run() {
        config.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                config.getPbConfig().setVisibility(View.VISIBLE);
                config.getBtnValidate().setEnabled(false);
                config.getBtnRegresar().setEnabled(false);
            }
        });

        config.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String db = config.getTxtDB().getText().toString().trim();
                String ip = config.getTxtIp().getText().toString().trim();
                String contrasena = config.getTxtContrasena().getText().toString().trim();
                String usuario = config.getTxtUsuario().getText().toString().trim();

                if (!db.isEmpty()) {
                    if (!ip.isEmpty()) {
                        if (!contrasena.isEmpty()) {
                            if (!usuario.isEmpty()) {
                                dbConnectionManager = new DBConnectionManager();
                                boolean res = dbConnectionManager.test(ip, db, usuario, contrasena);
                                if (res) {
                                    // Guardar los datos de conexión en las preferencias
                                    SharedPreferences preferences = config.getSharedPreferences("config", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("db", db);
                                    editor.putString("ip", ip);
                                    editor.putString("pass", contrasena);
                                    editor.putString("user", usuario);
                                    editor.apply();
                                    Toast.makeText(config, "Conexión exitosa, puedes regresar a iniciar Sesión", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(config, "Error al conectar con la base de datos, prueba con otros parametros", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(config, "El campo Usuario no puede estar vacío", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(config, "El campo Contraseña no puede estar vacío", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(config, "El campo IP no puede estar vacío", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(config, "El campo Base de datos no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        config.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                config.getPbConfig().setVisibility(View.INVISIBLE);
                config.getBtnValidate().setEnabled(true);
                config.getBtnRegresar().setEnabled(true);
            }
        });
    }
}

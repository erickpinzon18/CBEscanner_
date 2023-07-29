package com.example.cbescanner;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// SyncTask.java
public class LoginTask implements Runnable {

    private Login login;
    private ResultSet rslt;

    public LoginTask(Login activity) {
        this.login = activity;
    }

    @Override
    public void run() {
        login.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                login.getPbLogin().setVisibility(View.VISIBLE);
                login.getBtnLogin().setEnabled(false);
                login.getTxtUsuario().setEnabled(false);
                login.getTxtContrasena().setEnabled(false);
            }
        });

        DBConnectionManager dbManager = new DBConnectionManager();
        Connection connection = dbManager.conexionBD();
        try {
            if (connection != null) {
                // Validamos el login con la bd
                PreparedStatement stm = connection.prepareStatement("select * from usuario where usuario = '"+ login.getTxtUsuario().getText().toString() +"' and status = 1;");
                rslt = stm.executeQuery();
                // Validar que la contraseña sea correcta
                if (rslt.next()) {
                    String pass = rslt.getString("contrasena");
                    String id = rslt.getString("id");
                    String usuario = rslt.getString("usuario");
                    String nombre = rslt.getString("nombre");
                    if (pass.equals(login.getTxtContrasena().getText().toString())) {
                        // Si es correcta, abrir la actividad principal
                        login.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Mostar mensaje de login correcto
                                    Toast.makeText(login.getApplicationContext(), "Login correcto.", Toast.LENGTH_LONG).show();
                                    // Abrir la actividad principal y enviar el usuario
                                    Intent intent = new Intent(login, MainActivity.class);
                                    // Enviar los datos del usuario de la bd a la actividad principal
                                    intent.putExtra("id", id);
                                    intent.putExtra("usuario", usuario);
                                    intent.putExtra("nombre", nombre);
                                    login.startActivity(intent);

                                } catch (Exception e) {
                                    Log.e("Error al tratar de hacer el Intent ", e.getMessage());
                                    Toast.makeText(login.getApplicationContext(), "Error al tratar de hacer el Intent: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                };
                            }
                        });
                    } else {
                        // Si no, mostrar mensaje de error
                        login.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(login.getApplicationContext(), "Contraseña incorrecta.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    // Si no, mostrar mensaje de error
                    login.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(login.getApplicationContext(), "Usuario no encontrado.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                connection.close();
            }
        } catch (Exception e) {
            final String tit = "Error en la conexion con BD: ";
            final String msg = e.getMessage();
            Log.e(tit, msg);
            Log.d(tit, msg);
            login.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(login.getApplicationContext(), tit + msg, Toast.LENGTH_LONG).show();
                }
            });
        }

        login.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                login.getPbLogin().setVisibility(View.INVISIBLE);
                login.getBtnLogin().setEnabled(true);
                login.getTxtUsuario().setEnabled(true);
                login.getTxtContrasena().setEnabled(true);
            }
        });
    }
}

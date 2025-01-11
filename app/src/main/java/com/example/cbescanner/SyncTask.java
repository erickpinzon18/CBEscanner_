package com.example.cbescanner;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;

// SyncTask.java
public class SyncTask implements Runnable {

    private MainActivity mainActivity;

    public SyncTask(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public void run() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getpbSinc().setVisibility(View.VISIBLE);
                mainActivity.getBtnSincronizar().setEnabled(false);
            }
        });

        //vamos a guardar la lista de articulos escaneados
        //primero nos conectamos a la bd
        DBConnectionManager dbManager = new DBConnectionManager();
        Connection connection = dbManager.conexionBD(this.mainActivity.getApplicationContext());
        try {
            if (connection != null) {
                //recorremos el adaptador
                for (String ss : mainActivity.getLngList()) {
                    //separo codigo barras, cantidad y descripcion
                    String cb = ss.split("-")[0];
                    String cant = ss.split("-")[1];
                    String desc = ss.split("-")[2];
                    String date = ss.split("-")[3];
                    String id = ss.split("-")[4];
                    String stStm = "";

                    if (Integer.parseInt(id.trim()) != 0) {
                        stStm = "UPDATE producto SET codigo = '" + cb + "', etiqueta = '" + desc + "', cantidad = " + cant + ", fecha_cap = '" + date + "', id_usuario = " + mainActivity.getId_user() + " WHERE id = " + Integer.parseInt(id.trim()) + ";";
                    } else {
                        stStm = "INSERT INTO producto (codigo, etiqueta, cantidad, fecha_cap, fecha_mod, id_usuario) VALUES ('" + cb + "', '" + desc + "', " + cant + ", '" + date + "', getdate(), " + mainActivity.getId_user() + ");";
                    }

                    Log.i("Query: ", stStm);
                    PreparedStatement stm = connection.prepareStatement(stStm);
                    stm.executeUpdate();
                    Log.i("Producto insertado o actualizao: ", desc);
                }
                mainActivity.getLngList().removeAll(mainActivity.getLngList());

                // Updating UI elements should be done on the UI thread
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.getAdapter().notifyDataSetChanged();
                        mainActivity.getBtnSincronizar().setEnabled(false);
                        Toast.makeText(mainActivity.getApplicationContext(), "Sincronización correcta.", Toast.LENGTH_LONG).show();
                    }
                });

                connection.close();
            }
        } catch (Exception e) {
            final String tit = "Error al tratar de sincronizar ";
            final String msg = e.getMessage();
            Log.e(tit, msg);
            Log.d(tit, msg);
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainActivity.getApplicationContext(), tit + msg, Toast.LENGTH_LONG).show();
                }
            });
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getpbSinc().setVisibility(View.INVISIBLE);
                mainActivity.getBtnSincronizar().setEnabled(true);
            }
        });
    }
}

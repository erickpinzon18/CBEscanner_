package com.example.cbescanner;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SearchTask implements Runnable {

    private MainActivity mainActivity;

    public SearchTask(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public void run() {
        mainActivity.runOnUiThread(() -> {
            mainActivity.getpbSinc().setVisibility(View.VISIBLE);
            mainActivity.getBtnSincronizar().setEnabled(false);
            mainActivity.getBtnScan().setEnabled(false);
        });

        DBConnectionManager dbManager = new DBConnectionManager();
        Connection connection = dbManager.conexionBD(this.mainActivity.getApplicationContext());
        try {
            if (connection != null) {
                String cb = mainActivity.getTxtResultado();
                String stStm = "SELECT * FROM producto WHERE codigo_serv = '" + cb + "' OR codigo = '" + cb + "';";
                Log.i("Query: ", stStm);
                PreparedStatement stm = connection.prepareStatement(stStm);
                ResultSet rslt = stm.executeQuery();

                boolean hasResults = false;

                while (rslt.next()) {
                    hasResults = true;
                    Integer id = rslt.getInt("id");
                    String etiqueta = rslt.getString("etiqueta");
                    String cantidad = rslt.getString("cantidad");
                    String fecha = rslt.getString("fecha_cap");

                    // format date (yyyy-mm-dd hh:mm:ss) to (dd/mm/yyyy)
                    String[] date = fecha.split(" ");
                    String fecha_correcta = date[0].replace("-", "/");

                    Log.i("Producto encontrado: ", etiqueta);

                    mainActivity.runOnUiThread(() -> {
                        mainActivity.setTxtCantidad(cantidad);
                        mainActivity.setTxtDescripcion(etiqueta);
                        mainActivity.setTxtDate(fecha_correcta);
                        mainActivity.setID_producto(id);
                        mainActivity.getBtnBuscar().setEnabled(true);
                        Toast.makeText(mainActivity.getApplicationContext(), "Producto encontrado.", Toast.LENGTH_LONG).show();
                    });
                }

                if (!hasResults) {
                    String cbUpper = cb.toUpperCase();
                    String stStmInv20 = "SELECT * FROM inventario_2020 WHERE codigo_serv = '" + cbUpper + "';";
                    Log.i("Query: ", stStmInv20);
                    PreparedStatement stmInv = connection.prepareStatement(stStmInv20);
                    ResultSet rsltInv = stmInv.executeQuery();

                    boolean hasResultsInv = false;

                    while (rsltInv.next()) {
                        hasResultsInv = true;
                        String etiqueta = rsltInv.getString("nombre_serv");
                        String cantidad = rsltInv.getString("existencia_inicial");
                        String cantidad_entrada = rsltInv.getString("cant_entrada");
                        String fecha = rsltInv.getString("fecha_stock");
                        Integer cantidad_final = Integer.parseInt(cantidad) + Integer.parseInt(cantidad_entrada);

                        // Format date (yyyy-mm-dd) to (dd/mm/yyyy)
                        String[] date = fecha.split(" ");
                        String fecha_correcta = date[0].replace("-", "/");

                        boolean date_correct = Integer.parseInt(date[0].split("-")[0]) > 2015;;

                        Log.i("Producto encontrado: ", etiqueta);

                        mainActivity.runOnUiThread(() -> {
                            mainActivity.setTxtCantidad(cantidad_final.toString());
                            mainActivity.setTxtDescripcion(etiqueta);
                            if (date_correct) mainActivity.setTxtDate(fecha_correcta);
                            mainActivity.setID_producto(0);
                            mainActivity.getBtnBuscar().setEnabled(true);
                            Toast.makeText(mainActivity.getApplicationContext(), "Producto encontrado.", Toast.LENGTH_LONG).show();
                        });
                    }

                    if (!hasResultsInv) {
                        mainActivity.runOnUiThread(() -> {
                            Toast.makeText(mainActivity.getApplicationContext(), "Producto no encontrado.", Toast.LENGTH_LONG).show();
                            mainActivity.getBtnScan().setEnabled(true);
                            mainActivity.getBtnSincronizar().setEnabled(false);
                        });
                    }

                    rsltInv.close();
                    stmInv.close();
                }

                rslt.close();
                stm.close();
                connection.close();
            }
        } catch (Exception e) {
            final String tit = "Error al tratar de buscar ";
            final String msg = e.getMessage();
            Log.e(tit, msg);
            Log.d(tit, msg);
            mainActivity.runOnUiThread(() -> {
                Toast.makeText(mainActivity.getApplicationContext(), tit + msg, Toast.LENGTH_LONG).show();
            });
        }

        mainActivity.runOnUiThread(() -> {
            mainActivity.getpbSinc().setVisibility(View.INVISIBLE);
            mainActivity.getBtnSincronizar().setEnabled(true);
        });
    }
}
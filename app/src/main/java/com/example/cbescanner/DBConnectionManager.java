package com.example.cbescanner;// DBConnectionManager.java
import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Clase para la conexión a la base de datos
 */
public class DBConnectionManager {
    // Declara las variables de conexión a la base de datos
    private String url = "jdbc:jtds:sqlserver://192.168.1.109/inventario;instance=MSSQLSERVER;user=sa;password=q1w2e3r4;";
    public Connection conexionBD() {
        try {
            Connection cnn = null;
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.109;databaseName=inventario;username:sa;password=q1w2e3r4;");
            cnn = DriverManager.getConnection(url);
            return cnn;
        } catch (Exception e) {
            Log.e("DBConnectionManager", "Error al establecer la conexión a la base de datos", e);
            return null;
        }
    }
}
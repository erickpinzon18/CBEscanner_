package com.example.cbescanner;// DBConnectionManager.java
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Clase para la conexión a la base de datos
 */
public class DBConnectionManager {
    // Declara las variables de conexión a la base de datos
    private String url;
    // "jdbc:jtds:sqlserver://192.168.1.109/inventario;instance=MSSQLSERVER;user=sa;password=q1w2e3r4;"
    public Connection conexionBD(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            String ip = prefs.getString("ip", "");
            String db = prefs.getString("db", "");
            String user = prefs.getString("user", "");
            String pass = prefs.getString("pass", "");
            if (ip.isEmpty() || db.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "No se han configurado los datos de conexión", Toast.LENGTH_LONG).show();
                return null;
            }
            url = "jdbc:jtds:sqlserver://" + ip + "/" + db + ";instance=MSSQLSERVER;user=" + user + ";password=" + pass + ";";
            Log.i("UseDB", "conexionDB: " + url);

            Connection cnn = null;
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn = DriverManager.getConnection(url);
            return cnn;
        } catch (Exception e) {
            Log.e("DBConnectionManager", "Error al establecer la conexión a la base de datos", e);
            return null;
        }
    }

    public boolean test(String ip, String db, String user, String pass) {
        try {
            url = "jdbc:jtds:sqlserver://" + ip + "/" + db + ";instance=MSSQLSERVER;user=" + user + ";password=" + pass + ";";
            Log.i("TestDB", "test: " + url);
            Connection cnn = null;
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.109;databaseName=inventario;username:sa;password=q1w2e3r4;");
            cnn = DriverManager.getConnection(url);
            return true;
        } catch (Exception e) {
            Log.e("DBConnectionManager", "Error al establecer la conexión a la base de datos", e);
            return false;
        }
    }
}
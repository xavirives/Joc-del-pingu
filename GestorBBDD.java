package controlador;

import modelo.Partida;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestorBBDD {

    private String urlBBDD;
    private String username;
    private String password;

    public GestorBBDD(String urlBBDD, String username, String password) {
        this.urlBBDD = urlBBDD;
        this.username = username;
        this.password = password;
    }

    public String getUrlBBDD() {
        return urlBBDD;
    }

    public void setUrlBBDD(String urlBBDD) {
        this.urlBBDD = urlBBDD;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(urlBBDD, username, password);
    }

    public void guardarBBDD(Partida p) {
        // TODO implementar guardado en base de datos
        try {
            Connection conn = conectar();
            System.out.println("Conectado a la base de datos");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Partida cargarBBDD(int id) {
        // TODO implementar carga desde base de datos
        try {
            Connection conn = conectar();
            System.out.println("Cargando partida desde la base de datos...");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

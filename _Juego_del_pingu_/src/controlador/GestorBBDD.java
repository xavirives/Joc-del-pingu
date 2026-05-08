package controlador;

import java.io.*;
import java.sql.*;
import java.util.Base64;
import modelo.Partida;

/**
 * Gestiona el guardado y la carga de partidas en la base de datos Oracle.
 */
public class GestorBBDD {

    // Datos de conexión a Oracle.
    private final String urlBBDD = "jdbc:oracle:thin:@//192.168.3.26:1521/XEPDB2";
    private final String username = "DM1_2526_GRUP08"; 
    private final String password = "AGRUP08"; 

    /**
     * Guarda la partida en la base de datos.
     * Si ya existe una partida guardada, la actualiza.
     * Si no existe, crea una nueva.
     */
    public void guardarBBDD(Partida p) {
        if (p == null) return;

        // Consulta para actualizar la partida guardada.
        String sqlUpdate = "UPDATE PARTIDAS_PINGU SET DATOS_ENCRIPTADOS = ?, FECHA_GUARDADO = CURRENT_TIMESTAMP WHERE ID_PARTIDA = 1";
        
        try (Connection conn = DriverManager.getConnection(urlBBDD, username, password)) {

            // Convierte el objeto Partida en bytes.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(p);
            oos.close();

            // Convierte los bytes a texto para poder guardarlos en la BBDD.
            String datos = Base64.getEncoder().encodeToString(baos.toByteArray());

            // Prepara el UPDATE y añade los datos de la partida.
            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setString(1, datos);

            // Si no se actualiza ninguna fila, significa que la partida no existe.
            if (pstmt.executeUpdate() == 0) {

                // Inserta una nueva partida guardada.
                String sqlInsert = "INSERT INTO PARTIDAS_PINGU (ID_PARTIDA, DATOS_ENCRIPTADOS) VALUES (1, ?)";
                PreparedStatement pstmtIns = conn.prepareStatement(sqlInsert);
                pstmtIns.setString(1, datos);
                pstmtIns.executeUpdate();
            }

            // Mensaje que se mostrará en el juego.
            p.setUltimoEvento("Partida guardada en Oracle.");

        } catch (Exception e) {
            // Muestra el error si falla la conexión o el guardado.
            System.err.println("Error BBDD: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Carga una partida desde la base de datos usando su ID.
     */
    public Partida cargarBBDD(int id) {

        // Consulta para obtener la partida guardada.
        String sql = "SELECT DATOS_ENCRIPTADOS FROM PARTIDAS_PINGU WHERE ID_PARTIDA = " + id;

        try (Connection conn = DriverManager.getConnection(urlBBDD, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Si encuentra la partida, reconstruye el objeto Partida.
            if (rs.next()) {

                // Convierte el texto guardado en bytes.
                byte[] data = Base64.getDecoder().decode(rs.getString("DATOS_ENCRIPTADOS"));

                // Reconstruye la partida a partir de esos bytes.
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                Partida cargada = (Partida) ois.readObject();
                ois.close();

                return cargada;
            }

        } catch (Exception e) {
            // Muestra el error si falla la carga.
            System.err.println("Error Carga BBDD: " + e.getMessage());
        }

        // Si no encuentra partida o hay error, devuelve null.
        return null;
    }
}
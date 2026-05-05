package controlador;

import java.io.*;
import java.sql.*;
import java.util.Base64;
import modelo.Partida;

public class GestorBBDD {
    private final String urlBBDD = "jdbc:oracle:thin:@//192.168.3.26:1521/XEPDB2";
    private final String username = "DM1_2526_GRUP08"; 
    private final String password = "AGRUP08"; 

    public void guardarBBDD(Partida p) {
        if (p == null) return;
        String sqlUpdate = "UPDATE PARTIDAS_PINGU SET DATOS_ENCRIPTADOS = ?, FECHA_GUARDADO = CURRENT_TIMESTAMP WHERE ID_PARTIDA = 1";
        
        try (Connection conn = DriverManager.getConnection(urlBBDD, username, password)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(p);
            oos.close();
            String datos = Base64.getEncoder().encodeToString(baos.toByteArray());

            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setString(1, datos);
            if (pstmt.executeUpdate() == 0) {
                String sqlInsert = "INSERT INTO PARTIDAS_PINGU (ID_PARTIDA, DATOS_ENCRIPTADOS) VALUES (1, ?)";
                PreparedStatement pstmtIns = conn.prepareStatement(sqlInsert);
                pstmtIns.setString(1, datos);
                pstmtIns.executeUpdate();
            }
            p.setUltimoEvento("Partida guardada en Oracle.");
        }  catch (Exception e) {
            System.err.println("Error BBDD: " + e.toString()); // Cambia getMessage por toString
            e.printStackTrace(); // Esto te dirá la línea exacta del fallo
        }

    }

    public Partida cargarBBDD(int id) {
        String sql = "SELECT DATOS_ENCRIPTADOS FROM PARTIDAS_PINGU WHERE ID_PARTIDA = " + id;
        try (Connection conn = DriverManager.getConnection(urlBBDD, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                byte[] data = Base64.getDecoder().decode(rs.getString("DATOS_ENCRIPTADOS"));
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                Partida cargada = (Partida) ois.readObject();
                ois.close();
                return cargada;
            }
        } catch (Exception e) {
            System.err.println("Error Carga BBDD: " + e.getMessage());
        }
        return null;
    }
}

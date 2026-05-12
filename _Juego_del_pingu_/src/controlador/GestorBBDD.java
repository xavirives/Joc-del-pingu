package controlador;
import java.io.*;
import java.sql.*;
import java.util.Base64;
import modelo.Partida;
 
/**
 * Gestiona el guardado y la carga de partidas en la base de datos Oracle.
 */
public class GestorBBDD {
    // Datos de conexión a Oracle. 192.168.3.26:1521 = servidor Oracle, XEPDB2 = base de datos
    private final String urlBBDD = "jdbc:oracle:thin:@//192.168.3.26:1521/XEPDB2";
    // Usuario de la base de datos
    private final String username = "DM1_2526_GRUP08"; 
    // Contraseña de la base de datos
    private final String password = "AGRUP08"; 
 
    /**
     * Guarda la partida en la base de datos.
     * Si ya existe una partida guardada, la actualiza.
     * Si no existe, crea una nueva.
     */
    public void guardarBBDD(Partida p) {
        // Si la partida es null, salir sin hacer nada
        if (p == null) return;
        // SQL para actualizar la partida si ya existe. DATOS_ENCRIPTADOS = columna donde se guardan los datos
        String sqlUpdate = "UPDATE PARTIDAS_PINGU SET DATOS_ENCRIPTADOS = ?, FECHA_GUARDADO = CURRENT_TIMESTAMP WHERE ID_PARTIDA = 1";
        
        // Abre conexión a Oracle (se cierra automáticamente al salir del try)
        try (Connection conn = DriverManager.getConnection(urlBBDD, username, password)) {
            // Crea un flujo de bytes en memoria para convertir el objeto a bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Crea un flujo que escribe objetos Java como bytes
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // Escribe el objeto Partida como bytes (serialización)
            oos.writeObject(p);
            oos.close();
            // Convierte los bytes a texto Base64 para poder guardarlos en la BD
            String datos = Base64.getEncoder().encodeToString(baos.toByteArray());
            // Prepara la consulta UPDATE con el parámetro ?
            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
            // Establece el valor del parámetro ? con los datos Base64
            pstmt.setString(1, datos);
            // Ejecuta el UPDATE. Si devuelve 0, significa que no encontró la partida
            if (pstmt.executeUpdate() == 0) {
                // Si no existía la partida, la inserta como nueva
                String sqlInsert = "INSERT INTO PARTIDAS_PINGU (ID_PARTIDA, DATOS_ENCRIPTADOS) VALUES (1, ?)";
                PreparedStatement pstmtIns = conn.prepareStatement(sqlInsert);
                pstmtIns.setString(1, datos);
                pstmtIns.executeUpdate();
            }
            // Establece el mensaje que se mostrará en el juego
            p.setUltimoEvento("Partida guardada en Oracle.");
        } catch (Exception e) {
            // Si hay error de conexión o guardado, lo imprime
            System.err.println("Error BBDD: " + e.toString());
            e.printStackTrace();
        }
    }
 
    /**
     * Carga una partida desde la base de datos usando su ID.
     */
    public Partida cargarBBDD(int id) {
        // SQL para obtener los datos de la partida. El " + id" inserta el ID en la consulta
        String sql = "SELECT DATOS_ENCRIPTADOS FROM PARTIDAS_PINGU WHERE ID_PARTIDA = " + id;
        // Abre conexión a Oracle y ejecuta la consulta (todo se cierra automáticamente)
        try (Connection conn = DriverManager.getConnection(urlBBDD, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // rs.next() devuelve true si hay resultados, false si no
            if (rs.next()) {
                // Obtiene el texto Base64 de la columna DATOS_ENCRIPTADOS
                byte[] data = Base64.getDecoder().decode(rs.getString("DATOS_ENCRIPTADOS"));
                // Crea un flujo para leer objetos desde bytes
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                // Lee el objeto Partida desde los bytes (deserialización)
                Partida cargada = (Partida) ois.readObject();
                ois.close();
                // Devuelve la partida reconstruida
                return cargada;
            }
        } catch (Exception e) {
            // Si hay error en la carga, lo imprime
            System.err.println("Error Carga BBDD: " + e.getMessage());
        }
        // Si no encuentra partida o hay error, devuelve null
        return null;
    }
}

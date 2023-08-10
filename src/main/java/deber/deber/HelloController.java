package deber.deber;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.*;

public class HelloController {
    String NOMBRE, APELLIDO, CORREO,vacio = "Null";
    int ID,CEDULA;
    @FXML
    private Label welcomeText;
    @FXML
    private Label TACCION;
    @FXML
    private Button BCREATE;
    @FXML
    private Button BREAD;
    @FXML
    private Button BUPDATE;
    @FXML
    private Button BDELETE;
    @FXML
    private TextField TID;
    @FXML
    private TextField TNOMBRE;
    @FXML
    private TextField TAPELLIDO;
    @FXML
    private TextField TCEDULA;
    @FXML
    private TextField TCORREO;
    @FXML
    private Button BAYUDA;
    //Vaciar datos
    public void vaciar(){
        ID = 0;
        NOMBRE = vacio;
        APELLIDO = vacio;
        CEDULA = 0;
        CORREO = vacio;
    }
    //conectar con sql server
    String conexion= "jdbc:sqlserver://localhost:1433;database=DEBERJAVAFX;user=root;password=root_1;trustServerCertificate=true;";
    @FXML
    protected void onBCREATE() {
        vaciar();
        ID = Integer.parseInt(TID.getText());
        NOMBRE = TNOMBRE.getText();
        APELLIDO = TAPELLIDO.getText();
        CEDULA = Integer.parseInt(TCEDULA.getText());
        CORREO = TCORREO.getText();
        String QUERY="INSERT INTO datos(ID,NOMBRE,APELLIDO,CEDULA,CORREO)" +
                "VALUES(?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(conexion);
             PreparedStatement statement = conn.prepareStatement(QUERY)) {
            // Se ponen los parametros en el orden en el que se indica en el QUERY
            statement.setString(1, String.valueOf(ID));
            statement.setString(2, NOMBRE);
            statement.setString(3, APELLIDO);
            statement.setString(4, String.valueOf(CEDULA));
            statement.setString(5, CORREO);
            // Se ejecuta el QUERY
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                TACCION.setText("Datos creados correctamente");
            } else {
                TACCION.setText("Error al crear datos");
            }
        } catch (SQLException ex) {
            TACCION.setText("Error al crear datos");
            ex.printStackTrace();
        }
    }
    @FXML
    protected void onBREAD() {
        vaciar();
        ID= Integer.parseInt(TID.getText());
        String SELECT_QUERY="SELECT * FROM datos WHERE ID = ?";
        try(Connection conn=DriverManager.getConnection(conexion);)
        {
            PreparedStatement statement = conn.prepareStatement(SELECT_QUERY);
            statement.setString(1, String.valueOf(ID));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                NOMBRE = rs.getString("NOMBRE");
                APELLIDO = rs.getString("APELLIDO");
                CEDULA = Integer.parseInt(rs.getString("CEDULA"));
                CORREO = rs.getString("CORREO");
            }
            if (NOMBRE != "Null") {
                TNOMBRE.setText(NOMBRE);
                TAPELLIDO.setText(APELLIDO);
                TCEDULA.setText(String.valueOf(CEDULA));
                TCORREO.setText(CORREO);
                TACCION.setText("Datos mostrados correctamente");
            } else {
                vaciar();
                TNOMBRE.setText(NOMBRE);
                TAPELLIDO.setText(APELLIDO);
                TCEDULA.setText(String.valueOf(CEDULA));
                TCORREO.setText(CORREO);
                TACCION.setText("No se encontraron datos con ese ID");
            }

        }
        catch (SQLException eX){
            TACCION.setText("No se encontró datos con ese codigo");
            throw new RuntimeException(eX);
        }
    }
    @FXML
    protected void onBUPDATE() {
        vaciar();
        String idToUpdate; // Reemplaza 1 con el ID de la fila que deseas actualizar
        idToUpdate= TID.getText().trim();
        NOMBRE = TNOMBRE.getText();
        APELLIDO = TAPELLIDO.getText();
        CEDULA = Integer.parseInt(TCEDULA.getText());
        CORREO = TCORREO.getText();
        String UPDATE_QUERY = "UPDATE datos SET NOMBRE = ?, APELLIDO = ?, CEDULA = ?, CORREO = ? WHERE ID = ?";
        if (idToUpdate.isEmpty()) {
            TACCION.setText("Ingrese ID para realizaar la accion UPDATE");
            // Sale del método para evitar la ejecución del código siguiente
            return;
        }
            try (Connection conn = DriverManager.getConnection(conexion)) {
                PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY);
                // Establecer nuevos valores para los campos siguiendo el orden del query
                statement.setString(1,NOMBRE);
                statement.setString(2,APELLIDO);
                statement.setString(3, String.valueOf(CEDULA));
                statement.setString(4,CORREO);
                // Establecer valor para el parámetro del ID de la fila que deseas actualizar
                statement.setInt(5, Integer.parseInt(idToUpdate));
                // Ejecutar sentencia UPDATE
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    TACCION.setText("Se actualizaron los datos correctamente");
                } else {
                    TACCION.setText("No hay datos con ese codigo, ingrese nuevamente");
                }
            } catch (SQLException ex) {
                TACCION.setText("No hay datos con ese codigo, ingrese nuevamente");
                ex.printStackTrace();
        }
    }
    @FXML
    protected void onBDELETE() {
        vaciar();
        int idToDelete;
        idToDelete= Integer.parseInt(TID.getText());
        String DELETE_QUERY = "DELETE FROM datos WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(conexion)) {
            PreparedStatement statement = conn.prepareStatement(DELETE_QUERY);
            // Establecer valor para el parámetro del ID que deseas eliminar
            statement.setInt(1, idToDelete);
            // Ejecutar sentencia DELETE
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                TACCION.setText("Se eliminó la fila correctamente");
            } else {
                TACCION.setText("No se encontró datos con ese codigo");
            }
        } catch (SQLException eX) {
            TACCION.setText("No se encontró datos con ese codigo");
            eX.printStackTrace();
        }
    }
}

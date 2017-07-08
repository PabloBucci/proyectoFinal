/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abmc.dao;

import abmc.model.Persona;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author educacionit
 */
public class PersonaDao {

    private Connection connection;
    
    public PersonaDao() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/ambc", "root", "");
    }
    
    public List<Persona> getAll() throws SQLException {
        // Arma la consulta y la ejecuta
        String laConsulta = "SELECT * FROM personas";
        List<Persona> personas;
        
        try (Statement stmtConsulta = connection.createStatement()) {
            ResultSet rs = stmtConsulta.executeQuery(laConsulta);
            // Informa la insercion a realizar
            System.out.println(">>SQL: " + laConsulta);
            // Construye la coleccion de autos
            personas = new ArrayList<>();
            // Muestra los datos
            while (rs.next()) {
                // Arma el objeto auto con los datos de la consulta
                Persona  p = new Persona(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("direccion"),
                        rs.getString("telefono"), rs.getString("celular"), rs.getString("email"));
                
                // Agrega el auto a la coleccion
                personas.add(p);
            }
            // Cierra el Statement
        }

        // Retorna la coleccion
        return personas;

    }
    
    public void add(Persona persona)throws SQLException  {
                // Arma la sentencia de inserción
        String laInsercion = "INSERT INTO personas "
                + "(id, nombre, apellido, direccion, telefono, celular, email) "
                + " VALUES (?,?,?,?,?,?,?)";

        // Informa la insercion a realizar
        System.out.println(">>SQL: " + laInsercion);

        try ( // Ejecuta la insercion
                PreparedStatement ps = connection.prepareStatement(laInsercion, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, persona.getId());
            ps.setString(2, persona.getNombre());
            ps.setString(3, persona.getApellido());
            ps.setString(4, persona.getDireccion());
            ps.setString(5, persona.getTelefono());
            ps.setString(6, persona.getCelular());
            ps.setString(7, persona.getEmail());
            ps.execute();
            // Obtiene el ID generado
            Integer id = null;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            // Cierra el Statement
        }
    }

    public void update(Persona persona)throws SQLException  {
        
    }

    public void delete(Persona persona)throws SQLException  {
                // Arma la sentencia de eliminacion
        String laEliminacion = "DELETE FROM personas WHERE id = " + persona.getId();

        // Informa la eliminacion a realizar
        System.out.println(">>SQL: " + laEliminacion);

        try ( // Ejecuta la eliminacion
                Statement stmtEliminacion = connection.createStatement()) {
            stmtEliminacion.execute(laEliminacion);
            // Cierra el Statement
        }
    }    
}

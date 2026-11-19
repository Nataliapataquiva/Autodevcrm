package com.autodev.crm.app;

import com.autodev.crm.config.ConexionBD;
import com.autodev.crm.dao.ClienteDAO;
import com.autodev.crm.model.Cliente;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = ConexionBD.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexion exitosa a autodev_crm");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return;
        }

        ClienteDAO dao = new ClienteDAO();

        // 1. Insertar
        Cliente nuevo = new Cliente("Natalia Pataquiva", "AutoDev Solutions", "natalia.test@autodev.com", "3001234567");
        System.out.println("Insertado: " + dao.insertar(nuevo));

        // 2. Listar
        List<Cliente> lista = dao.listarTodos();
        System.out.println("--- Clientes en BD ---");
        for (Cliente c : lista) {
            System.out.println(c);
        }

        // 3. Actualizar y 4. Eliminar el ultimo cliente insertado con ese correo
        Cliente objetivo = null;
        for (Cliente c : lista) {
            if ("natalia.test@autodev.com".equals(c.getCorreo())) {
                objetivo = c;
            }
        }

        if (objetivo != null) {
            objetivo.setTelefono("3109876543");
            System.out.println("Actualizado: " + dao.actualizar(objetivo));
            System.out.println("Eliminado: " + dao.eliminar(objetivo.getIdCliente()));
        }
    }
}

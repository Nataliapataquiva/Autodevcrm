package com.autodev.crm.servlet;

import com.autodev.crm.dao.ClienteDAO;
import com.autodev.crm.model.Cliente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        this.clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String accion = req.getParameter("accion");
        String buscar = req.getParameter("buscar");
        if ("editar".equals(accion)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                try {
                    int idEditar = Integer.parseInt(idParam);
                    for (Cliente c : clienteDAO.listarTodos()) {
                        if (c.getIdCliente() == idEditar) {
                            req.setAttribute("clienteEditar", c);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("mensajeError", "ID de cliente no valido.");
                }
            }
        }
        List<Cliente> lista = clienteDAO.listarTodos();
        if (buscar != null && !buscar.isBlank()) {
            String filtro = buscar.trim().toLowerCase();
            List<Cliente> filtrada = new ArrayList<>();
            for (Cliente c : lista) {
                String nom = c.getNombre() != null ? c.getNombre().toLowerCase() : "";
                String emp = c.getEmpresa() != null ? c.getEmpresa().toLowerCase() : "";
                if (nom.contains(filtro) || emp.contains(filtro)) {
                    filtrada.add(c);
                }
            }
            lista = filtrada;
            req.setAttribute("buscar", buscar);
        }
        req.setAttribute("listaClientes", lista);
        req.getRequestDispatcher("/clientes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String accion = req.getParameter("accion");
        String mensajeExito = null;
        String mensajeError = null;
        if ("insertar".equals(accion)) {
            Cliente nuevo = new Cliente(req.getParameter("nombre"), req.getParameter("empresa"), req.getParameter("correo"), req.getParameter("telefono"));
            if (clienteDAO.insertar(nuevo)) { mensajeExito = "Cliente registrado correctamente."; } else { mensajeError = "No fue posible registrar el cliente."; }
        } else if ("actualizar".equals(accion)) {
            try {
                int idCliente = Integer.parseInt(req.getParameter("idCliente"));
                Cliente actualizado = new Cliente(idCliente, req.getParameter("nombre"), req.getParameter("empresa"), req.getParameter("correo"), req.getParameter("telefono"));
                if (clienteDAO.actualizar(actualizado)) { mensajeExito = "Cliente actualizado correctamente."; } else { mensajeError = "No fue posible actualizar el cliente."; }
            } catch (NumberFormatException e) { mensajeError = "ID de cliente invalido para actualizar."; }
        } else if ("eliminar".equals(accion)) {
            try {
                int idCliente = Integer.parseInt(req.getParameter("idCliente"));
                if (clienteDAO.eliminar(idCliente)) { mensajeExito = "Cliente eliminado correctamente."; } else { mensajeError = "No fue posible eliminar el cliente."; }
            } catch (NumberFormatException e) { mensajeError = "ID de cliente invalido para eliminar."; }
        }
        if (mensajeExito != null) { req.setAttribute("mensajeExito", mensajeExito); }
        if (mensajeError != null) { req.setAttribute("mensajeError", mensajeError); }
        req.setAttribute("listaClientes", clienteDAO.listarTodos());
        req.getRequestDispatcher("/clientes.jsp").forward(req, resp);
    }
}

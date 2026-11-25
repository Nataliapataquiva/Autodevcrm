<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.autodev.crm.model.Cliente" %>
<%@ page import="com.autodev.crm.dao.ClienteDAO" %>
<%
    List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
    if (listaClientes == null) { listaClientes = new ClienteDAO().listarTodos(); }
    Cliente clienteEditar = (Cliente) request.getAttribute("clienteEditar");
    boolean editando = (clienteEditar != null);
    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String mensajeError = (String) request.getAttribute("mensajeError");
    String buscar = (String) request.getAttribute("buscar");
    if (buscar == null) { buscar = ""; }
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>AutoDev CRM - Gestion de Clientes</title>
<style>
body { font-family: Arial, sans-serif; margin: 24px; background: #f5f7fa; }
.box { max-width: 960px; margin: 0 auto; background: #fff; padding: 24px; border-radius: 8px; }
.grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 14px; }
label { display: block; font-weight: bold; margin-bottom: 4px; }
input[type=text], input[type=email] { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; }
button, .btn { padding: 8px 14px; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
.ok { background: #dafbe1; color: #116329; padding: 10px; border-radius: 4px; margin-bottom: 12px; }
.err { background: #ffebe9; color: #82071e; padding: 10px; border-radius: 4px; margin-bottom: 12px; }
table { width: 100%; border-collapse: collapse; margin-top: 14px; }
th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
th { background: #eef2f7; }
</style>
</head>
<body>
<div class="box">
<h1>AutoDev CRM - Modulo de Clientes</h1>
<% if (mensajeExito != null) { %><div class="ok"><%= mensajeExito %></div><% } %>
<% if (mensajeError != null) { %><div class="err"><%= mensajeError %></div><% } %>

<h2><%= editando ? "Editar Cliente #" + clienteEditar.getIdCliente() : "Registrar Nuevo Cliente" %></h2>
<form action="<%= request.getContextPath() %>/clientes" method="post">
<input type="hidden" name="accion" value="<%= editando ? "actualizar" : "insertar" %>">
<% if (editando) { %><input type="hidden" name="idCliente" value="<%= clienteEditar.getIdCliente() %>"><% } %>
<div class="grid">
<div><label>Nombre</label><input type="text" name="nombre" required value="<%= editando && clienteEditar.getNombre() != null ? clienteEditar.getNombre() : "" %>"></div>
<div><label>Empresa</label><input type="text" name="empresa" required value="<%= editando && clienteEditar.getEmpresa() != null ? clienteEditar.getEmpresa() : "" %>"></div>
<div><label>Correo</label><input type="email" name="correo" required value="<%= editando && clienteEditar.getCorreo() != null ? clienteEditar.getCorreo() : "" %>"></div>
<div><label>Telefono</label><input type="text" name="telefono" required value="<%= editando && clienteEditar.getTelefono() != null ? clienteEditar.getTelefono() : "" %>"></div>
</div>
<button type="submit"><%= editando ? "Actualizar" : "Guardar" %></button>
<% if (editando) { %><a class="btn" href="<%= request.getContextPath() %>/clientes">Cancelar</a><% } %>
</form>

<hr style="margin:20px 0;">
<h2>Consulta de Clientes (GET)</h2>
<form action="<%= request.getContextPath() %>/clientes" method="get">
<input type="text" name="buscar" placeholder="Buscar por nombre o empresa..." value="<%= buscar %>" style="width:260px; display:inline-block;">
<button type="submit">Consultar</button>
<a class="btn" href="<%= request.getContextPath() %>/clientes">Ver todos</a>
</form>

<table>
<thead><tr><th>ID</th><th>Nombre</th><th>Empresa</th><th>Correo</th><th>Telefono</th><th>Acciones</th></tr></thead>
<tbody>
<% if (listaClientes.isEmpty()) { %>
<tr><td colspan="6">No hay clientes registrados.</td></tr>
<% } else { for (Cliente c : listaClientes) { %>
<tr>
<td><%= c.getIdCliente() %></td>
<td><%= c.getNombre() != null ? c.getNombre() : "" %></td>
<td><%= c.getEmpresa() != null ? c.getEmpresa() : "" %></td>
<td><%= c.getCorreo() != null ? c.getCorreo() : "" %></td>
<td><%= c.getTelefono() != null ? c.getTelefono() : "" %></td>
<td>
<a class="btn" href="<%= request.getContextPath() %>/clientes?accion=editar&id=<%= c.getIdCliente() %>">Editar</a>
<form action="<%= request.getContextPath() %>/clientes" method="post" style="display:inline;">
<input type="hidden" name="accion" value="eliminar">
<input type="hidden" name="idCliente" value="<%= c.getIdCliente() %>">
<button type="submit">Eliminar</button>
</form>
</td>
</tr>
<% } } %>
</tbody>
</table>
</div>
</body>
</html>

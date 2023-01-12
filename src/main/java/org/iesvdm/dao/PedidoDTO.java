package org.iesvdm.dao;

import java.sql.Date;

import org.iesvdm.modelo.Pedido;

public class PedidoDTO extends Pedido {
	
	private String nombre_cliente;

	public PedidoDTO(long id, double total, Date fecha, long id_cliente, long id_comercial, String nombre_cliente) {
		super(id, total, fecha, id_cliente, id_comercial);
		this.nombre_cliente = nombre_cliente;
	}

	public String getNombreCliente() {
		return nombre_cliente;
	}

	public void setNombreCliente(String nombre) {
		this.nombre_cliente = nombre;
	}
	
}

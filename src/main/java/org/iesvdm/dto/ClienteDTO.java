package org.iesvdm.dto;

import org.iesvdm.modelo.Cliente;

import lombok.Data;

@Data
public class ClienteDTO extends Cliente {
	int idComercial;
	String nombreComercial;
	int numPedidosTotal;
	int numPedidosTrimestre;
	int numPedidosAnio;
	int numPedidosLustro;
	
	public ClienteDTO(long id, String nombre, String apellido1, String apellido2, String ciudad, int categoria, int idComercial, 
			String nombreComercial, int numPedidosTotal, int numPedidosTrimestre, int numPedidosAnio,
			int numPedidosLustro) {
		super(id, nombre, apellido1, apellido2, ciudad, categoria);
		this.idComercial = idComercial;
		this.nombreComercial = nombreComercial;
		this.numPedidosTotal = numPedidosTotal;
		this.numPedidosTrimestre = numPedidosTrimestre;
		this.numPedidosAnio = numPedidosAnio;
		this.numPedidosLustro = numPedidosLustro;
	}
	
}

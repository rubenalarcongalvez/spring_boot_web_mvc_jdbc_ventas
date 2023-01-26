package org.iesvdm.dto;

import java.math.BigDecimal;

import org.iesvdm.modelo.Comercial;

public class ComercialDTO extends Comercial {

	private double totalFacturado;
	private double mediaFacturada;

	public ComercialDTO(int id, String nombre, String apellido1, String apellido2, BigDecimal comision,
			double totalFacturado, double mediaFacturada) {
		super(id, nombre, apellido1, apellido2, comision);
		this.totalFacturado = totalFacturado;
		this.mediaFacturada = mediaFacturada;
	}

	public double getTotalFacturado() {
		return this.totalFacturado;
	}

	public void setTotalFacturado(double totalFacturado) {
		this.totalFacturado = totalFacturado;
	}
	
	public double getMediaFacturada() {
		return this.mediaFacturada;
	}

	public void setMediaFacturada(double mediaFacturada) {
		this.mediaFacturada = mediaFacturada;
	}

}

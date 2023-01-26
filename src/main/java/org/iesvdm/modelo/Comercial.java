package org.iesvdm.modelo;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comercial {

	private int id;
	
	@NotBlank(message = "{notblank-nombre}")
	@Size(min=4, message = "{nombre-min}")
	@Size(max=30, message = "{nombre-max}")
	private String nombre;
	
	@NotBlank(message = "{notblank-apellido1}")
	@Size(min=4, message = "{apellido1-min}")
	@Size(max=30, message = "{apellido1-max}")
	private String apellido1;
	
	private String apellido2;
	
	@NotNull(message = "{comision-rango}")
	@DecimalMin(value="0.276", inclusive=true, message = "{comision-rango}")
	@DecimalMax(value="0.946", inclusive=true, message = "{comision-rango}")
	private BigDecimal comision;
	
}

package org.iesvdm.modelo;

import org.iesvdm.validator.RangoCategoria;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//La anotación @Data de lombok proporcionará el código de: 
//getters/setters, toString, equals y hashCode
//propio de los objetos POJOS o tipo Beans
@Data
//Para generar un constructor con lombok con todos los args
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
	
	private long id;
	
	@NotBlank(message = "{notblank-nombre}")
	@Size(min=4, message = "{nombre-min}")
	@Size(max=30, message = "{nombre-max}")
	private String nombre;
	
	@NotBlank(message = "{notblank-apellido1}")
	@Size(min=4, message = "{apellido1-min}")
	@Size(max=30, message = "{apellido1-max}")
	private String apellido1;
	
	private String apellido2;
	
	@NotBlank(message = "{notblank-email}")
	@Email(message = "{email-patron}", regexp="^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}")
	private String email;
	
	@NotBlank(message = "{notblank-ciudad}")
	@Size(max=50, message = "{ciudad-max}")
	private String ciudad;
	
	
//	@Min(value = 100, message = "{categoria-min}")
//	@Max(value = 1000, message = "{categoria-max}")
	
	
//	@RangoCategoria(message = "No haría falta porque ya predeterminamos uno")
	@NotNull(message = "{notblank-categoria}")
	@RangoCategoria(value = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000}, message = "La categoría debe estar entre 100 y 1000")
	private Integer categoria;
	
}

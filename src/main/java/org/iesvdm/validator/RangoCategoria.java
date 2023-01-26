package org.iesvdm.validator;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RangoCategoriaValidator.class)
@Documented
public @interface RangoCategoria {
	
	//Anotación para elegir el array que queramos (valores que vamos a poner).
	int[] value() default {
		100, 200, 300, 400, 500, 600, 700, 800, 1000
	}; //También le podemos asignar por defecto si no lo ponemos
	
	//El mensaje que mostramos de error
	String message() default "{error.rangocategoria}";
	
	//Validación en wizards (no se usa mucho)
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
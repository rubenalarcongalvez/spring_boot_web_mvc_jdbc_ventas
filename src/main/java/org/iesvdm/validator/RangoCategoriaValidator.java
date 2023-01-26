package org.iesvdm.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RangoCategoriaValidator implements ConstraintValidator<RangoCategoria, Integer> {

	private int[] rangoCategoria;

	@Override
	public void initialize(RangoCategoria constraintAnnotation) {

		this.rangoCategoria = constraintAnnotation.value(); //Pasamos el rango para poder utilizarlo luego en el isValid

	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		boolean isValid = false;

		// Si la lista contiene al número, es válido, si no, salta el error que hemos
		// predeterminado

		for (int i = 0; i < rangoCategoria.length && !isValid; i++) {
			if (rangoCategoria[i] == value)
				isValid = true;
		}

		// No se le añade el constraintContext.disableDefaultConstraintViolation() ni el
		// build, porque el que tenemos predeterminado ya va bien

		return isValid;
	}

}
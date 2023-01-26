package org.iesvdm.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

	@ExceptionHandler(ExcepcionGlobal.class)
	public String handleMiExcepcion(Model model, ExcepcionGlobal miExcepcion) {
		model.addAttribute("traza", miExcepcion.getMessage());

		return "error-mi-excepcion"; //página de error "error-mi-excepcion.html"
	}

	@ExceptionHandler(RuntimeException.class)
	public String handleAllUncaughtException(Model model, RuntimeException exception) {
		model.addAttribute("traza", exception.getMessage());

		return "error"; //página de error "error.html"
	}
	
}

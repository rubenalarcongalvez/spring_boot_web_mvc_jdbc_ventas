package org.iesvdm.controlador;

import org.iesvdm.exception.ExcepcionGlobal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {
	
	//Control de errores globales
		@GetMapping("/*") //Para que sea en cualquier página excepto si se especifica en otro lado
		public String globalRuntimeException() {
			throw new RuntimeException("FUNCIONA CORRECTAMENTE MAL - EXCEPCIÓN GLOBAL.");

			//return "global-runtime-exception"; No lo ponemos porque ya lo hace la RuntimeException y nos redirige a error.html
		}

		@GetMapping("/global-mi-excepcion")
		public String globalMiExcepcion() throws ExcepcionGlobal {
			throw new ExcepcionGlobal();

			//return "demoth-mi-excepcion"; No lo ponemos porque ya lo hace MiExcepcion y nos redirige a error-mi-excepcion.html
		}
	
}
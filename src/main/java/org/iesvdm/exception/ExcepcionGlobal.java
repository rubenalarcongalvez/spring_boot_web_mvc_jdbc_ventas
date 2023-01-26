package org.iesvdm.exception;

//Realmente podemos heredar de excepciones más concretas si queremos
public class ExcepcionGlobal extends Exception { //Hereda de Exception general
	private static final long serialVersionUID = 1L;

	public ExcepcionGlobal() {
		super("Mensaje de mi exception... PERSONALIZADO GLOBAL"); //Establezco mi mensaje de excepción
	}
}
package org.iesvdm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlNumberInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

@SpringBootTest
public class VentasApplicationTests {
	WebClient webClient;

	@BeforeEach
	void setup(WebApplicationContext context) {
		webClient = MockMvcWebClientBuilder.webAppContextSetup(context).build();
	}

	@Test
	void validationForm() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		HtmlPage validationFormPage = webClient.getPage("http://localhost/clientes/crear"); // Cargo la página

		HtmlForm validationForm = validationFormPage.getHtmlElementById("idFormularioCrearCliente"); // Digo que quiero
																										// revisar el
																										// formulario

		//CUIDADO QUE HAY QUE PONER id en el html SOLO A LOS CAMPOS QUE NO TENGAN th:field
		
		// ACCEDO A LOS INPUTS Y SETEO VALORES
		HtmlTextInput nombreInput = validationFormPage.getHtmlElementById("nombre");
		nombreInput.setValueAttribute("Juan");

		HtmlTextInput apellido1Input = validationFormPage.getHtmlElementById("apellido1");
		apellido1Input.setValueAttribute("Roque");

		HtmlTextInput emailInput = validationFormPage.getHtmlElementById("email");
		emailInput.setValueAttribute("juanpedro1975@gmail.com");

		HtmlTextInput ciudadInput = validationFormPage.getHtmlElementById("ciudad");
		ciudadInput.setValueAttribute("Málaga");

		HtmlNumberInput categoriaInput = validationFormPage.getHtmlElementById("categoria");
		categoriaInput.setValueAttribute("1000");

		HtmlSubmitInput submit = validationForm.getOneHtmlElementByAttribute("input", "type", "submit");
		HtmlPage newValidationPage = submit.click(); // Pincho en submit

		// COMPRUEBO CON LOS TESTS QUE QUIERA PARA VER QUE SE HA INSERTADO EN CLIENTES
		assertThat(newValidationPage.getUrl().toString()).endsWith("/clientes");

		// LA PRUEBA LA HARÍAMOS CON LA BD VACÍA, y en este caso, daría correcto, porque
		// el único que habría sería Juan y estaría correcto
		String nombreNew = newValidationPage.getHtmlElementById("nombre").getTextContent(); // Por ejemplo, accedo al
																							// elemento que tiene el id
																							// "nombre" al cuerpo de
																							// texto que tenga (el
																							// contenido del div en este
																							// caso)
		assertThat(nombreNew).isEqualTo("Juan");

		String apellido1New = newValidationPage.getHtmlElementById("apellido1").getTextContent();
		assertThat(apellido1New).isEqualTo("Roque");

		String emailNew = newValidationPage.getHtmlElementById("email").getTextContent();
		assertThat(emailNew).isEqualTo("juan@gmail.com");

		String ciudadNew = newValidationPage.getHtmlElementById("ciudad").getTextContent();
		assertThat(ciudadNew).isEqualTo("Málaga");

		String categoriaNew = newValidationPage.getHtmlElementById("categoria").getTextContent();
		assertThat(categoriaNew).isEqualTo("1000");

	}
}
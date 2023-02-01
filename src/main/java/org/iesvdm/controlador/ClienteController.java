package org.iesvdm.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.mapstruct.ClienteMapper;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.service.ClienteService;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.validation.Valid;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /clientes como
//prefijo.
//@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ClienteMapper clienteMapper;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
//	public ClienteController(ClienteService clienteService) {
//		this.clienteService = clienteService;
//	}
	
	//@RequestMapping(value = "/clientes", method = RequestMethod.GET)
	//equivalente a la siguiente anotación
	//Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
	@GetMapping({"/clientes", "/clientes/"}) 
	public String listar(Model model) {
		
		List<Cliente> listaClientes =  clienteService.listAll();
		model.addAttribute("listaClientes", listaClientes);
				
		return "clientes";
		
	}
	
	@GetMapping("/clientes/{id}")
	public String detalle(Model model, @PathVariable Integer id ) {
		
		boolean tienePedidos = true;
		
		Cliente cliente = clienteService.one(id);
		
		ArrayList<HashMap<String, Object>> listaDatos = clienteService.obtenerDatosAdicionales(id);
		
		ArrayList<ClienteDTO> listaClientesComerciales = new ArrayList<>();
		
		ClienteDTO clienteDTO;
		
		if(listaDatos.isEmpty()) {
			listaClientesComerciales.add(clienteMapper.listaPedidos(cliente, "NO TIENE", -1, -1, -1, -1, -1));
			tienePedidos = false;
		} else {
			for (int i = 0; i < listaDatos.size(); i++) {
				clienteDTO = clienteMapper.listaPedidos(cliente,
						(String) listaDatos.get(i).get("nombreComercial"),
						(int) listaDatos.get(i).get("idComercial"),
						(int) listaDatos.get(i).get("numPedidosTotal"),
						(int) listaDatos.get(i).get("numPedidosTrimestre"),
						(int) listaDatos.get(i).get("numPedidosAnio"),
						(int) listaDatos.get(i).get("numPedidosLustro"));

				listaClientesComerciales.add(clienteDTO);
			}
		}
		
		model.addAttribute("listaClientesComerciales", listaClientesComerciales);
		
		model.addAttribute("tienePedidos", tienePedidos);
		
		return "detalle-cliente";
		
	}
	
	@GetMapping("/clientes/crear")
	public String crear(@ModelAttribute Cliente cliente, Model model) {
				
//		Cliente cliente = new Cliente();
//		model.addAttribute(cliente);
		
		return "crear-cliente";
		
	}
	
	@PostMapping("/clientes/crear")
	public String submitCrear(@Valid @ModelAttribute Cliente cliente, BindingResult bindingResulted, Model model) {
		
		String vista = "";

		if (bindingResulted.hasErrors()) {
			model.addAttribute("cliente", cliente);
			model.addAttribute("toString", cliente.toString());
			vista = "crear-cliente";
		} else {
			clienteService.newCliente(cliente);
			vista = "redirect:/clientes"; //En el caso que sea correcto, devolvemos una redirección a la vista /empleados
		}
		
		return vista; //retornamos el html que referenciamos
		
	}
	
	@GetMapping("/clientes/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		
		Cliente cliente = clienteService.one(id);
		model.addAttribute("cliente", cliente);
		
		return "editar-cliente";
		
	}
	
	@PostMapping("/clientes/editar/{id}") 
	public String editar(@Valid @ModelAttribute Cliente cliente, BindingResult bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("cliente", cliente);
			model.addAttribute("toString", cliente.toString());
			
			return "editar-cliente";
		} else {
			clienteService.replaceCliente(cliente);
			return "redirect:/clientes";
		}
		
	}
	
	@PostMapping("/clientes/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {
		
		clienteService.deleteCliente(id);
		
		return new RedirectView("/clientes");
	}

}

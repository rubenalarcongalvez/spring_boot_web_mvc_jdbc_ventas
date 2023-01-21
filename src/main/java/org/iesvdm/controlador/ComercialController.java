package org.iesvdm.controlador;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.iesvdm.dto.ComercialDTO;
import org.iesvdm.dto.PedidoDTO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.service.ComercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /comerciales como
//prefijo.
//@RequestMapping({"/comerciales", "/comerciales/"})
public class ComercialController {
	
	private ComercialService comercialService;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ComercialController(ComercialService comercialService) {
		this.comercialService = comercialService;
	}
	
	//@RequestMapping(value = "/comerciales", method = RequestMethod.GET)
	//equivalente a la siguiente anotación
	@GetMapping({"/comerciales", "/comerciales/"}) //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
	public String listar(Model model) {
		
		List<Comercial> listaComerciales =  comercialService.listAllComercial();
		model.addAttribute("listaComerciales", listaComerciales);
				
		return "comerciales";
		
	}
	
	@GetMapping("/comerciales/{id}")
	public String detalle(Model model, @PathVariable Integer id ) {
		
		ComercialDTO comercial = comercialService.oneComercialDTO(id);
		model.addAttribute("comercial", comercial);
		
		List<PedidoDTO> pedidos = comercialService.onePedido(id);
		model.addAttribute("pedidos", pedidos);
		
		OptionalDouble max = pedidos.stream().mapToDouble(PedidoDTO::getTotal).max();
		
		double maximo = 0;
		
		if (max.isPresent())
			maximo = max.getAsDouble();
		
		OptionalDouble min = pedidos.stream().mapToDouble(PedidoDTO::getTotal).min();
		
		double minimo = 0;
		
		if (min.isPresent())
			minimo = min.getAsDouble();
		
		model.addAttribute("pedidoMaximo", maximo);
		model.addAttribute("pedidoMinimo", minimo);
		
		List<PedidoDTO> pedidosOrdenadosCliente = pedidos.stream().sorted(Comparator.comparing(PedidoDTO::getTotal)).toList();
		
		model.addAttribute("pedidosOrden", pedidosOrdenadosCliente);
		
		return "detalle-comercial";
		
	}
	
	@GetMapping("/comerciales/crear")
	public String crear(Model model) {
				
		Comercial comercial = new Comercial();
		model.addAttribute(comercial);
		
		return "crear-comercial";
		
	}
	
	@PostMapping("/comerciales/crear")
	public RedirectView submitCrear(@ModelAttribute("comercial") Comercial comercial) {
		
		comercialService.newComercial(comercial);
				
		return new RedirectView("/comerciales") ;
		
	}
	
	@GetMapping("/comerciales/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		
		Comercial comercial = comercialService.oneComercial(id);
		model.addAttribute("comercial", comercial);
		
		return "editar-comercial";
		
	}
	
	
	@PostMapping("/comerciales/editar/{id}")
	public RedirectView submitEditar(@ModelAttribute("comercial") Comercial comercial) {
		
		comercialService.replaceComercial(comercial);		
		
		return new RedirectView("/comerciales");
	}
	
	@PostMapping("/comerciales/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {
		
		comercialService.deleteComercial(id);
		
		return new RedirectView("/comerciales");
	}

}

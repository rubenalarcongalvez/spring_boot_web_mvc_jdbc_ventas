package org.iesvdm.service;

import java.util.List;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.ComercialDTO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.dao.PedidoDTO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ComercialService {

	private ComercialDAO comercialDAO;
	private PedidoDAO pedidoDAO;

	// Se utiliza inyección automática por constructor del framework Spring.
	// Por tanto, se puede omitir la anotación Autowired
	// @Autowired
	public ComercialService(ComercialDAO comercialDAO, PedidoDAO pedidoDAO) {
		this.comercialDAO = comercialDAO;
		this.pedidoDAO = pedidoDAO;
	}

	public List<Comercial> listAllComercial() {

		return comercialDAO.getAll();

	}
	
	public List<Pedido> listAllPedido() {

		return pedidoDAO.getAll();

	}
	
	public Comercial oneComercial(Integer id) {
		Optional<Comercial> optCom = comercialDAO.find(id);
		if (optCom.isPresent())
			return optCom.get();
		else 
			return null;
	}
	
	public ComercialDTO oneComercialDTO(Integer id) {
		Optional<ComercialDTO> optCom = comercialDAO.findPlusStatsPedidos(id);
		
		if (optCom.isPresent()) {
			return optCom.get();} 
		else 
			return null;
	}

	public List<PedidoDTO> onePedido(Integer id) {
		List<PedidoDTO> peds = pedidoDAO.findPlusNombreCliente(id);
		
		log.info("Devueltos {} registros.", peds.size());
		
		return peds;
	}

	public void newComercial(Comercial comercial) {

		comercialDAO.create(comercial);

	}
	
	public void replaceComercial(Comercial comercial) {
		
		comercialDAO.update(comercial);
		
	}
	
	public void deleteComercial(int id) {
		
		comercialDAO.delete(id);
		
	}

}

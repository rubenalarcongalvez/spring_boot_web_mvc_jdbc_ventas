package org.iesvdm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteDAO clienteDAO;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
//	public ClienteService(ClienteDAO clienteDAO) {
//		this.clienteDAO = clienteDAO;
//	}
	
	public List<Cliente> listAll() {
		
		return clienteDAO.getAll();
		
	}
	
	public Cliente one(Integer id) {
		Optional<Cliente> optCom = clienteDAO.find(id);
		if (optCom.isPresent())
			return optCom.get();
		else 
			return null;
	}
	
	public ArrayList<HashMap<String, Object>> obtenerDatosAdicionales(long id_cliente) {
		ArrayList<Integer> IdsComerciales = clienteDAO.getIdComerciales(id_cliente);
		
		ArrayList<HashMap<String, Object>> listaDatos = clienteDAO.getClienteComercial(IdsComerciales);
		
		return listaDatos;
	}

	public void newCliente(Cliente cliente) {

		clienteDAO.create(cliente);

	}
	
	public void replaceCliente(Cliente cliente) {
		
		clienteDAO.update(cliente);
		
	}
	
	public void deleteCliente(int id) {
		
		clienteDAO.delete(id);
		
	}

}

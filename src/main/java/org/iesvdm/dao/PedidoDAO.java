package org.iesvdm.dao;

import java.util.List;

import org.iesvdm.dto.PedidoDTO;
import org.iesvdm.modelo.Pedido;

public interface PedidoDAO {

	public void create(Pedido pedido);
	
	public List<Pedido> getAll();
	public List<Pedido> find(long id);
	
	public void update(Pedido pedido);
	
	public void delete(long id);

	List<PedidoDTO> findPlusNombreCliente(long id_comercial);
	
}

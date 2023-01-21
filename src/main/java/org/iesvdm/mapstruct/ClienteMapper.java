package org.iesvdm.mapstruct;

import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.modelo.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
	
	@Mapping(target = "nombreComercial", source = "nombreComercialIn")
	@Mapping(target = "idComercial", source = "idComercialIn")
	@Mapping(target = "numPedidosTotal", source = "numPedidosTotalIn")
	@Mapping(target = "numPedidosTrimestre", source = "numPedidosTrimestreIn")
	@Mapping(target = "numPedidosAnio", source = "numPedidosAnioIn")
	@Mapping(target = "numPedidosLustro", source = "numPedidosLustroIn")
	public ClienteDTO listaPedidos(Cliente cliente, String nombreComercialIn, int idComercialIn, int numPedidosTotalIn, int numPedidosTrimestreIn, int numPedidosAnioIn, int numPedidosLustroIn);
	
	public ClienteDTO listaClientes(ClienteDTO cliente);
	
}
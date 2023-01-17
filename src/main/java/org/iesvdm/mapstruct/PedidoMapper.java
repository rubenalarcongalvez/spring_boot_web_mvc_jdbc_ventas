package org.iesvdm.mapstruct;

import org.iesvdm.dto.PedidoDTO;
import org.iesvdm.modelo.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
	
	@Mapping(target = "nombre_cliente", source = "nombreCliente")
	public PedidoDTO comercialAComercialDTO(Pedido pedido, String nombreCliente);
	
	public Pedido comercialDTOAComercial(PedidoDTO pedido);
	
}
package org.iesvdm.mapstruct;

import org.iesvdm.dto.ComercialDTO;
import org.iesvdm.modelo.Comercial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComercialMapper {
	
	@Mapping(target = "totalFacturado", source = "totalFacturadoIn")
	@Mapping(target = "mediaFacturada", source = "mediaFacturadaIn")
	public ComercialDTO comercialAComercialDTO(Comercial comercial, double totalFacturadoIn, double mediaFacturadaIn);
	
	public Comercial comercialDTOAComercial(ComercialDTO comercial);
	
}
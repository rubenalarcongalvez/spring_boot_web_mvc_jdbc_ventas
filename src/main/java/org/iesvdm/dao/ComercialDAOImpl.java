package org.iesvdm.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.dto.ComercialDTO;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
@Repository
//Utilizo lombok para generar el constructor
@AllArgsConstructor
public class ComercialDAOImpl implements ComercialDAO {

	//JdbcTemplate se inyecta por el constructor de la clase automáticamente
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Inserta en base de datos el nuevo Comercial, actualizando el id en el bean Comercial.
	 */
	@Override	
	public synchronized void create(Comercial comercial) {
		
							//Desde java15+ se tiene la triple quote """ para bloques de texto como cadenas.
		String sqlInsert = """
							INSERT INTO comercial (nombre, apellido1, apellido2, comisión) 
							VALUES  (     ?,         ?,         ?,       ?)
						   """;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setString(idx++, comercial.getNombre());
			ps.setString(idx++, comercial.getApellido1());
			ps.setString(idx++, comercial.getApellido2());
			ps.setBigDecimal(idx, comercial.getComision());
			return ps;
		},keyHolder);
		
		comercial.setId(keyHolder.getKey().intValue());

		log.info("Insertados {} registros.", rows);
	}

	/**
	 * Devuelve lista con todos los Comerciales.
	 */
	@Override
	public List<Comercial> getAll() {
		
		List<Comercial> listComercial = jdbcTemplate.query(
                "SELECT * FROM comercial",
                (rs, rowNum) -> new Comercial(rs.getInt("id"), 
                							  rs.getString("nombre"), 
                							  rs.getString("apellido1"),
                							  rs.getString("apellido2"), 
                							  rs.getBigDecimal("comisión"))
                						 	
        );
		
		log.info("Devueltos {} registros.", listComercial.size());
		
        return listComercial;
	}

	/**
	 * Devuelve Optional de Comercial con el ID dado.
	 */
	@Override
	public Optional<Comercial> find(long id) {
		
		Comercial com =  jdbcTemplate
				.queryForObject("SELECT * FROM comercial WHERE id = ?"														
								, (rs, rowNum) -> new Comercial(rs.getInt("id"),
            						 						rs.getString("nombre"),
            						 						rs.getString("apellido1"),
            						 						rs.getString("apellido2"),
            						 						rs.getBigDecimal("comisión")) 
								, id
								);
		
		if (com != null) { 
			return Optional.of(com);}
		else { 
			log.info("Comercial no encontrado.");
			return Optional.empty(); }
        
	}
	/**
	 * Actualiza Comercial con campos del bean Comercial según ID del mismo.
	 */
	@Override
	public void update(Comercial comercial) {
		
		int rows = jdbcTemplate.update("""
										UPDATE comercial SET 
														nombre = ?, 
														apellido1 = ?, 
														apellido2 = ?,
														comisión = ?  
												WHERE id = ?
										""", comercial.getNombre()
										, comercial.getApellido1()
										, comercial.getApellido2()
										, comercial.getComision()
										, comercial.getId());
		
		log.info("Update de Comercial con {} registros actualizados.", rows);
    
	}

	/**
	 * Borra Comercial con ID proporcionado.
	 */
	@Override
	public void delete(long id) {
		
		int rows = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);
		
		log.info("Delete de Comercial con {} registros eliminados.", rows);		
		
	}

	/**
	 * Lista todos los comerciales y las estadísticas de sus pedidos globales
	 */
	@Override
	public Optional<ComercialDTO> findPlusStatsPedidos(int id_comercial) {
        ComercialDTO com =  jdbcTemplate
				.queryForObject("SELECT c.*, sum(p.total) as total_facturado ,avg(p.total) as media_facturada FROM comercial c INNER JOIN pedido p ON c.id = p.id_comercial WHERE id_comercial = ?",
		                (rs, rowNum) -> new ComercialDTO(rs.getInt("id"), 
  							  rs.getString("nombre"), 
  							  rs.getString("apellido1"),
  							  rs.getString("apellido2"), 
  							  rs.getBigDecimal("comisión").setScale(2, BigDecimal.ROUND_HALF_EVEN),
  							  Math.round(rs.getDouble("total_facturado") * 100) / 100.0,
  							  Math.round(rs.getDouble("media_facturada") * 100) / 100.0)
  						 	, id_comercial
);
		
		if (com != null) { 
			return Optional.of(com);}
		else { 
			log.info("Comercial no encontrado.");
			return Optional.empty(); }
	}

}

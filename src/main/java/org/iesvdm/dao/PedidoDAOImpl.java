package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Pedido;
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
public class PedidoDAOImpl implements PedidoDAO {

	//JdbcTemplate se inyecta por el constructor de la clase automáticamente
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Inserta en base de datos el nuevo Pedido, actualizando el id en el bean Pedido.
	 */
	@Override	
	public synchronized void create(Pedido pedido) {
		
							//Desde java15+ se tiene la triple quote """ para bloques de texto como cadenas.
		String sqlInsert = """
							INSERT INTO pedido (total, fecha, id_cliente, id_comercial) 
							VALUES  (     ?,         ?,         ?,       ?)
						   """;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setDouble(idx++, pedido.getTotal());
			ps.setDate(idx++, pedido.getFecha());
			ps.setLong(idx++, pedido.getId_cliente());
			ps.setLong(idx, pedido.getId_comercial());
			return ps;
		},keyHolder);
		
		pedido.setId(keyHolder.getKey().intValue());

		log.info("Insertados {} registros.", rows);
	}

	/**
	 * Devuelve lista con todos los Pedidos.
	 */
	@Override
	public List<Pedido> getAll() {
		
		List<Pedido> listPedido = jdbcTemplate.query(
                "SELECT * FROM pedido",
                (rs, rowNum) -> new Pedido(rs.getLong("id"), 
                							  rs.getDouble("total"), 
                							  rs.getDate("fecha"),
                							  rs.getLong("id_cliente"), 
                							  rs.getLong("id_comercial"))
                						 	
        );
		
		log.info("Devueltos {} registros.", listPedido.size());
		
        return listPedido;
	}

	/**
	 * Devuelve Optional de Pedido con el ID dado.
	 */
	@Override
	public List<Pedido> find(long id_comercial) {
		
		List<Pedido> peds =  jdbcTemplate
				.query("SELECT * FROM pedido WHERE id_comercial = ?"														
								, (rs, rowNum) -> new Pedido(rs.getLong("id"), 
          							  rs.getDouble("total"), 
          							  rs.getDate("fecha"),
          							  rs.getLong("id_cliente"), 
          							  rs.getLong("id_comercial"))
								, id_comercial
								);
		
		log.info("Devueltos {} registros.", peds.size());
		
		return peds;
        
	}
	
	/**
	 * Devuelve Optional de Pedido con el ID dado.
	 */
	@Override
	public List<PedidoDTO> findPlusNombreCliente(long id_comercial) {
		List<PedidoDTO> peds =  jdbcTemplate
				.query("SELECT p.*, concat_ws(' ', c.nombre, c.apellido1, c.apellido2) as nombreCliente FROM pedido p INNER JOIN cliente c ON p.id_cliente = c.id WHERE id_comercial = ?;"														
								, (rs, rowNum) -> new PedidoDTO(rs.getLong("id"), 
          							  Math.round(rs.getDouble("p.total") * 100) / 100.0, 
          							  rs.getDate("p.fecha"),
          							  rs.getLong("p.id_cliente"), 
          							  rs.getLong("p.id_comercial"),
          							  rs.getString("nombreCliente"))
								, id_comercial
								);
		
		log.info("Devueltos {} registros.", peds.size());
		
		return peds;
        
	}
	
	/**
	 * Actualiza Pedido con campos del bean Pedido según ID del mismo.
	 */
	@Override
	public void update(Pedido pedido) {
		
		int rows = jdbcTemplate.update("""
										UPDATE pedido SET 
														total = ?, 
														fecha = ?, 
														id_cliente = ?,
														id_comercial = ?  
												WHERE id = ?
										""", pedido.getTotal()
										, pedido.getFecha()
										, pedido.getId_cliente()
										, pedido.getId_comercial()
										, pedido.getId());
		
		log.info("Update de Pedido con {} registros actualizados.", rows);
    
	}

	/**
	 * Borra Pedido con ID proporcionado.
	 */
	@Override
	public void delete(long id) {
		
		int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);
		
		log.info("Delete de Pedido con {} registros eliminados.", rows);		
		
	}

}

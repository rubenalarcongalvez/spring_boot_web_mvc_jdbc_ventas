package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
//Un Repository es un componente y a su vez un estereotipo de Spring 
//que forma parte de la ‘capa de persistencia’.
@Repository
public class ClienteDAOImpl implements ClienteDAO {

	 //Plantilla jdbc inyectada automáticamente por el framework Spring, gracias a la anotación @Autowired.
	 @Autowired
	 private JdbcTemplate jdbcTemplate;
	
	/**
	 * Inserta en base de datos el nuevo Cliente, actualizando el id en el bean Cliente.
	 */
	@Override	
	public synchronized void create(Cliente cliente) {
		
							//Desde java15+ se tiene la triple quote """ para bloques de texto como cadenas.
		String sqlInsert = """
							INSERT INTO cliente (nombre, apellido1, apellido2, ciudad, categoría) 
							VALUES  (     ?,         ?,         ?,       ?,         ?)
						   """;
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); // Generamos el KeyHolder
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> { // Actualizamos las filas para añadir una más
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" }); // Con esto, creamos una fila más
			int idx = 1; //Decimos por dónde empieza
			ps.setString(idx++, cliente.getNombre()); //Para la columna 1, asignamos el nombre del cliente y luego sumamos el idx.
			ps.setString(idx++, cliente.getApellido1());
			ps.setString(idx++, cliente.getApellido2());
			ps.setString(idx++, cliente.getCiudad());
			ps.setInt(idx, cliente.getCategoria());
			return ps; // Devuelve el número de filas insertadas
		},keyHolder);
		
		cliente.setId(keyHolder.getKey().intValue()); //Como hemos obtenido el id gracias al KeyHolder, lo asignamos al objeto cliente
		
		//Sin recuperación de id generado
//		int rows = jdbcTemplate.update(sqlInsert,
//							cliente.getNombre(),
//							cliente.getApellido1(),
//							cliente.getApellido2(),
//							cliente.getCiudad(),
//							cliente.getCategoria()
//					);

		log.info("Insertados {} registros.", rows); //Mostramos el log si queremos
	}

	/**
	 * Devuelve lista con todos los Clientes.
	 */
	@Override
	public List<Cliente> getAll() {
		
		List<Cliente> listClie = jdbcTemplate.query( // Ejecutamos directamente la row que nos devuelve la lista
                "SELECT * FROM cliente",
                (rs, rowNum) -> new Cliente(rs.getInt("id"),
                						 	rs.getString("nombre"),
                						 	rs.getString("apellido1"),
                						 	rs.getString("apellido2"),
                						 	rs.getString("email"),
                						 	rs.getString("ciudad"),
                						 	rs.getInt("categoría")
                						 	)
        );
		
		log.info("Devueltos {} registros.", listClie.size());
		
        return listClie;
        
	}

	/**
	 * Devuelve Optional de Cliente con el ID dado.
	 */
	@Override
	public Optional<Cliente> find(long id) {
		
		Cliente clie =  jdbcTemplate
				.queryForObject("SELECT * FROM cliente WHERE id = ?"														
								, (rs, rowNum) -> new Cliente(rs.getInt("id"),
            						 						rs.getString("nombre"),
            						 						rs.getString("apellido1"),
            						 						rs.getString("apellido2"),
            						 						rs.getString("email"),
            						 						rs.getString("ciudad"),
            						 						rs.getInt("categoría")) 
								, id
								);
		
		if (clie != null) { 
			return Optional.of(clie);}
		else { 
			log.info("Cliente no encontrado.");
			return Optional.empty(); }
        
	}
	/**
	 * Actualiza Cliente con campos del bean Cliente según ID del mismo.
	 */
	@Override
	public void update(Cliente cliente) {
		
		int rows = jdbcTemplate.update("""
										UPDATE cliente SET 
														nombre = ?, 
														apellido1 = ?, 
														apellido2 = ?,
														email = ?,
														ciudad = ?,
														categoría = ?  
												WHERE id = ?
										""", cliente.getNombre()
										, cliente.getApellido1()
										, cliente.getApellido2()
										, cliente.getEmail()
										, cliente.getCiudad()
										, cliente.getCategoria()
										, cliente.getId());
		
		log.info("Update de Cliente con {} registros actualizados.", rows);
    
	}

	/**
	 * Borra Cliente con ID proporcionado.
	 */
	@Override
	public void delete(long id) {
		
		int rows = jdbcTemplate.update("DELETE FROM cliente WHERE id = ?", id);
		
		log.info("Delete de Cliente con {} registros eliminados.", rows);		
		
	}
	
	public ArrayList<Integer> getIdComerciales(long id_cliente) {
		
		ArrayList<Integer> listaIds = new ArrayList<>();
		
		jdbcTemplate.query(
                """
                		SELECT 
						    ped.id_comercial
						FROM
						    comercial com
						        INNER JOIN
						    pedido ped ON com.id = ped.id_comercial
						        INNER JOIN
						    cliente clie ON clie.id = ped.id_cliente
						WHERE
						    clie.id = ?
						GROUP BY ped.id_comercial;
                		""",
                (rs, rowNum) -> listaIds.add(rs.getInt("id_comercial"))
                , id_cliente);
		
		return listaIds;
	}
	
	/**
	 * Devuelve lista con todos los parámetros necesarios de los pedidos a comerciales.
	 * @return 
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getClienteComercial(ArrayList<Integer> listaIds) {
		
		ArrayList<HashMap<String, Object>> listaDatos = new ArrayList<>();
		
		for (int i = 0; i < listaIds.size(); i++) {
			listaDatos.add(new HashMap<>());
			
			listaDatos.get(i).put("idComercial", listaIds.get(i));		
			final int ii = i;
			
			jdbcTemplate.query(
					"""
	        		SELECT 
					    CONCAT_WS(' ',
					            com.nombre,
					            com.apellido1,
					            com.apellido2) AS nombreComercial
					FROM
					    comercial com
					        INNER JOIN
					    pedido ped ON com.id = ped.id_comercial
					        INNER JOIN
					    cliente clie ON clie.id = ped.id_cliente
					WHERE
					    com.id = ? group by com.id;
	        		""",(rs, rowNum) -> listaDatos.get(ii).put("nombreComercial", rs.getString("nombreComercial"))
	        , listaIds.get(i));
			
			jdbcTemplate.query(
					"""
	        		SELECT 
					    COUNT(ped.id) AS numPedidosTotal
					FROM
					    comercial com
					        INNER JOIN
					    pedido ped ON com.id = ped.id_comercial
					        INNER JOIN
					    cliente clie ON clie.id = ped.id_cliente
					WHERE
					    com.id = ?;
	        		""",(rs, rowNum) -> listaDatos.get(ii).put("numPedidosTotal", rs.getInt("numPedidosTotal"))
	    	        , listaIds.get(i));
			
			jdbcTemplate.query(
					"""
	    		SELECT 
				    COUNT(ped.id) AS numPedidosTrimestre
				FROM
				    comercial com
				        INNER JOIN
				    pedido ped ON com.id = ped.id_comercial
				        INNER JOIN
				    cliente clie ON clie.id = ped.id_cliente
				WHERE
				    com.id = ? AND datediff(curdate(), ped.fecha) < (90);
	        		""",(rs, rowNum) -> listaDatos.get(ii).put("numPedidosTrimestre", rs.getInt("numPedidosTrimestre"))
	    	        , listaIds.get(i));
			
			jdbcTemplate.query(
					"""
	    		SELECT 
				    COUNT(ped.id) AS numPedidosAnio
				FROM
				    comercial com
				        INNER JOIN
				    pedido ped ON com.id = ped.id_comercial
				        INNER JOIN
				    cliente clie ON clie.id = ped.id_cliente
				WHERE
				    com.id = ? AND datediff(curdate(), ped.fecha) < (365);
	        		""",(rs, rowNum) -> listaDatos.get(ii).put("numPedidosAnio", rs.getInt("numPedidosAnio"))
	    	        , listaIds.get(i));
			
			jdbcTemplate.query(
					"""
	    		SELECT 
				    COUNT(ped.id) AS numPedidosLustro
				FROM
				    comercial com
				        INNER JOIN
				    pedido ped ON com.id = ped.id_comercial
				        INNER JOIN
				    cliente clie ON clie.id = ped.id_cliente
				WHERE
				    com.id = ? AND datediff(curdate(), ped.fecha) < (365 * 5);
	        		""",(rs, rowNum) -> listaDatos.get(ii).put("numPedidosLustro", rs.getInt("numPedidosLustro"))
	    	        , listaIds.get(i));
		}
		
		return listaDatos;
        
	}
	
}

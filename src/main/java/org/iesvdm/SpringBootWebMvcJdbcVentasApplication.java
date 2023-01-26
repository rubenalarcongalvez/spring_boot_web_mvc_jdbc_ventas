package org.iesvdm;

import java.math.BigDecimal;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringBootWebMvcJdbcVentasApplication implements CommandLineRunner {

	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private ComercialDAO comercialDAO;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebMvcJdbcVentasApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		log.info("*******************************");
		log.info("*Prueba de arranque ClienteDAO y ComercialDAO*");
		log.info("*******************************");

		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));
		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));

		int id = 1;
		Optional<Cliente> cliente = clienteDAO.find(id);
		Optional<Comercial> comercial = comercialDAO.find(id);

		if (cliente.isPresent()) {
			log.info("Cliente {}: {}", id, cliente.get());

			String nombreOld = cliente.get().getNombre();

			cliente.get().setNombre("Jose M");

			clienteDAO.update(cliente.get());

			cliente = clienteDAO.find(id);

			log.info("Cliente {}: {}", id, cliente.get());

			// Volvemos a cargar el nombre antiguo..
			cliente.get().setNombre(nombreOld);
			clienteDAO.update(cliente.get());

		}

		if (comercial.isPresent()) {
			log.info("Comercial {}: {}", id, comercial.get());

			String nombreOldCom = comercial.get().getNombre();

			comercial.get().setNombre("Alfonso P");

			comercialDAO.update(comercial.get());

			comercial = comercialDAO.find(id);

			log.info("Comercial {}: {}", id, comercial.get());

			// Volvemos a cargar el nombre antiguo..
			comercial.get().setNombre(nombreOldCom);
			comercialDAO.update(comercial.get());
		}

		// Como es un cliente nuevo a persistir, id a 0
		Cliente clienteNew = new Cliente(0, "Jose M", "Martín", "josemmartin@gmail.com" , null, "Málaga", 100);

		// Como es un comercial nuevo a persistir, id a 0
		Comercial comercialNew = new Comercial(0, "Alfredo P", "Porras", null, BigDecimal.ZERO);

		// create actualiza el id
		clienteDAO.create(clienteNew);
		comercialDAO.create(comercialNew);

		log.info("Cliente nuevo con id = {}", clienteNew.getId());
		log.info("Comercial nuevo con id = {}", comercialNew.getId());

		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));

		// borrando por el id obtenido de create
		clienteDAO.delete(clienteNew.getId());
		comercialDAO.delete(comercialNew.getId());

		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));
		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));

		log.info("************************************");
		log.info("*FIN: Prueba de arranque ClienteDAO y ComercialDAO*");
		log.info("************************************");

	}

}

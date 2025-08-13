package com.example.demo.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    ClienteEntity findFirstByDocumentoOrderByIdAsc(String mDocumento);

    List<ClienteEntity> findAll();

    Optional<ClienteEntity> findByEmail(String mEmail);

    @Query("SELECT COUNT(c) > 0 FROM ClienteEntity c WHERE (c.email = :mEmail)")
    Boolean getEmail(@Param("mEmail") String mEmail);

    @Query(value = """
           SELECT nome, COUNT(*) AS quantidade
           FROM (SELECT com_id_cliente AS cliente_id, cli_nome AS nome
                 FROM compra
                 LEFT JOIN cliente ON (cli_id = com_id_cliente)
                 UNION ALL
                 SELECT ven_id_cliente AS cliente_id, cli_nome AS nome
                 FROM vendas
                 LEFT JOIN cliente ON (cli_id = ven_id_cliente)) AS todas
           GROUP BY cliente_id
           ORDER BY quantidade DESC
           LIMIT 5
           """, nativeQuery = true)
    List<ClienteQuantidadeDTO>buscarTop5Clientes();
}

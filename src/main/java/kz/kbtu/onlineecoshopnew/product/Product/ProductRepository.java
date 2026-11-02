package kz.kbtu.onlineecoshopnew.product.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = {"category"})
    @Query("select p from Product p")
    Page<Product> findAllWithCategory(Specification<Product> spec, Pageable pageable);

    @Modifying
    @Query("""
            update Product p
            set p.stockQuantity = p.stockQuantity - :qty
            where p.id = :id and p.stockQuantity >= :qty
            """)
    int decreaseStock(@Param("id") UUID id, @Param("qty") int qty);

}

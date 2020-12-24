package elena.kozlova.orange;

import elena.kozlova.orange.entity.ClidAddrId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface OrangeRepository extends JpaRepository<ClidAddrId,String>, PagingAndSortingRepository<ClidAddrId, String> {

    Optional<ClidAddrId> findByClid(String clid);

    @Query("select ca from ClidAddrId ca where ca.clid in :clidList")
    List<ClidAddrId> findAllByClidList(@Param("clidList") List<String> clidList);

    Page<ClidAddrId> findAll(Pageable pageable);
}

package elena.kozlova.orange;

import elena.kozlova.orange.entity.ClidAddrId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью ClidAddrId
 */
@Repository
public interface OrangeRepository extends JpaRepository<ClidAddrId,String>, PagingAndSortingRepository<ClidAddrId, String> {

    /**
     * Поиск всех записей в базе данных с данными значениями CLID из списка
     * @param clidList список телефонов
     * @return список сущностей ClidAddrId
     */
    @Query("select ca from ClidAddrId ca where ca.clid in :clidList")
    List<ClidAddrId> findAllByClidList(@Param("clidList") List<String> clidList);

    /**
     * Поиск всех записей в базе данных
     * @param pageable настройки пагинации
     * @return страница сущностей ClidAddrId
     */
    Page<ClidAddrId> findAll(Pageable pageable);
}

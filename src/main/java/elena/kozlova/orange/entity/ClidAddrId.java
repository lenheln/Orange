package elena.kozlova.orange.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Сущность соответсвий CLID(номер телефона) и идентификатора адреса
 */
@Entity
@Table(name = "clid")
@NoArgsConstructor
@Getter @Setter
public class ClidAddrId {

    //Номер телефона
    @Id()
    private String clid;

    //Идентификатор адреса
    @Column(name = "addr_id")
    private BigDecimal addrId;
}

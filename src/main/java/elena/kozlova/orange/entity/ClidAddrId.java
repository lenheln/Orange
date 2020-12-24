package elena.kozlova.orange.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "clid")
@NoArgsConstructor
@Getter @Setter
public class ClidAddrId {

    @Id()
    private String clid;

    @Column(name = "addr_id")
    private BigDecimal addrId;
}

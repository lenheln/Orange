package elena.kozlova.orange.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс Адрес
 */
@NoArgsConstructor
@Getter @Setter
public class Address implements Serializable {

    //Идентификатор адреса в rest-сервисе
    @JsonProperty("addr_id")
    private Integer addrId;

    //Строка с адресом
    @JsonProperty("full_address")
    private String fullAddress;
}

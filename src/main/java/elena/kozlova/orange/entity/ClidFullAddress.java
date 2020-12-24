package elena.kozlova.orange.entity;

import lombok.*;

/**
 * Класс соответствий телефон-адрес
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@ToString
public class ClidFullAddress {

    //Телефонный номер (clid)
    private String clid;

    //Строка с адресом
    private String fullAddress;
}

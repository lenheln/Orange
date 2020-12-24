package elena.kozlova.orange.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter @Setter
public class Address implements Serializable {

    @JsonProperty("addr_id")
    private Integer addrId;

    @JsonProperty("full_address")
    private String fullAddress;
}

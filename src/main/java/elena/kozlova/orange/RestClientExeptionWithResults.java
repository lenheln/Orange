package elena.kozlova.orange;

import elena.kozlova.orange.entity.ClidFullAddress;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

public class RestClientExeptionWithResults extends RestClientException {

    List<ClidFullAddress> clidFullAddressList = new ArrayList<>();

    public RestClientExeptionWithResults(String msg, List<ClidFullAddress> clidFullAddresses) {
        super(msg);
        this.clidFullAddressList = clidFullAddresses;
    }
}

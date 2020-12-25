package elena.kozlova.orange.exceptions;

import elena.kozlova.orange.entity.ClidFullAddress;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

/**
 * Исключение, возникающее при отключении внешнего сервиса
 */
public class RestClientExeptionWithResults extends RestClientException {

    //Список данных, которые успели получить до того, как сервис отключился
    List<ClidFullAddress> clidFullAddressList = new ArrayList<>();

    public RestClientExeptionWithResults(String msg, List<ClidFullAddress> clidFullAddresses) {
        super(msg);
        this.clidFullAddressList = clidFullAddresses;
    }

    public List<ClidFullAddress> getClidFullAddressList() {
        return clidFullAddressList;
    }
}

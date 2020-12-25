package elena.kozlova.orange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elena.kozlova.orange.repository.OrangeRepository;
import elena.kozlova.orange.exceptions.RestClientExeptionWithResults;
import elena.kozlova.orange.entity.Address;
import elena.kozlova.orange.entity.ClidFullAddress;
import elena.kozlova.orange.entity.ClidAddrId;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервисный слой
 */
@Service
@Transactional
@AllArgsConstructor
public class OrangeService {

    @Autowired
    private final OrangeRepository orangeRepository;

    @Autowired
    private final RestTemplate restTemplate;

    /**
     * Возвращает список пар телефон-адрес по заданному списку телефонов
     * @param clidList
     * @return список пар телефон-адрес
     * @throws RestClientException
     */
    public List<ClidFullAddress> getAddressesByList(List<String> clidList) throws RestClientExeptionWithResults {
        List<ClidAddrId> clidAddrIds = orangeRepository.findAllByClidList(clidList);
        return getFullAddress(clidAddrIds);
    }

    /**
     * Возвращает все пары телефон-адрес
     * @param pageable настройки пагинации. По умолчанию страница = 0, размер страницы = 100
     * @return страницу с парами телефон-адрес
     * @throws RestClientException
     */
    public Page<ClidFullAddress> getAll(Pageable pageable) throws RestClientExeptionWithResults {
        Page<ClidAddrId> clidAddrIds = orangeRepository.findAll(pageable);
        List<ClidFullAddress> clidFullAddresses = getFullAddress(clidAddrIds.toList());
        return new PageImpl<>(clidFullAddresses, pageable, clidFullAddresses.size());
    }

    /**
     * Возвращает список пар телефон-адрес для указанного списка пар телефон - идентификатор адреса
     * @param addrIds список пар телефон - идентификатор адреса
     * @return список пар телефон-адрес
     * @throws RestClientException
     */
    public List<ClidFullAddress> getFullAddress(List<ClidAddrId> addrIds) throws RestClientExeptionWithResults {
        List<ClidFullAddress> clidFullAddresses = new ArrayList<>();
        addrIds.forEach(ca -> {
            try {
                String fullAddress = (ca.getAddrId() == null) ? null : getAddressById(ca.getAddrId());
                clidFullAddresses.add(new ClidFullAddress(ca.getClid(), fullAddress));
            } catch (RestClientException e){
                throw new RestClientExeptionWithResults(e.getMessage(), clidFullAddresses);
            }
        });
        return clidFullAddresses;
    }

    /**
     * Получение строки адреса по id из внешнего Rest сервиса
     * @param addrId идентификатор строки с адресом
     * @return сторока с адресом. Если адрес не найден, возвращает null
     * @throws RestClientException
     */
    public String getAddressById(BigDecimal addrId) throws RestClientExeptionWithResults {
        String url = "http://{some.url}/addr/getaddrdata/"+addrId;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            Address address = objectMapper.readValue(response.getBody(), Address.class);
            return address.getFullAddress();
        } catch (HttpClientErrorException.NotFound exception) {
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

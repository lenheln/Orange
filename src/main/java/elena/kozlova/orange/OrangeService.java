package elena.kozlova.orange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elena.kozlova.orange.entity.Address;
import elena.kozlova.orange.entity.ClidFullAddress;
import elena.kozlova.orange.entity.ClidAddrId;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class OrangeService {

    @Autowired
    private final OrangeRepository orangeRepository;

    @Autowired
    private final RestTemplate restTemplate;

    public ClidFullAddress getAddressByClid(String clid)
            throws JsonProcessingException, RestClientException {

        ClidAddrId clidAddrId = orangeRepository.findByClid(clid).orElse(null);
        if(clidAddrId == null){
            return new ClidFullAddress();
        } else {
            String fullAddress = getAddressById(clidAddrId.getAddrId());
            return new ClidFullAddress(clid, fullAddress);
        }
    }

    /**
     * Получение списка адресов по списку номеров телефонов
     * @param clidList список clid
     * @return список адресов
     * @throws JsonProcessingException
     */
    public List<ClidFullAddress> getAddressesByList(List<String> clidList)
            throws JsonProcessingException, RestClientException {
        List<ClidAddrId> clidAddrIds = orangeRepository.findAllByClidList(clidList);
        return getFullAddrForList(clidAddrIds);
    }

    public Page<ClidFullAddress> getAll(Pageable pageable)
            throws JsonProcessingException, RestClientException {
        Page<ClidAddrId> clidAddrIds = orangeRepository.findAll(pageable);
        List<ClidFullAddress> clidFullAddresses = getFullAddrForList(clidAddrIds.toList());
        return new PageImpl<>(clidFullAddresses, pageable, clidFullAddresses.size());
    }

    public List<ClidFullAddress> getFullAddrForList(List<ClidAddrId> addrIds)
            throws JsonProcessingException, RestClientException {
        List<ClidFullAddress> clidFullAddresses = new ArrayList<>();
        for (ClidAddrId ca: addrIds) {
            String fullAddress = getAddressById(ca.getAddrId());
            ClidFullAddress clidFullAddress = new ClidFullAddress(ca.getClid(), fullAddress);
            clidFullAddresses.add(clidFullAddress);
        }
        return clidFullAddresses;
    }

    /**
     * Получение строки адреса по id из внешнего Rest сервиса
     * @param addrId - идентификатор строки с адресом
     * @return строку с адресом
     * @throws JsonProcessingException
     */
    public String getAddressById(BigDecimal addrId) throws JsonProcessingException, RestClientException {
        String url = "http://localhost:9090/addr/getaddrdata/"+addrId;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            Address address = objectMapper.readValue(response.getBody(), Address.class);
            return address.getFullAddress();
        } catch (HttpClientErrorException.NotFound exception) {
            return null;
        }
    }
}

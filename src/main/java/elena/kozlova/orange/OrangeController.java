package elena.kozlova.orange;

import com.fasterxml.jackson.core.JsonProcessingException;
import elena.kozlova.orange.entity.ClidFullAddress;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Главный контроллер
 */

@RestController
@Validated
@RequestMapping("/orange")
@AllArgsConstructor
public class OrangeController {

    @Autowired
    private final OrangeService orangeService;

    private final static Logger logger = LoggerFactory.getLogger(OrangeController.class);

    /**
     * Возвращает пару телефон-адрес для данного телефона
     * @param clid номер телефона
     * @param request запрос
     * @return пара телефон-адрес
     * @throws JsonProcessingException
     * @throws RestClientException
     */
    @GetMapping
    public ClidFullAddress getAddress(
            @RequestParam(value = "clid")
            @Pattern(regexp = "^([0-9]{10})*$", message = "CLID must contains only 10 numbers")
            String clid, ServletWebRequest request)
            throws JsonProcessingException, RestClientException {

                logger.info("URI: " + request.getRequest().getRequestURI());
                logger.info("params: " + request.getRequest().getQueryString());

                ClidFullAddress clidFullAddress =  orangeService.getAddressByClid(clid);

                logger.info("response: " + clidFullAddress.toString());
                return clidFullAddress;
    }

    /**
     * Получить список адресов по списку телефонов
     * @param clidList список телефонов
     * @return List<String> список адресов
     * @throws JsonProcessingException
     */
    @GetMapping("/addresses")
    public List<ClidFullAddress> getAddresses(
            @RequestParam(value="clidList")
            @NotEmpty(message = "Input CLID list cannot be empty")
            List< @Pattern(regexp = "^([0-9]{10})*$", message = "CLID must contains only 10 numbers")
                    String> clidList, ServletWebRequest request)
            throws JsonProcessingException, RestClientException {

                 logger.info("URI: " + request.getRequest().getRequestURI());
                 logger.info("params: " + request.getRequest().getQueryString());

                 List<ClidFullAddress> clidFullAddresses = orangeService.getAddressesByList(clidList);

                 logger.info("response: " + clidFullAddresses.toString());
                 return clidFullAddresses;

    }

    /**
     * Запрос всех адресов и телефонов
     * @param pageable настройки пагинации. По умолчанию страница = 0, размер страницы = 100
     * @return страницу с адресами и телефонами
     * @throws JsonProcessingException
     */
    @GetMapping("/all")
    public Page<ClidFullAddress> getAll(
            @PageableDefault(size = 100)
            Pageable pageable, ServletWebRequest request)
            throws JsonProcessingException, RestClientException {

                logger.info("URI: " + request.getRequest().getRequestURI());
                logger.info("params: " + request.getRequest().getQueryString());

                Page<ClidFullAddress> clidFullAddresses = orangeService.getAll(pageable);

                logger.info("response: " + clidFullAddresses.toList().toString());
                return clidFullAddresses;
    }


    /**
     * Обработчик ошибок валидации полей
     *
     * @param ex исключение
     * @return исключение в тестовом виде
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handleValidationExceptions(ConstraintViolationException ex, ServletWebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", new Date());
        errors.put("path", request.getRequest().getRequestURI());
        errors.put("params", request.getRequest().getQueryString());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errors.put("message", ex.getMessage());
        logger.warn(errors.toString());
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RestClientException.class)
    public Map<String, Object> handleValidationExceptions(RestClientException ex, ServletWebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", new Date());
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errors.put("message", "External rest servise doesn't answer");
        logger.error(errors.toString());
        return errors;
    }
}

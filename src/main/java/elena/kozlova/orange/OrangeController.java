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
     * Возвращает список пар телефон-адрес по заданному списку телефонов
     * @param clidList список телефонов
     * @param request
     * @return список пар телефон-адрес
     * @throws RestClientException
     */
    @GetMapping()
    public List<ClidFullAddress> getAddresses(
            @RequestParam(value="clidList")
            @NotEmpty(message = "Input CLID list cannot be empty")
            List< @Pattern(regexp = "^([0-9]{10})*$", message = "CLID must contains only 10 numbers")
                    String> clidList, ServletWebRequest request)
            throws RestClientException {

                 logger.info("URI: " + request.getRequest().getRequestURI());
                 logger.info("params: " + request.getRequest().getQueryString());

                 List<ClidFullAddress> clidFullAddresses = orangeService.getAddressesByList(clidList);

                 logger.info("response: " + clidFullAddresses.toString());
                 return clidFullAddresses;

    }

    /**
     * Возвращает все пары телефон-адрес
     * @param pageable настройки пагинации. По умолчанию страница = 0, размер страницы = 100
     * @param request
     * @return страница с парами телефон-адрес
     * @throws RestClientException
     */
    @GetMapping("/all")
    public Page<ClidFullAddress> getAll(
            @PageableDefault(size = 100)
            Pageable pageable, ServletWebRequest request) throws RestClientException {

                logger.info("URI: " + request.getRequest().getRequestURI());
                logger.info("params: " + request.getRequest().getQueryString());

                Page<ClidFullAddress> clidFullAddresses = orangeService.getAll(pageable);

                logger.info("response: " + clidFullAddresses.toList().toString());
                return clidFullAddresses;
    }

    /**
     * Обработчик ошибок валидации полей
     * @param ex
     * @param request
     * @return подробная информация об ошибке
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

    /**
     * Обработчик ошибок внешнего rest-сервиса RestClientException
     * @param ex
     * @param request
     * @return подробная информация об ошибке
     */
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

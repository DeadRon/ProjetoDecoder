package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import com.ead.authuser.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UtilsService utilsService;

    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable){
        List<CourseDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;
        String url = utilsService.createdURL(userId, pageable);

        log.debug("Request URL: {}", url);
        log.debug("Request URL: {}", url);

        try {
            /*ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>
            informa ao RestTemplate o tipo de objeto que deve ser desserializado na resposta.
            */
            ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType = new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>() {};

            /*retorna um objeto ResponseEntity
            url: A URL do endpoint da API a ser chamada.
            HttpMethod.GET: O método HTTP para esta chamada, que é GET.
            null: O objeto HttpEntity que representa o corpo da requisição e cabeçalhos HTTP.
            Neste caso, é passado null, pois a chamada GET geralmente não possui corpo.
            responseType: Um objeto ParameterizedTypeReference que define o tipo esperado na resposta.
            Neste caso, espera-se um objeto ResponsePageDTO<CourseDTO>*/
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();

            log.debug("Response Number of Elements: {}", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses {}", e);
        }
        log.info("Ending request /courses userId {}", userId);
        return result.getBody();
    }

}
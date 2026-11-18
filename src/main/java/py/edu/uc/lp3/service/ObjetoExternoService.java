package py.edu.uc.lp3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import py.edu.uc.lp3.rest.dto.ObjetoExternoDTO;

@Service
public class ObjetoExternoService {

    private final WebClient webClient;

    public ObjetoExternoService(@Qualifier("apiExternaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public List<ObjetoExternoDTO> listar() {
        return webClient.get()
                .uri("/objects")
                .retrieve()
                .bodyToFlux(ObjetoExternoDTO.class)
                .collectList()
                .block();
    }

    public ObjetoExternoDTO buscar(String id) {
        return webClient.get()
                .uri("/objects/{id}", id)
                .retrieve()
                .bodyToMono(ObjetoExternoDTO.class)
                .block();
    }
}

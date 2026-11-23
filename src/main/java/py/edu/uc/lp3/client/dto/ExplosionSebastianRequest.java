package py.edu.uc.lp3.client.dto;

public record ExplosionSebastianRequest(Long creeperId,
                                        Long jugadorId,
                                        Long zombieId,
                                        Integer radio) {
}

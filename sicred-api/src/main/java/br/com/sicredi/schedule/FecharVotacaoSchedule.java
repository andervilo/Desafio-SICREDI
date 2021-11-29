package br.com.sicredi.schedule;

import br.com.sicredi.services.PautaService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class FecharVotacaoSchedule {

    private PautaService pautaService;

    public FecharVotacaoSchedule(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @Scheduled(fixedDelay = 60000)
    public void fecharVotacoesDePautas(){
        pautaService.fecharVotacoesDePautas();
    }
}

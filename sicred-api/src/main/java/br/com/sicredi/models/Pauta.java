package br.com.sicredi.models;

import br.com.sicredi.converter.LocalDateTimeDeserializer;
import br.com.sicredi.converter.LocalDateTimeSerializer;
import br.com.sicredi.enums.StatusVotacao;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
public class Pauta {

	@Id
	private String id;
	
	private String descricaoPauta;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime dataHoraAbeturaVotacao;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime dataHoraFechamentoVotacao;
	
	private Long totalVotosFavoraveis;
	
	private Long totalVotosContrarios;

	private StatusVotacao statusVotacao;

	private Set<Associado> listaVotantes = new HashSet<>();

	public Pauta() {
		this.totalVotosContrarios = 0L;
		this.totalVotosFavoraveis = 0L;
	}

	public Pauta(String descricaoPauta) {
		this.descricaoPauta = descricaoPauta;
		this.statusVotacao = StatusVotacao.NAO_INICIADA;
		this.dataHoraAbeturaVotacao = null;
		this.dataHoraFechamentoVotacao = null;
		this.totalVotosContrarios = 0L;
		this.totalVotosFavoraveis = 0L;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricaoPauta() {
		return descricaoPauta;
	}

	public LocalDateTime getDataHoraAbeturaVotacao() {
		return dataHoraAbeturaVotacao;
	}

	public LocalDateTime getDataHoraFechamentoVotacao() {
		return dataHoraFechamentoVotacao;
	}

	public Long getTotalVotosFavoraveis() {
		return totalVotosFavoraveis;
	}

	public Long getTotalVotosContrarios() {
		return totalVotosContrarios;
	}

	public void setDescricaoPauta(String descricaoPauta) {
		this.descricaoPauta = descricaoPauta;
	}

	public void setDataHoraAbeturaVotacao(LocalDateTime dataHoraAbeturaVotacao) {
		this.dataHoraAbeturaVotacao = dataHoraAbeturaVotacao;
	}

	public void setDataHoraFechamentoVotacao(LocalDateTime dataHoraFechamentoVotacao) {
		this.dataHoraFechamentoVotacao = dataHoraFechamentoVotacao;
	}

	public void  addVotoFavoravel() {
		this.totalVotosFavoraveis += 1;
	}

	public void addVotoContrario() {
		this.totalVotosContrarios += 1;
	}
	
	public void addVotante(Associado votante) {
		this.listaVotantes.add(votante);
	}
	
	public void removeVotante(Associado votante) {
		if(this.listaVotantes.contains(votante)) {
			this.listaVotantes.remove(votante);
		}		
	}
	
	public Set<Associado> getListaVotantes() {
		return listaVotantes;
	}

	public StatusVotacao getStatusVotacao() {
		return statusVotacao;
	}

	public void setStatusVotacao(StatusVotacao statusVotacao) {
		this.statusVotacao = statusVotacao;
	}
}

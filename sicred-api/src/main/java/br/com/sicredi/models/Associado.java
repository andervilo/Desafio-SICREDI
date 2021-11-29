package br.com.sicredi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Associado {

	@Id
	private String id;

	private String nome;

	private String cpf;

	public Associado(String nome, String cpf) {
		this.nome = nome;
		this.cpf = cpf;
	}

	public Associado() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Associado associado = (Associado) o;

		if (id != null ? !id.equals(associado.id) : associado.id != null) return false;
		if (nome != null ? !nome.equals(associado.nome) : associado.nome != null) return false;
		return cpf != null ? cpf.equals(associado.cpf) : associado.cpf == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (nome != null ? nome.hashCode() : 0);
		result = 31 * result + (cpf != null ? cpf.hashCode() : 0);
		return result;
	}
}

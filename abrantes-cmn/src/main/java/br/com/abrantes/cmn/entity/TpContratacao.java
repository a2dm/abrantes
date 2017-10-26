package br.com.abrantes.cmn.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * @author Mateus Bastos
 * @since 26/01/2016
 */
@Entity
@Table(name = "tb_tp_contratacao", schema = "seg")
@Proxy(lazy = true)
public class TpContratacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_tp_contratacao")
	private BigInteger idTpContratacao;

	@Column(name = "des_tp_contratacao")
	private String desTpContratacao;

	public BigInteger getIdTpContratacao() {
		return idTpContratacao;
	}

	public void setIdTpContratacao(BigInteger idTpContratacao) {
		this.idTpContratacao = idTpContratacao;
	}

	public String getDesTpContratacao() {
		return desTpContratacao;
	}

	public void setDesTpContratacao(String desTpContratacao) {
		this.desTpContratacao = desTpContratacao;
	}

}

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
@Table(name = "tb_tp_audiencia", schema = "seg")
@Proxy(lazy = true)
public class TpAudiencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_tp_audiencia")
	private BigInteger idTpAudiencia;

	@Column(name = "des_tp_audiencia")
	private String desTpAudiencia;

	public BigInteger getIdTpAudiencia() {
		return idTpAudiencia;
	}

	public void setIdTpAudiencia(BigInteger idTpAudiencia) {
		this.idTpAudiencia = idTpAudiencia;
	}

	public String getDesTpAudiencia() {
		return desTpAudiencia;
	}

	public void setDesTpAudiencia(String desTpAudiencia) {
		this.desTpAudiencia = desTpAudiencia;
	}

}

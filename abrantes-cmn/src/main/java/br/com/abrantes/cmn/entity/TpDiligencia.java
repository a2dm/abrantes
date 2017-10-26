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
@Table(name = "tb_tp_diligencia", schema = "seg")
@Proxy(lazy = true)
public class TpDiligencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_tp_diligencia")
	private BigInteger idTpDiligencia;

	@Column(name = "des_tp_diligencia")
	private String desTpDiligencia;

	public BigInteger getIdTpDiligencia() {
		return idTpDiligencia;
	}

	public void setIdTpDiligencia(BigInteger idTpDiligencia) {
		this.idTpDiligencia = idTpDiligencia;
	}

	public String getDesTpDiligencia() {
		return desTpDiligencia;
	}

	public void setDesTpDiligencia(String desTpDiligencia) {
		this.desTpDiligencia = desTpDiligencia;
	}

}

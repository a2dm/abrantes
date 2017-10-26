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
@Table(name = "tb_origem_processo", schema = "seg")
@Proxy(lazy = true)
public class OrigemProcesso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_origem_processo")
	private BigInteger idOrigemProcesso;

	@Column(name = "des_origem_processo")
	private String desOrigemProcesso;

	public BigInteger getIdOrigemProcesso() {
		return idOrigemProcesso;
	}

	public void setIdOrigemProcesso(BigInteger idOrigemProcesso) {
		this.idOrigemProcesso = idOrigemProcesso;
	}

	public String getDesOrigemProcesso() {
		return desOrigemProcesso;
	}

	public void setDesOrigemProcesso(String desOrigemProcesso) {
		this.desOrigemProcesso = desOrigemProcesso;
	}

}

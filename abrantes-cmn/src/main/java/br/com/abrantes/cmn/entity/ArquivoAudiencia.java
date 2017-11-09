package br.com.abrantes.cmn.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "tb_arquivo_audiencia", schema = "seg")
@Proxy(lazy = true)
public class ArquivoAudiencia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SQ_ARQUIVO_AUDIENCIA", sequenceName = "SQ_ARQUIVO_AUDIENCIA", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ARQUIVO_AUDIENCIA")
	@Column(name = "id_arquivo_audiencia")
	private Integer idArquivoAudiencia;

	@Column(name = "nome")
	private String nome;

	@Column(name = "caminho")
	private String caminho;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "tamanho")
	private float tamanho;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_audiencia", insertable = false, updatable = false)
	private Audiencia audiencia;

	@Column(name = "id_audiencia")
	private BigInteger idAudiencia;

	@Column(name = "id_usuario_cad")
	private BigInteger idUsuarioCad;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_cad", insertable = false, updatable = false)
	private Usuario usuarioCad;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_alteracao")
	private Date datAlteracao;

	@Column(name = "id_usuario_alt")
	private BigInteger idUsuarioAlt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_alt", insertable = false, updatable = false)
	private Usuario usuarioAlt;

	@Column(name = "flg_ativo")
	private String flgAtivo;

	public Integer getIdArquivoAudiencia() {
		return idArquivoAudiencia;
	}

	public void setIdArquivoAudiencia(Integer idArquivoAudiencia) {
		this.idArquivoAudiencia = idArquivoAudiencia;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public float getTamanho() {
		return tamanho;
	}

	public void setTamanho(float tamanho) {
		this.tamanho = tamanho;
	}

	public Audiencia getAudiencia() {
		return audiencia;
	}

	public void setAudiencia(Audiencia audiencia) {
		this.audiencia = audiencia;
	}

	public BigInteger getIdAudiencia() {
		return idAudiencia;
	}

	public void setIdAudiencia(BigInteger idAudiencia) {
		this.idAudiencia = idAudiencia;
	}

	public BigInteger getIdUsuarioCad() {
		return idUsuarioCad;
	}

	public void setIdUsuarioCad(BigInteger idUsuarioCad) {
		this.idUsuarioCad = idUsuarioCad;
	}

	public Usuario getUsuarioCad() {
		return usuarioCad;
	}

	public void setUsuarioCad(Usuario usuarioCad) {
		this.usuarioCad = usuarioCad;
	}

	public Date getDatAlteracao() {
		return datAlteracao;
	}

	public void setDatAlteracao(Date datAlteracao) {
		this.datAlteracao = datAlteracao;
	}

	public BigInteger getIdUsuarioAlt() {
		return idUsuarioAlt;
	}

	public void setIdUsuarioAlt(BigInteger idUsuarioAlt) {
		this.idUsuarioAlt = idUsuarioAlt;
	}

	public Usuario getUsuarioAlt() {
		return usuarioAlt;
	}

	public void setUsuarioAlt(Usuario usuarioAlt) {
		this.usuarioAlt = usuarioAlt;
	}

	public String getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(String flgAtivo) {
		this.flgAtivo = flgAtivo;
	}
	
	

}
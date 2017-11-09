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
@Table(name = "tb_arquivo_diligencia", schema = "seg")
@Proxy(lazy = true)
public class ArquivoDiligencia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SQ_ARQUIVO_DILIGENCIA", sequenceName = "SQ_ARQUIVO_DILIGENCIA", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ARQUIVO_DILIGENCIA")
	@Column(name = "id_arquivo_diligencia")
	private Integer idArquivoDiligencia;

	@Column(name = "nome")
	private String nome;

	@Column(name = "caminho")
	private String caminho;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "tamanho")
	private float tamanho;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_diligencia", insertable = false, updatable = false)
	private Diligencia diligencia;

	@Column(name = "id_diligencia")
	private BigInteger idDiligencia;

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

	public Integer getIdArquivoDiligencia() {
		return idArquivoDiligencia;
	}

	public void setIdArquivoDiligencia(Integer idArquivoDiligencia) {
		this.idArquivoDiligencia = idArquivoDiligencia;
	}

	public Diligencia getDiligencia() {
		return diligencia;
	}

	public void setDiligencia(Diligencia diligencia) {
		this.diligencia = diligencia;
	}

	public BigInteger getIdDiligencia() {
		return idDiligencia;
	}

	public void setIdDiligencia(BigInteger idDiligencia) {
		this.idDiligencia = idDiligencia;
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
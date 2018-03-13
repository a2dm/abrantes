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
import javax.persistence.Transient;
import javax.servlet.http.Part;

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

	@Column(name = "des_arquivo")
	private String desArquivo;
	
	@Column(name = "nome")
	private String nome;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "tamanho")
	private Long tamanho;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_diligencia", insertable = false, updatable = false)
	private Diligencia diligencia;

	@Column(name = "id_audiencia")
	private BigInteger idAudiencia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;
	
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
	
	@Transient
	private Part file;

	public Integer getIdArquivoDiligencia() {
		return idArquivoDiligencia;
	}

	public void setIdArquivoDiligencia(Integer idArquivoDiligencia) {
		this.idArquivoDiligencia = idArquivoDiligencia;
	}

	public String getDesArquivo() {
		return desArquivo;
	}

	public void setDesArquivo(String desArquivo) {
		this.desArquivo = desArquivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getTamanho() {
		return tamanho;
	}

	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}

	public Diligencia getDiligencia() {
		return diligencia;
	}

	public void setDiligencia(Diligencia diligencia) {
		this.diligencia = diligencia;
	}

	public BigInteger getIdAudiencia() {
		return idAudiencia;
	}

	public void setIdAudiencia(BigInteger idAudiencia) {
		this.idAudiencia = idAudiencia;
	}

	public Date getDatCadastro() {
		return datCadastro;
	}

	public void setDatCadastro(Date datCadastro) {
		this.datCadastro = datCadastro;
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

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}
	
}
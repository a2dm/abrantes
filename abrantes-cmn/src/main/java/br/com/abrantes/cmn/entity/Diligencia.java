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

/**
 * @author Mateus Bastos
 * @since 26/01/2016
 */
@Entity
@Table(name = "tb_diligencia", schema = "seg")
@Proxy(lazy = true)
public class Diligencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SQ_DILIGENCIA", sequenceName = "SQ_DILIGENCIA", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DILIGENCIA")
	@Column(name = "id_diligencia")
	private BigInteger idDiligencia;

	@Column(name = "id_ext")
	private BigInteger idExt;

	@Column(name = "dat_prazo_maximo")
	private Date datPrazoMaximo;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tp_diligencia", insertable = false, updatable = false)
	private TpDiligencia tpDiligencia;

	@Column(name = "id_tp_diligencia")
	private BigInteger idTpDiligencia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_origem_processo", insertable = false, updatable = false)
	private OrigemProcesso origemProcesso;

	@Column(name = "id_origem_processo")
	private BigInteger idOrigemProcesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tp_contratacao", insertable = false, updatable = false)
	private TpContratacao tpContratacao;

	@Column(name = "id_tp_contratacao")
	private BigInteger idTpContratacao;

	@Column(name = "vara")
	private String vara;

	@Column(name = "cidade")
	private String cidade;

	@Column(name = "requerido")
	private String requerido;

	@Column(name = "requerente")
	private String requerente;

	@Column(name = "contratante")
	private String contratante;

	@Column(name = "contratado")
	private String contratado;

	@Column(name = "valor_recebido")
	private Double valorRecebido;

	@Column(name = "valor_contratado")
	private Double valorContratado;

	@Column(name = "processo")
	private String processo;

	@Column(name = "flg_ativo")
	private String flgAtivo;

	public BigInteger getIdDiligencia() {
		return idDiligencia;
	}

	public void setIdDiligencia(BigInteger idDiligencia) {
		this.idDiligencia = idDiligencia;
	}

	public BigInteger getIdExt() {
		return idExt;
	}

	public void setIdExt(BigInteger idExt) {
		this.idExt = idExt;
	}

	public Date getDatPrazoMaximo() {
		return datPrazoMaximo;
	}

	public void setDatPrazoMaximo(Date datPrazoMaximo) {
		this.datPrazoMaximo = datPrazoMaximo;
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

	public TpDiligencia getTpDiligencia() {
		return tpDiligencia;
	}

	public void setTpDiligencia(TpDiligencia tpDiligencia) {
		this.tpDiligencia = tpDiligencia;
	}

	public BigInteger getIdTpDiligencia() {
		return idTpDiligencia;
	}

	public void setIdTpDiligencia(BigInteger idTpDiligencia) {
		this.idTpDiligencia = idTpDiligencia;
	}

	public OrigemProcesso getOrigemProcesso() {
		return origemProcesso;
	}

	public void setOrigemProcesso(OrigemProcesso origemProcesso) {
		this.origemProcesso = origemProcesso;
	}

	public BigInteger getIdOrigemProcesso() {
		return idOrigemProcesso;
	}

	public void setIdOrigemProcesso(BigInteger idOrigemProcesso) {
		this.idOrigemProcesso = idOrigemProcesso;
	}

	public TpContratacao getTpContratacao() {
		return tpContratacao;
	}

	public void setTpContratacao(TpContratacao tpContratacao) {
		this.tpContratacao = tpContratacao;
	}

	public BigInteger getIdTpContratacao() {
		return idTpContratacao;
	}

	public void setIdTpContratacao(BigInteger idTpContratacao) {
		this.idTpContratacao = idTpContratacao;
	}

	public String getVara() {
		return vara;
	}

	public void setVara(String vara) {
		this.vara = vara;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getRequerido() {
		return requerido;
	}

	public void setRequerido(String requerido) {
		this.requerido = requerido;
	}

	public String getRequerente() {
		return requerente;
	}

	public void setRequerente(String requerente) {
		this.requerente = requerente;
	}

	public String getContratante() {
		return contratante;
	}

	public void setContratante(String contratante) {
		this.contratante = contratante;
	}

	public String getContratado() {
		return contratado;
	}

	public void setContratado(String contratado) {
		this.contratado = contratado;
	}

	public Double getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Double getValorContratado() {
		return valorContratado;
	}

	public void setValorContratado(Double valorContratado) {
		this.valorContratado = valorContratado;
	}

	public String getProcesso() {
		return processo;
	}

	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public String getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(String flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

}

package br.gov.serpro.vertxapp.model;

public class Bookmark {

	private Integer id;

	private String nome;

	private String url;

	private String _x = "X";

	public String getX() {
		return this._x;
	}

	public Bookmark() {
	}

	public Bookmark(Integer id, String nome, String url) {
		this.id = id;
		this.nome = nome;
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

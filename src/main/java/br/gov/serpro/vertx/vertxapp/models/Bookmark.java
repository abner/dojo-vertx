package br.gov.serpro.vertx.vertxapp.models;

public class Bookmark {

	private Integer id;

	private String nome;

	private String url;

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

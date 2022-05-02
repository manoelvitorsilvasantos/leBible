package br.app.mvictor.modelo;
import android.text.*;

public class Descricao
{
	private int id;
	private int capitulo;
	private String texto;
	private int versiculo;
	private String nome;
	
	public Descricao(){
		
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setCapitulo(int capitulo)
	{
		this.capitulo = capitulo;
	}

	public int getCapitulo()
	{
		return capitulo;
	}

	public void setTexto(String texto)
	{
		this.texto = texto;
	}

	public String getTexto()
	{
		return texto;
	}

	public void setVersiculo(int versiculo)
	{
		this.versiculo = versiculo;
	}

	public int getVersiculo()
	{
		return versiculo;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getNome()
	{
		return nome;
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("[").append(nome)
		.append("]").append("\n")
		.append(capitulo).append(":").append(versiculo).append(".")
		.append("\n").append(Html.fromHtml(texto));	
		// TODO: Implement this method
		return str.toString();
	}
	
	
	
}

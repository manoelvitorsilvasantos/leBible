package br.app.mvictor.modelo;

import android.text.*;
import br.lib.mvictor.utils.*;

public class Versiculo
{
	private int id;
	private int capitulo;
	private String texto;
	private int versiculo;
	private String nome;

	public Versiculo(){

	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getNome()
	{
		return nome;
	}

	public void setCapitulo(int capitulo)
	{
		this.capitulo = capitulo;
	}

	public int getCapitulo()
	{
		return capitulo;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
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

	

	@Override
	public String toString(){
		String formatado = null;
		formatado = String.valueOf(versiculo);
		formatado = U.getInstance().superScript(formatado);
		return String.valueOf(formatado+" "+Html.fromHtml(texto));
	}
	
	
}

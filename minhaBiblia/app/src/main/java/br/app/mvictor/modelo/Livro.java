package br.app.mvictor.modelo;

public class Livro
{
	
	private int id;
	private String titulo;
	
    public Livro(){
		
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setTitulo(String titulo)
	{
		this.titulo = titulo;
	}

	public String getTitulo()
	{
		return titulo;
	}
	
	@Override
	public String toString(){
		return titulo;
	}
	
}

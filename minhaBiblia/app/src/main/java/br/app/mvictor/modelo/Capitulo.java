package br.app.mvictor.modelo;

public class Capitulo
{
	private int id;
	private int capitulo;
    
	public Capitulo(){
		
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
	
	@Override
	public String toString(){
		return String.valueOf(capitulo);
	}
	

}

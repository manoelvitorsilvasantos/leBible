package br.app.mvictor;
import android.os.*;
import android.widget.*;
import br.app.mvictor.modelo.*;
import br.lib.mvictor.utils.*;
import java.util.*;
import br.app.mvictor.dao.*;
import android.widget.AdapterView.*;
import android.view.*;
import android.content.*;
import java.io.*;
import android.database.sqlite.*;
import android.support.v7.app.*;


public class SearchActivity extends Primordial implements ListView.OnItemClickListener
{



	private ListView lista;
	private ArrayAdapter<Descricao> meuAdapter;
	private List<Descricao> minhaLista = new ArrayList<Descricao>();
	private String nome = null;
	private String capitulo = null;
	private String livro = null;
	private String keyword = null;
	private BancoDeDados mBancoDeDados;

	public void setKeyword(String keyword){this.keyword = keyword;}
	public String getKeyword(){return keyword;}
	public void setNome(String nome){this.nome = nome;}
	public String getNome(){return nome;}
	public void setCapitulo(String capitulo){this.capitulo = capitulo;}
	public String getCapitulo(){return capitulo;}
	public void setLivro(String livro){this.livro = livro;}
	public String getLivro(){return livro;}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		inicializarVariaveis();
		
		Toast.makeText(this, "Pagina busca", 1000).show();
		
		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null){
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setTitle(getKeyword());
		}
		
		inicializarComponentes();
		povoarLista();
	}
	
	
	private void inicializarComponentes(){
		lista = (ListView) findViewById(R.id.lvPessoa);
	}
	
	private void inicializarVariaveis(){
		Bundle extras = getIntent().getExtras();
		setNome(extras.getString("nome"));
		setCapitulo(extras.getString("capitulo"));
		setLivro(extras.getString("livro"));
		setKeyword(extras.getString("keyword"));
	}
	
	public String getId(String str){
		mBancoDeDados = new BancoDeDados(this);
		return String.valueOf(mBancoDeDados.getIdLivro(str));
	}
	
	
	private void povoarLista(){
		mBancoDeDados = new BancoDeDados(this);
		minhaLista.clear();
		minhaLista = mBancoDeDados.getVerse(getKeyword());
		meuAdapter = new ArrayAdapter<Descricao>(this, R.layout.item_list2, minhaLista);
		lista.setAdapter(meuAdapter);
		lista.setOnItemClickListener(this);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id)
	{
		// TODO: Implement this method
		// TODO: Implement this method
		String livroNome = null;
		String livroNumero = null;
		String livroCapitulo = null;


		livroNome = Script.getInstance().subString(adapterView.getItemAtPosition(posicao), "[", "]", 1);
		livroCapitulo = Script.getInstance().subString(adapterView.getItemAtPosition(posicao), "]", ":", 1);
		try{
			livroNumero = getId(livroNome);
			//alerta("Nome"+livroNome+" - "+livroNumero+"\n"+livroCapitulo);

			Intent tela = new Intent(SearchActivity.this, ConteudoActivity.class);
			tela.putExtra("nome", livroNome);
			tela.putExtra("livro", livroNumero);
			tela.putExtra("capitulo", livroCapitulo);
			startActivity(tela);

		}
		catch(SQLiteException ex){
			alert(this,"Error na query", 500);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()){
			case android.R.id.home:
				startActivity(new Intent(this, MainActivity.class));
				break;
			default:break;
		}
		return true;
	}
	
	
}

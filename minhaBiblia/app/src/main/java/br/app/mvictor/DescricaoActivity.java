package br.app.mvictor;

import android.support.v7.app.*;
import android.os.*;
import android.widget.*;
import br.app.mvictor.modelo.*;
import java.util.*;
import android.view.*;
import br.app.mvictor.dao.*;
import android.database.sqlite.*;
import android.speech.tts.*;
import android.content.*;
import android.preference.*;
import br.lib.mvictor.utils.*;
import br.app.mvictor.Utils.*;
import android.support.v4.view.*;

public class DescricaoActivity extends Primordial implements ListView.OnItemClickListener, ListView.OnItemLongClickListener, Setting

{

	@Override
	public void falar(String conteudo)
	{
		// TODO: Implement this method
	}

	@Override
	public void initSpeak(Context context)
	{
		// TODO: Implement this method
	}


	private ListView listView;
	private List<Descricao>minhaLista;
	private ArrayAdapter<Descricao>meuAdapter;
	private BancoDeDados minhaData;
	private String capitulo;
	private String livro;
	private String nome;
	private String keyword;
	private boolean theme;
	private float rate;
	private float pitch;
	private Locale locale;
	private ActionBar action;

	private TextToSpeech ts;

	private void setCapitulo(final String capitulo){this.capitulo = capitulo;}
	private void setLivro(final String livro){this.livro = livro;}
	private void setNome(final String nome){this.nome = nome;}
	private void setPitch(final float pitch){this.pitch = pitch;}
	private void setRate(final float rate){this.rate = rate;}
	private void setLocale(final Locale locale){this.locale = locale;}
	private void setKeyword(final String keyword){this.keyword = keyword;}

	private String getCapitulo(){return capitulo;}
	private String getLivro(){ return livro;}
	private String getNome(){return nome;}
	private float getPitch(){return pitch;}
	private float getRate(){return rate;}
	private Locale getLocale(){return locale;}
	private String getKeyword(){return keyword;}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		listView = new ListView(this);
		config(this);
		action = getSupportActionBar();
		
		if(action!=null){
			action.setDisplayHomeAsUpEnabled(true);
			action.setHomeButtonEnabled(true);
			action.setTitle(getKeyword());
		}
		
		try{
			minhaData = new BancoDeDados(this);
			minhaLista = minhaData.getVerse(getKeyword());
			meuAdapter = new ArrayAdapter<Descricao>(this, R.layout.item_list2, minhaLista);
			listView.setAdapter(meuAdapter);
		}catch(SQLiteException e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		setContentView(listView);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()){
			case android.R.id.home:
				startActivity(new Intent(this, MainActivity.class));
				this.finishAffinity();
				break;
			default:break;
		} return true;
	}

	public String getIdLivro(String keyword){
		minhaData = new BancoDeDados(this);
		return String.valueOf(minhaData.getIdLivro(keyword));
	}
	
	public String getOnlyVerse(String idLivro, String capitulo, String verso){
		minhaData = new BancoDeDados(this);
		return minhaData.getOnlyVerse(idLivro, capitulo, verso);
	}

	public List<Descricao> getLista(){
		minhaData = new BancoDeDados(this);
		return minhaData.getVerse(getKeyword());
	}
	
	public String getNomeLivro(String keyword){
		minhaData = new BancoDeDados(this);
		return minhaData.getNomeLivro(keyword);
	}
	
	public String getCapitulo(String keyword){
		minhaData = new BancoDeDados(this);
		return minhaData.getCapitulo(keyword);
	}



	@Override
	public void config(Context contexto)
	{
		// TODO: Implement this method
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
		this.theme = prefs.getBoolean("checkBox", false);
		setRate(Float.parseFloat(prefs.getString("rate", "Default list prefs")));
		setPitch(Float.parseFloat(prefs.getString("pitch", "Default list prefs")));
		setLocale(new Locale(prefs.getString("ttsLang", "Default list prefs")));
		Bundle extras = getIntent().getExtras();
		setKeyword(extras.getString("keyword"));
	}



	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id)
	{
		//Toast.makeText(this, adapter.getItemAtPosition(posicao).toString(), Toast.LENGTH_SHORT).show();
		setCapitulo(Script.getInstance().subString(adapter.getItemAtPosition(posicao), "]", ":", 1));
		setNome(Script.getInstance().subString(adapter.getItemAtPosition(posicao), "[", "]",1));
		setLivro(getIdLivro(getNome()));
		String verso = Script.getInstance().subString(adapter.getItemAtPosition(posicao).toString(), ":", ".", 1);
		dialog(this, getNome()+":"+getCapitulo()+":"+verso, getOnlyVerse(getLivro(), getCapitulo(), verso) , "Fechar");
		//alerta(getLivro(), getNome(), getCapitulo());
		/*
		Intent i = new Intent(this, VerseActivity.class);
		i.putExtra("nome", getNome());
		i.putExtra("livro", getLivro());
		i.putExtra("capitulo", getCapitulo());
		startActivity(i);*/
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicao, long id)
	{
		String title = getNome()+":"+getCapitulo()+"\n";
		String verso = (U.getInstance().superScriptInvert(adapter.getItemAtPosition(posicao).toString()))+"\n\n";
		String end = "\n"+"LeBible";

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, (title+verso+end));
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Compartilhar versiculo"));
		// TODO: Implement this method
		return false;
	}
	
	

}

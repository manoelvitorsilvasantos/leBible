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

public class VerseActivity extends Primordial implements ListView.OnItemClickListener, ListView.OnItemLongClickListener, Setting
{
	private ListView listView;
	private List<Versiculo>minhaLista;
	private ArrayAdapter<Versiculo>meuAdapter;
	private BancoDeDados minhaData;
	private String capitulo;
	private String livro;
	private String nome;
	private boolean theme;
	private float rate;
	private float pitch;
	private Locale locale;
	private ActionBar action;
	
	private TextToSpeech ts;
	
	private void setCapitulo(String capitulo){this.capitulo = capitulo;}
	private void setLivro(String livro){this.livro = livro;}
	private void setNome(String nome){this.nome = nome;}
	private void setPitch(float pitch){this.pitch = pitch;}
	private void setRate(float rate){this.rate = rate;}
	private void setLocale(Locale locale){this.locale = locale;}
	
	private String getCapitulo(){return capitulo;}
	private String getLivro(){ return livro;}
	private String getNome(){return nome;}
	private float getPitch(){return pitch;}
	private float getRate(){return rate;}
	private Locale getLocale(){return locale;}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		config(this);
		listView = new ListView(this);
	
		ts = new TextToSpeech(this, new TextToSpeech.OnInitListener(){
				@Override
				public void onInit(int status)
				{
					if(status!=TextToSpeech.ERROR){
						ts.setLanguage(getLocale());
					}
				}
			});
		initSpeak(this);
		
		
		action = getSupportActionBar();
		if(action!=null){
			action.setDisplayHomeAsUpEnabled(true);
			action.setHomeButtonEnabled(true);
			action.setTitle((getNome()+":"+getCapitulo()));
		}
		
		try{
			minhaData = new BancoDeDados(this);
			minhaLista = minhaData.allVersiculo(getLivro(), getCapitulo());
			meuAdapter = new ArrayAdapter<Versiculo>(this, R.layout.item_list2, minhaLista);
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
		getMenuInflater().inflate(R.menu.verso, menu);
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
			case R.id.action_settings:
				startActivity(new Intent(this, Preference.class));
				break;
			case R.id.action_falar:
				falarCapitulo();
				break;
			case R.id.action_voltar:
				voltar();
				break;
			case R.id.action_proximo:
				proximo();
				break;
			default:break;
		} return true;
	}
	
	

	private void proximo()
	{
		// TODO: Implement this method
		if(Integer.valueOf(getCapitulo()) >= 1 && Integer.valueOf(getCapitulo()) < getLastChapter()){
			try{
				setCapitulo(String.valueOf(Integer.parseInt(getCapitulo())+1));
				action.setTitle(getNome()+":"+getCapitulo());
				minhaData = new BancoDeDados(this);
				minhaLista.clear();
				meuAdapter.clear();
				minhaLista = minhaData.allVersiculo(getLivro(), getCapitulo());
				meuAdapter.addAll(minhaLista);
				meuAdapter.notifyDataSetChanged();
			}catch(SQLiteFullException e){
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void voltar()
	{
		// TODO: Implement this method
		if(Integer.valueOf(getCapitulo()) > 1 && Integer.valueOf(getCapitulo()) <= getLastChapter()){
			try{
				setCapitulo(String.valueOf(Integer.parseInt(getCapitulo())-1));
				action.setTitle(getNome()+":"+getCapitulo());
				minhaData = new BancoDeDados(this);
				minhaLista.clear();
				meuAdapter.clear();
				minhaLista = minhaData.allVersiculo(getLivro(), getCapitulo());
				meuAdapter.addAll(minhaLista);
				meuAdapter.notifyDataSetChanged();
			}catch(SQLiteFullException e){
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public List<Versiculo> getLista(){
		minhaData = new BancoDeDados(this);
		return minhaData.allVersiculo(getLivro(), getCapitulo());
	}
	
	public int getLastChapter(){
		minhaData = new BancoDeDados(this);
		return minhaData.getLastChapter(getLivro());
	}

	private void falarCapitulo()
	{
		// TODO: Implement this method

		if(ts.isSpeaking()){
			ts.stop();
		}

		Iterator <Versiculo> it = getLista().iterator();
		Versiculo verso = null;
		StringBuilder msg = new StringBuilder();
		int cont = 1;
		while(it.hasNext()){
			verso = new Versiculo();
			verso = it.next();
			msg.append(verso.getTexto()+" ");
			cont++;
		}

	   	String original = msg.toString();
	   	String saidaFinal = Utils.getInstance().removeTagsHtml(original);  
		int count = saidaFinal.length();
		int max = 4000;
		int loopCount = count/max;

		for(int i = 0 ; i <= loopCount; i++ ) {
			if (i != loopCount) {
				ts.speak(saidaFinal.substring(i*max, (i+1)*max),ts.QUEUE_ADD, null);
			} else {
				int end = (count - ((i*max))+(i*max));
				ts.speak(saidaFinal.substring(i*max, end), TextToSpeech.QUEUE_ADD, null);
			}
		}
	}
	
	
	@Override
	public void config(Context contexto)
	{
		// TODO: Implement this method
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
		this.theme = prefs.getBoolean("checkBox", true);
		setRate(Float.parseFloat(prefs.getString("rate", "")));
		setPitch(Float.parseFloat(prefs.getString("pitch", "")));
		setLocale(new Locale(prefs.getString("ttsLang", "")));
		setNome(getIntent().getStringExtra("nome"));
		setLivro(getIntent().getStringExtra("livro"));
		setCapitulo(getIntent().getStringExtra("capitulo"));
	}


	@Override
	public void initSpeak(Context contexto)
	{
		// TODO: Implement this method
		ts = new TextToSpeech(contexto, new TextToSpeech.OnInitListener(){
				@Override
				public void onInit(int status)
				{
					if(status!=TextToSpeech.ERROR){
						ts.setLanguage(MyLocale.PT);
					}
				}
			});
	}


	@Override
	public void falar(String conteudo)
	{
		if(ts.isSpeaking()){
			ts.stop();
		}
		ts.setPitch(getPitch());
		ts.setSpeechRate(getRate());
		ts.speak(conteudo, TextToSpeech.QUEUE_FLUSH, null);
	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id)
	{
		falar("");
		// TODO: Implement this method
		String verso = U.getInstance().superScriptInvert(adapter.getItemAtPosition(posicao).toString());
		falar(verso);
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

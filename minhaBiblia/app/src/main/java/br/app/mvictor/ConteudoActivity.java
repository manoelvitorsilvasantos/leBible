package br.app.mvictor;




import android.content.*;
import android.os.*;
import android.preference.*;
import android.speech.tts.*;
import android.view.*;
import android.widget.*;
import br.app.mvictor.Utils.*;
import br.app.mvictor.dao.*;
import br.app.mvictor.modelo.*;
import java.util.*;
import br.lib.mvictor.utils.*;
import java.io.*;
import javax.xml.transform.*;
import android.database.sqlite.*;
import android.support.v7.app.*;

public class ConteudoActivity extends AppCompatActivity implements ListView.OnItemClickListener, ListView.OnItemLongClickListener
{

	
	private String capitulo = ("1");
	private String livro = ("1");
	private String nome = ("Book");
	private TextToSpeech ts;
	private Locale locale;
	private Locale pt = new Locale("pt-br");
	private float rate = (float) 1.0;
	private float pitch = (float) 1.0;
	private List<Versiculo> listItems = new ArrayList<Versiculo>();
	private List<Versiculo> listItems2 = new ArrayList<Versiculo>();
	private ArrayAdapter<Versiculo> myAdapter;
	private ListView listView;
	private BancoDeDados mBancoDeDados;
	public int ultimoCapitulo;
	
	
	
	public void setCapitulo(final String capitulo){ this.capitulo = capitulo;}
	public void setLivro(final String livro){ this.livro = livro;}
	public void setNome(final String nome){ this.nome = nome;}
	public void setLocale(final Locale locale){this.locale = locale;}
	public void setRate(final float rate){this.rate = rate;}
	public void setPitch(final float pitch){this.pitch = pitch;}
	public void setListItem(final List<Versiculo>listaItems){this.listItems = listaItems;}
	public void setMyAdapter(final ArrayAdapter<Versiculo>myAdapter){this.myAdapter = myAdapter;}
	
	
	public String getCapitulo(){return this.capitulo;}
	public String getLivro(){return this.livro;}
	public String getNome(){return this.nome;}
	public Locale getLocale(){return this.locale;}
	public float getRate(){return this.rate;}
	public float getPitch(){return this.pitch;}
	public List<Versiculo> getListItem(){return this.listItems;}
	public ArrayAdapter<Versiculo> getMyAdapter(){return this.myAdapter;}

	
	@Override
	protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			initVariables();
			initComponents();
			popularLista();
			
			//ttsSpeech
			ts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener(){
				@Override
				public void onInit(int status){
					if(status!=TextToSpeech.ERROR){
						ts.setLanguage(pt);
					}
				}
			});
			
	}

	private void popularLista()
	{
		// TODO: Implement this method
		mBancoDeDados = new BancoDeDados(this);
		listItems.clear();
		
		setCapitulo(getCapitulo());
		setLivro(getLivro());
		setNome(getNome());
		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null){
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setTitle(getNome().toUpperCase()+":"+getCapitulo());
		}
		
		listItems = mBancoDeDados.allVersiculo(getLivro(), getCapitulo());
		myAdapter = new ArrayAdapter<Versiculo>(this, R.layout.item_list2, listItems);
		listView.setAdapter(myAdapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		
	}

	public List<Versiculo>getLista(String livro, String capitulo){
		mBancoDeDados = new BancoDeDados(this);
		listItems2 = mBancoDeDados.allVersiculo(getLivro(), getCapitulo());
		return listItems2;
	}

	private void alerta(String str)
	{
		// TODO: Implement this method
		Toast.makeText(this, str, 1000).show();
	}

	

	private void initComponents(){
		listView = (ListView) findViewById(R.id.lvPessoa);
	}

	private void initVariables()
	{
		// TODO: Implement this method
		config();
		setLocale(Locale.getDefault());
		setLivro(getIntent().getStringExtra("livro"));
		setNome(getIntent().getStringExtra("nome"));
		setCapitulo(getIntent().getStringExtra("capitulo"));
		
	}

	private void config()
	{
		// TODO: Implement this method
		SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
		setRate(Float.parseFloat(preferencias.getString("rate", "Default list prefs")));
		setPitch(Float.parseFloat(preferencias.getString("pitch", "Default list prefs")));
		setLocale(new Locale(preferencias.getString("ttsLang", "Default list prefs")));
	}
	
	


	public void falarCapitulo(){

		if(ts.isSpeaking()){
			ts.stop();
		}

		String it_book  =  this.livro;
		String it_chapter = this.capitulo;

		Iterator <Versiculo> it = getLista(it_book, it_chapter).iterator();
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
	public void onItemClick(AdapterView<?> l, View view, int posicao, long id)
	{
		// TODO: Implement this method
		String texto = "Versiculo "+l.getItemAtPosition(posicao).toString();
		falar(U.getInstance().superScriptInvert(texto));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> l, View view, int posicao, long id)
	{
		String texto = l.getItemAtPosition(posicao).toString();
		String nomeLivro = getNome();
		String capituloLivro = getCapitulo();
		String conteudo = nomeLivro+":"+capituloLivro+"\n"+texto+"\n\n"+"LeBible";
		Intent send = new Intent();
		send.setAction(Intent.ACTION_SEND);
		send.putExtra(Intent.EXTRA_TEXT, conteudo);
		send.setType("text/plain");
		startActivity(Intent.createChooser(send, "Share versicle"));
		return false;
	}
	
	private void falar(final String str){
		if(ts.isSpeaking()){
			ts.stop();
		}
		ts.setSpeechRate(getRate());
		ts.setPitch(getPitch());
		ts.setLanguage(getLocale());
		ts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		this.finishAffinity();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.conteudo, menu);
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
			case R.id.action_falar:
				falarCapitulo();
				break;
			default:break;
		}
		return true;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(ts!=null){
			ts.stop();
			ts.shutdown();
		}
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		//ttsSpeech
		ts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener(){
				@Override
				public void onInit(int status){
					if(status!=TextToSpeech.ERROR){
						ts.setLanguage(pt);
					}
				}
			});
	}
	
	
	

	
}

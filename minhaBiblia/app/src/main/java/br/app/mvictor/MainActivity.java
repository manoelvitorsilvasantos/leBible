package br.app.mvictor;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import br.app.mvictor.dao.*;
import java.util.*;
import br.app.mvictor.modelo.*;
import java.io.*;
import android.content.*;
import android.preference.*;
import android.content.res.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.app.ActionBar;
import android.support.v4.view.*;
import android.net.*;
import android.provider.*;


public class MainActivity extends Primordial implements ReclyclerViewAdapter.ItemClickListener
{

    private ReclyclerViewAdapter adapter;
	private BancoDeDados mBancoDeDados;
	private List<Livro> listLivros = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_grid);
		preferenciasDefaults();
	
	    ActionBar action = getSupportActionBar();
		if(action!=null){
			action.setDisplayHomeAsUpEnabled(true);
			action.setHomeButtonEnabled(true);
		}
		// data to populate the RecyclerView with
        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
        int numberOfColumns = 1;
		mBancoDeDados = new BancoDeDados(this);
		recyclerView.clearOnChildAttachStateChangeListeners();
		listLivros = mBancoDeDados.allLivro();
		
		String[] minhaData = mBancoDeDados.getLivros().toArray(new String[0]);
	
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ReclyclerViewAdapter(this, minhaData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    
	}
	
    @Override
    public void onItemClick(View view, int position) {
		String livro = String.valueOf(position+1);
		String nomeLivro = adapter.getItem(position);
		Intent i = new Intent(this, ChapterActivity.class);
		i.putExtra("nome", nomeLivro.toUpperCase());
		i.putExtra("livro", livro);
		startActivity(i);
		finishAffinity();
    }
	
	private void preferenciasDefaults()
	{
		//SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		//SharedPreferences.Editor editor = sharedPreferences.edit();
		/*Resources res = getResources();
		editor.putBoolean("checkBox", false);
		editor.putString("pitch", res.getString(R.string.pitch_default));
		editor.putString("rate", res.getString(R.string.rate_default));
		editor.putString("ttsLang", res.getString(R.string.lang_default));
		editor.commit();*/
	}
	

	@Override
	public void onBackPressed()
	{
		this.finishAffinity();
		// TODO: Implement this method
		super.onBackPressed();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_verso, menu);
		MenuItem busca = menu.findItem(R.id.buscaMenu);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(busca);
		search(searchView);
		return true;
	}

	private void search(SearchView searchView)
	{
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
				@Override
				public boolean onQueryTextSubmit(String str)
				{
					enviar(str);
					return true;
				}

				@Override
				public boolean onQueryTextChange(String str)
				{
					// TODO: Implement this method
					return false;
				}

			});
	}


	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case android.R.id.home:
				this.finishAffinity();
				break;
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
				Intent settsIntent = new Intent(MainActivity.this, Preference.class);
				startActivity(settsIntent);
                break;
			case R.id.action_back:
				Intent intent = new Intent(MainActivity.this, AboutActivity.class);
				startActivity(intent);
				break;
            default:
				break;
        }
		return true;
    }

	private void enviar(String keyword){
		Intent tela = new Intent(this, DescricaoActivity.class);
		tela.putExtra("keyword",keyword);
		startActivity(tela);
		finishAffinity();
	}
	
}

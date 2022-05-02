package br.app.mvictor;
import android.view.*;
import android.content.*;
import android.widget.*;
import br.app.mvictor.dao.*;
import java.util.*;
import br.app.mvictor.modelo.*;
import android.os.*;
import java.io.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;


public class ChapterActivity extends AppCompatActivity implements ReclyclerViewAdapter.ItemClickListener 
{
	private ReclyclerViewAdapter adapter;
	private BancoDeDados mBancoDeDados;
	private List<Capitulo> listCapitulo = new ArrayList<>();
	private String livro;
	private String nomeLivro;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_grid);

		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null){
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setTitle(getIntent().getStringExtra("nome"));
		}

		// data to populate the RecyclerView with
        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
        int numberOfColumns = 4;
		mBancoDeDados = new BancoDeDados(this);
		recyclerView.clearOnChildAttachStateChangeListeners();
		this.livro = getIntent().getStringExtra("livro");
		this.nomeLivro = getIntent().getStringExtra("nome");
	    
		listCapitulo = mBancoDeDados.allCapitulo(this.livro);
		String[] minhaData = mBancoDeDados.getChapter(this.livro).toArray(new String[0]);

        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ReclyclerViewAdapter(this, minhaData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

	}

    @Override
    public void onItemClick(View view, int position) {
		String capitulo = String.valueOf(position+1);
		Intent i = new Intent(this, VerseActivity.class);
		i.putExtra("livro", livro);
		i.putExtra("capitulo", capitulo);
		i.putExtra("nome", nomeLivro.toLowerCase());
		startActivity(i);
		finishAffinity();
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
		// TODO: Implement this method
		return true;
	}


	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		startActivity(new Intent(this, MainActivity.class));
	}
	
}

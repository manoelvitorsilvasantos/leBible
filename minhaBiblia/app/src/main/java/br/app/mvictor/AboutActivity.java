package br.app.mvictor;
import android.os.*;
import android.widget.*;
import android.text.*;
import android.content.*;
import android.view.*;
import android.text.method.*;
import android.support.v7.app.*;


public class AboutActivity extends Primordial
{
	
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about);
		
		textView = (TextView) findViewById(R.id.textView);
		textView.setPadding(8,10,8,10);
		textView.setText(Html.fromHtml(getResources().getString(R.string.html)));
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null){
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setTitle("Sobre App");
		}
	}

	@Override
	public void onBackPressed()
	{
		home();
		// TODO: Implement this method
		super.onBackPressed();
	}
	
	private void home(){
		Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
		startActivityForResult(myIntent, 0);
		this.finishAffinity();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		home();
		// TODO: Implement this method
		return super.onOptionsItemSelected(item);
	}  
}

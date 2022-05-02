package br.app.mvictor;



import android.content.*;
import android.content.res.*;
import android.os.*;
import android.preference.*;
import android.widget.Toast;
import br.app.mvictor.dao.BancoDeDados;
import java.io.*;
import android.support.v7.app.*;

public class TelaActivity extends Primordial
{
	
	private BancoDeDados mBancoDeDados;

	private SharedPreferences sharedPreferences;

	private SharedPreferences sh;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		preferenciasDefaults();
		inicializarBancoDeDados();
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable(){
			@Override
			public void run(){
			
				mostrar();
			}
		}, 2000);
		
	}
	
	private void preferenciasDefaults()
	{
		Resources res = getResources();
		sh = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor meuEditor = sh.edit();
		meuEditor.putBoolean("checkBox", false);
		meuEditor.putString("pitch",res.getString(R.string.pitch_default));
		meuEditor.putString("pitch", res.getString(R.string.pitch_default));
		meuEditor.putString("rate", res.getString(R.string.rate_default));
		meuEditor.putString("ttsLang", res.getString(R.string.lang_default));
		meuEditor.commit();
	}
	
	private void mostrar(){
		Intent i = new Intent(TelaActivity.this, MainActivity.class);
		startActivity(i);
	}
	
	private void inicializarBancoDeDados() {
        mBancoDeDados = new BancoDeDados(this);
        File database = getApplicationContext().getDatabasePath(BancoDeDados.NOMEDB);
        if (database.exists() == false){
            mBancoDeDados.getReadableDatabase();
            if (copiaBanco(this)){
                alert(this, "Banco copiado com sucesso", 500);
            }else{
                alert(this, "Erro ao copiar o banco de dados", 500);
            }
        }
    }
	

    private boolean copiaBanco(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(BancoDeDados.NOMEDB);
            String outFile = BancoDeDados.LOCALDB + BancoDeDados.NOMEDB;
            OutputStream outputStream = new FileOutputStream(outFile);
            byte[] buff = new byte[1024];
            int legth = 0;
            while ((legth = inputStream.read(buff))>0){
                outputStream.write(buff,0,legth);
            }
            outputStream.flush();
            outputStream.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
	
	
	
}

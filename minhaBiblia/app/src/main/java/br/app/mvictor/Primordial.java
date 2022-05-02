package br.app.mvictor;

import android.support.v7.app.*;
import android.content.*;
import android.widget.*;

public class Primordial extends AppCompatActivity
{
	
	public void alert(Context contexto, String saida, int milissegundos){
		Toast.makeText(contexto, saida, milissegundos).show();
	}

	public void dialog(Context contexto, String titulo, String corpo, String botaoTag){
		final AlertDialog.Builder meuAlerta = new AlertDialog.Builder(contexto);
		meuAlerta.setTitle(titulo);
		meuAlerta.setMessage(corpo);
		meuAlerta.setNeutralButton(botaoTag, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					p1.dismiss();
				}
			});
		meuAlerta.create().show();
	}
}

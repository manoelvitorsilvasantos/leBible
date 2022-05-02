package br.app.mvictor;
import android.content.*;

public interface Setting
{
	public abstract void config(Context contexto);
	public abstract void falar(String conteudo);
	public abstract void initSpeak(Context context);
}

package br.app.mvictor;
import android.preference.*;
import android.os.*;




public class Preference extends PreferenceActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
	
}

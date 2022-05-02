package br.app.mvictor.Utils;

public class Utils
{
	private static Utils INSTANCE;
	private Utils(){}
	public static Utils getInstance(){
		if(INSTANCE==null){
			INSTANCE=new Utils();
		}
		return INSTANCE;
	}
	public static String removeTagsHtml(String str){
		str=str.replaceAll("<small>","");
		str=str.replaceAll("</small>","");
		str=str.replaceAll("<i>","");
		str=str.replaceAll("</i>","");
		str=str.replaceAll("Yahweh","Yahveh");
		return str;
	}
}

package br.app.mvictor.dao;

/**
 * Created by Manoel Vitor on 26/01/22.
 */

import android.content.*;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.app.mvictor.modelo.*;
import java.util.*;

public class BancoDeDados extends SQLiteOpenHelper
 {
    public static final String NOMEDB = "biblia";
    public static final String LOCALDB = "/data/data/br.app.mvictor/databases/";
    private static final int VERSION = 1;
    private Context mContext;
    private SQLiteDatabase mSQSqLiteDatabase;


    public BancoDeDados(Context context) {
        super(context, NOMEDB, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDataBase(){
        String dbPath = mContext.getDatabasePath(NOMEDB).getPath();
        if (mSQSqLiteDatabase != null && mSQSqLiteDatabase.isOpen()){
            return;
        }
        mSQSqLiteDatabase = SQLiteDatabase.openDatabase(dbPath,null, SQLiteDatabase.OPEN_READWRITE);
    }
	
	public int getIdLivro(String search){
		openDataBase();
		int id = 0;
		mSQSqLiteDatabase = this.getReadableDatabase();
		String sql = "SELECT _id FROM livro WHERE titulo LIKE ?";
		Cursor c = mSQSqLiteDatabase.rawQuery(sql ,new String[]{"%"+search+"%"});
		if(c.moveToLast()){
			id = c.getInt(c.getColumnIndex("_id"));
		}
		c.close();
		mSQSqLiteDatabase.close();
		return id;
	}
	
	public String getNomeLivro(String search){
		openDataBase();
		String nome  = null;
		mSQSqLiteDatabase = this.getReadableDatabase();
		String sql = "SELECT distinct titulo FROM livro WHERE titulo LIKE ?";
		Cursor c = mSQSqLiteDatabase.rawQuery(sql ,new String[]{"%"+search+"%"});
		if(c.moveToLast()){
			nome = c.getString(c.getColumnIndex("titulo"));
		}
		c.close();
		mSQSqLiteDatabase.close();
		return nome;
	}
	
	
	public String getCapitulo(String search){
		openDataBase();
		String capitulo  = null;
		mSQSqLiteDatabase = this.getReadableDatabase();
		String sql = "SELECT distinct capitulo FROM versiculo WHERE id_livro = ?";
		Cursor c = mSQSqLiteDatabase.rawQuery(sql ,new String[]{search});
		if(c.moveToLast()){
			capitulo = c.getString(c.getColumnIndex("capitulo"));
		}
		c.close();
		mSQSqLiteDatabase.close();
		return capitulo;
	}
	
	public String getOnlyVerse(String idLivro, String capitulo, String verso){
		openDataBase();
		String texto = null;
		mSQSqLiteDatabase = this.getReadableDatabase();
		String sql = "SELECT distinct texto FROM versiculo WHERE id_livro = ? AND capitulo = ? AND numero = ?";
		Cursor c = mSQSqLiteDatabase.rawQuery(sql ,new String[]{idLivro, capitulo, verso});
		if(c.moveToLast()){
			texto = c.getString(c.getColumnIndex("texto"));
		}
		c.close();
		mSQSqLiteDatabase.close();
		return texto;
	}
	
	
	public int getLastChapter(String livro){
		openDataBase();
		int ultimo = 0;
		mSQSqLiteDatabase = this.getReadableDatabase();
		Cursor c = mSQSqLiteDatabase.rawQuery("SELECT DISTINCT * FROM versiculo WHERE id_livro = ? ORDER BY capitulo DESC LIMIT 1",new String[]{livro});
		if(c.moveToLast()){
			ultimo = c.getInt(c.getColumnIndex("capitulo"));
		}
		c.close();
		mSQSqLiteDatabase.close();
		return ultimo;
	}
	
	public int getChapterNumber(String chapter){
		openDataBase();
		int ultimo = 0;
		mSQSqLiteDatabase = this.getReadableDatabase();
		Cursor c = mSQSqLiteDatabase.rawQuery("SELECT DISTINCT capitulo FROM versiculo WHERE capitulo LIKE = ?",new String[]{"%"+chapter+"%"});
		if(c.moveToLast()){
			ultimo = c.getInt(c.getColumnIndex("capitulo"));
		}
		c.close();
		mSQSqLiteDatabase.close();
		return ultimo;
	}
	
	

	
	public List<String> getLivros(){
	 	openDataBase();
        mSQSqLiteDatabase = this.getWritableDatabase();
        List <String>listLivro = new ArrayList<>();
        String sql = "SELECT * FROM livro ORDER BY ordem";
        Cursor cursor = mSQSqLiteDatabase.rawQuery(sql,null);
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do{
					Livro l = new Livro();
					l.setId(cursor.getInt(cursor.getColumnIndex("_id")));
					l.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
					listLivro.add(l.getTitulo());
                }while(cursor.moveToNext());

            }
        }
        cursor.close();
        mSQSqLiteDatabase.close();
        return listLivro;
		
	}
	
	public List<Descricao> getVerse(String keyword){
		openDataBase();
        mSQSqLiteDatabase = this.getWritableDatabase();
        List<Descricao> listVersiculo = new ArrayList<Descricao>();
        String sql = "SELECT * FROM versiculo v JOIN livro l ON l._id = v.id_livro WHERE texto LIKE ? ORDER BY l._id";
        Cursor cursor = mSQSqLiteDatabase.rawQuery(sql, new String[]{"%"+keyword+"%"});
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do{
				    Descricao d = new Descricao();
					d.setId(cursor.getInt(cursor.getColumnIndex("id_livro")));
					d.setNome(cursor.getString(cursor.getColumnIndex("titulo")));
					d.setCapitulo(cursor.getInt(cursor.getColumnIndex("capitulo")));
					d.setVersiculo(cursor.getInt(cursor.getColumnIndex("numero")));
					d.setTexto(cursor.getString(cursor.getColumnIndex("texto")));
					listVersiculo.add(d);
                }while(cursor.moveToNext());

            }
        }
        cursor.close();
        mSQSqLiteDatabase.close();
        return listVersiculo;
	}
	
	public List<Versiculo> allVersiculo(String livro, String capitulo){
		openDataBase();
        mSQSqLiteDatabase = this.getWritableDatabase();
        List<Versiculo> listVersiculo = new ArrayList<>();
        String sql = "SELECT DISTINCT id_livro, capitulo, numero,  texto FROM versiculo WHERE id_livro = ? AND capitulo = ?";
        Cursor cursor = mSQSqLiteDatabase.rawQuery(sql, new String[]{livro, capitulo});
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do{
				    Versiculo v = new Versiculo();
					v.setId(cursor.getInt(cursor.getColumnIndex("id_livro")));
					v.setVersiculo(cursor.getInt(cursor.getColumnIndex("numero")));
					v.setTexto(cursor.getString(cursor.getColumnIndex("texto")));
					listVersiculo.add(v);

                }while(cursor.moveToNext());

            }
        }
        cursor.close();
        mSQSqLiteDatabase.close();
        return listVersiculo;
	}
	
	public List<String> getChapter(String idLivro){
		openDataBase();
        mSQSqLiteDatabase = this.getWritableDatabase();
        List<String> listCapitulo = new ArrayList<>();
        String sql = "SELECT DISTINCT id_livro, capitulo FROM versiculo WHERE id_livro = ?";
        Cursor cursor = mSQSqLiteDatabase.rawQuery(sql, new String[]{idLivro});
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do{
				    Capitulo c = new Capitulo();
					c.setId(cursor.getInt(cursor.getColumnIndex("id_livro")));
					c.setCapitulo(cursor.getInt(cursor.getColumnIndex("capitulo")));
					listCapitulo.add(String.valueOf(c.getCapitulo()));

                }while(cursor.moveToNext());

            }
        }
        cursor.close();
        mSQSqLiteDatabase.close();
        return listCapitulo;
	}

	
	public List<Capitulo> allCapitulo(String idLivro){
		openDataBase();
        mSQSqLiteDatabase = this.getWritableDatabase();
        List<Capitulo> listCapitulo = new ArrayList<>();
        String sql = "SELECT DISTINCT id_livro, capitulo FROM versiculo WHERE id_livro = ?";
        Cursor cursor = mSQSqLiteDatabase.rawQuery(sql, new String[]{idLivro});
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do{
				    Capitulo c = new Capitulo();
					c.setId(cursor.getInt(cursor.getColumnIndex("id_livro")));
					c.setCapitulo(cursor.getInt(cursor.getColumnIndex("capitulo")));
					listCapitulo.add(c);

                }while(cursor.moveToNext());

            }
        }
        cursor.close();
        mSQSqLiteDatabase.close();
        return listCapitulo;
	}

    public List<Livro> allLivro(){
        openDataBase();
        mSQSqLiteDatabase = this.getWritableDatabase();
        List<Livro> listLivro = new ArrayList<>();
        String sql = "SELECT * FROM livro ORDER BY ordem";
        Cursor cursor = mSQSqLiteDatabase.rawQuery(sql,null);
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do{
					Livro l = new Livro();
					l.setId(cursor.getInt(cursor.getColumnIndex("_id")));
					l.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
					listLivro.add(l);
					
                }while(cursor.moveToNext());
				
            }
        }
        cursor.close();
        mSQSqLiteDatabase.close();
        return listLivro;

    }

}

package inc.meh.DBHelper;

import android.content.Context; 
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper; 
import android.database.sqlite.SQLiteStatement; 
import android.util.Log;  
import java.util.ArrayList; 
import java.util.List;   


public class DBHelper {      
	private static final String DATABASE_NAME = "coordinates.db";    
	private static final int DATABASE_VERSION = 5;    
	private static final String TABLE_NAME = "location";      
	private Context context;    
	private SQLiteDatabase db;      
	private SQLiteStatement insertStmt;    
	private static final String INSERT = "insert into " + TABLE_NAME + " (insertype,lat,lon,cat,isdeductible,created_date) values (?,?,?,?,?,?)";      
	public DBHelper(Context context) {       
		this.context = context;       
		OpenHelper openHelper = new OpenHelper(this.context);       
		this.db = openHelper.getWritableDatabase();       
		this.insertStmt = this.db.compileStatement(INSERT);   
		}     
	
	public long insert(String name,Double lat, Double lon, String cat, int isdeductible) {  
		this.insertStmt.bindString(1, name);
		this.insertStmt.bindDouble(2,lat);
		this.insertStmt.bindDouble(3,lon);
		this.insertStmt.bindString(4,cat);
		this.insertStmt.bindDouble(5,isdeductible);
		return this.insertStmt.executeInsert();    
		}      
	
	public void deleteAll() {       
			this.db.delete(TABLE_NAME, null, null);    
			}      
	
	public String SelectRow(String str) {
		String strRow = "";

		//Cursor cursor = this.db.query(TABLE_NAME, new String[] {"insertype"}, "insertype like " + "'" + str + "%'",null,null,null,"id desc");
		
		Cursor cursor = this.db.query(TABLE_NAME, new String[] {"insertype"}, "insertype like '" + str + "%'",null,null,null,"id desc");
		
		if (cursor.moveToFirst()) {
			strRow = cursor.getString(0);
		}
		
		cursor.close();
        return strRow; 
    }

	public String[] SelectRowArray(String str) {
		String strRow="";

		//Cursor cursor = this.db.query(TABLE_NAME, new String[] {"insertype"}, "insertype like " + "'" + str + "%'",null,null,null,"id desc");
		
		Cursor cursor = this.db.query(TABLE_NAME, new String[] {"insertype"}, "insertype like '" + str + "%'",null,null,null,"id desc");
		
		if (cursor.moveToFirst()) {
			strRow = cursor.getString(0);
		}
		
		cursor.close();
		String[] sReturn=strRow.split(",");
		
        return sReturn; 
    }
	
	public List<String> selectAll(String sortstring) {      
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query(TABLE_NAME, new String[] { "insertype", "lat", "lon", "cat", "isdeductible", "created_date"},null, null, null, null, sortstring);
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0));
				list.add(cursor.getString(1));
				list.add(cursor.getString(2));
				list.add(cursor.getString(3));
				list.add(cursor.getString(4));
				}
			while (cursor.moveToNext());
			}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();    
			}   
		return list;  
		}     
	
	
		private static class OpenHelper extends SQLiteOpenHelper {  
			OpenHelper(Context context) {         
				super(context, DATABASE_NAME, null, DATABASE_VERSION); 
				}         
			
			@Override      
			public void onCreate(SQLiteDatabase db) {         
				db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, insertype TEXT, lat real, lon real, cat TEXT, isdeductible int, created_date date default CURRENT_DATE)");      
				}         
			
			@Override     
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				Log.w("Example", "Upgrading database, this will drop tables and recreate.");   
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);          onCreate(db);       
				}   
			}
		}

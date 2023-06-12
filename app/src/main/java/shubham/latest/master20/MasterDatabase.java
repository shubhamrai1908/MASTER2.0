package shubham.latest.master20;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MasterDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MasterApp_database";
    private static final String TABLE_NAME1 = "gnd_to_master_table";
    private static final String TABLE_NAME2 = "master_to_server_table";
    private static final String TABLE_NAME3 = "server_to_master_table";
    private static final String TABLE_NAME4 = "master_to_gnd_table";
    private static final String TABLE_NAME5 = "request_table";
    private static final String TABLE_NAME6= "groundlist_table";
    private static final String TABLE_NAME7= "serverlist_table";

    MasterDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable1 = "create table " + TABLE_NAME1 + "(id INTEGER PRIMARY KEY, sender TEXT, req TEXT,op TEXT,msg TEXT,time TEXT)";
        String createTable2 = "create table " + TABLE_NAME2 + "(id INTEGER PRIMARY KEY, server TEXT, req TEXT,op TEXT,gnd TEXT,msg TEXT,time TEXT)";
        String createTable3 = "create table " + TABLE_NAME3 + "(id INTEGER PRIMARY KEY, server TEXT, req TEXT,op TEXT,msg TEXT,time TEXT)";
        String createTable4 = "create table " + TABLE_NAME4 + "(id INTEGER PRIMARY KEY, gnd TEXT, req TEXT,op TEXT,msg TEXT,time TEXT)";
        String createTable5 = "create table " + TABLE_NAME5 + "(id INTEGER PRIMARY KEY, gnd TEXT, req TEXT,op TEXT,time TEXT)";
        String createTable6 = "create table " + TABLE_NAME6 + "(id INTEGER PRIMARY KEY, name TEXT,phone TEXT)";
        String createTable7 = "create table " + TABLE_NAME7 + "(id INTEGER PRIMARY KEY, name TEXT,phone TEXT)";
        sqLiteDatabase.execSQL(createTable1);
        sqLiteDatabase.execSQL(createTable2);
        sqLiteDatabase.execSQL(createTable3);
        sqLiteDatabase.execSQL(createTable4);
        sqLiteDatabase.execSQL(createTable5);
        sqLiteDatabase.execSQL(createTable6);
        sqLiteDatabase.execSQL(createTable7);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME4);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME5);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME6);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME7);
        onCreate(sqLiteDatabase);

    }
    public boolean groundTOmaster(String gndno,String reqno,String operator,String msg,String time)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sender",gndno);
        contentValues.put("req",reqno);
        contentValues.put("op",operator);
        contentValues.put("msg",msg);
        contentValues.put("time",time);
        sqLiteDatabase.insert(TABLE_NAME1,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }
    public boolean masterTOserver(String serverno,String reqno,String operator,String gndno,String msg,String time)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("server",serverno);
        contentValues.put("req",reqno);
        contentValues.put("op",operator);
        contentValues.put("gnd",gndno);
        contentValues.put("msg",msg);
        contentValues.put("time",time);
        sqLiteDatabase.insert(TABLE_NAME2,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }
    public boolean serverTOmaster(String serverno,String reqno,String op,String response,String time)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("server",serverno);
        contentValues.put("req",reqno);
        contentValues.put("op",op);
        contentValues.put("msg",response);
        contentValues.put("time",time);
        sqLiteDatabase.insert(TABLE_NAME3,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }
    public boolean masterTOground(String gndno,String reqno,String operator,String response,String time)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("gnd",gndno);
        contentValues.put("req",reqno);
        contentValues.put("op",operator);
        contentValues.put("msg",response);
        contentValues.put("time",time);
        sqLiteDatabase.insert(TABLE_NAME4,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }
    public boolean requestTable(String gndno,String reqno,String operator,String time)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("gnd",gndno);
        contentValues.put("req",reqno);
        contentValues.put("op",operator);
        contentValues.put("time",time);
        sqLiteDatabase.insert(TABLE_NAME5,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }
    public boolean grounglistTable(String name,String phone)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("phone",phone);
        sqLiteDatabase.insert(TABLE_NAME6,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }
    public boolean serverlistTable(String name,String phone)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("phone",phone);
        sqLiteDatabase.insert(TABLE_NAME7,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }

    public Cursor getAll(String TABLE_NAME)
    {
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        // db.close();
        return res;
    }

    public void deleteData(String id,String TABLE_NAME)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+ TABLE_NAME + " WHERE id =" + id);
        sqLiteDatabase.close();
    }

    public void eraseData(String TABLE_NAME)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
        sqLiteDatabase.close();

    }
}

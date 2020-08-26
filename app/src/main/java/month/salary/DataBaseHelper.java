package month.salary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "calculator.db";
    public static final String TABLE_NAME = "userInfo";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "workHours";
    public static final String COL_3 = "hourSalary";
    public static final String COL_4 = "salary";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE userInfo (ID INTEGER PRIMARY  KEY AUTOINCREMENT, workHours TEXT, hourSalary TEXT, salary TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean updateInfo(String workHour, String salaryHour)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("workHours", workHour);
        contentValues.put("hourSalary", salaryHour);

        int result = db.update(TABLE_NAME,contentValues,"ID = ?",new String[]{"1"});

        if(result == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public Cursor getData()
    {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "SELECT  * FROM " + TABLE_NAME;
        Cursor res = database.rawQuery(query, null);
        res.moveToFirst();

        return res;
    }

    public void clearDatabase()
    {
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(TABLE_NAME,null,null);
    }


    public long addData(String workH, String hourS)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("workHours", workH);
        contentValues.put("hourSalary", hourS);

        long res = db.insert("userInfo", null, contentValues);
        db.close();

        return res;
    }
}

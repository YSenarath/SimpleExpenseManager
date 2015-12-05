package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.ExpenseMetaData;

/**
 * Created by Yasas Senarath (130556L)
 * For DB Project
 */
public class DBHelper extends SQLiteOpenHelper{

    /**
     * Information regarding Table: ACCOUNT
     */
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS account (" +
            ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO + " TEXT PRIMARY KEY, " +
            ExpenseMetaData.ACCOUNT_COLUMN_BANK + " TEXT NOT NULL, " +
            ExpenseMetaData.ACCOUNT_COLUMN_HOLDER + " TEXT NOT NULL, " +
            ExpenseMetaData.ACCOUNT_COLUMN_BALANCE + " REAL DEFAULT 0);";

    /**
     * Information regarding Table: TRANSACTION
     */
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE IF NOT EXISTS " + ExpenseMetaData.TRANSACTION_TABLE_NAME + " (" +
            ExpenseMetaData.TRANSACTION_COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ExpenseMetaData.TRANSACTION_COLUMN_ACCOUNT_NO + " TEXT NOT NULL, " +
            ExpenseMetaData.TRANSACTION_COLUMN_EXPENSE_TYPE + " INTEGER NOT NULL, " +
            ExpenseMetaData.TRANSACTION_COLUMN_AMOUNT + " REAL DEFAULT 0, " +
            ExpenseMetaData.TRANSACTION_COLUMN_DATE + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + ExpenseMetaData.TRANSACTION_COLUMN_ACCOUNT_NO + ") REFERENCES " +
            ExpenseMetaData.ACCOUNT_TABLE_NAME + "(" + ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO + "));";

    public DBHelper(Context context) {
        super(context, ExpenseMetaData.DATABASE_NAME, null, ExpenseMetaData.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseMetaData.TRANSACTION_TABLE_NAME);
        // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseMetaData.ACCOUNT_TABLE_NAME);
        sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNT); // IF NOT EXIST
        sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTION); // IF NOT EXIST
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseMetaData.TRANSACTION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseMetaData.ACCOUNT_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

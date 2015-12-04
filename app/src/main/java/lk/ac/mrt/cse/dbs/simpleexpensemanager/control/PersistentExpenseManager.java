package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by Yasas on 12/3/2015.
 */
public class PersistentExpenseManager extends ExpenseManager {
    private static final String DB_NAME = "130556L";
    private SQLiteDatabase db;

    /**
     * Information regarding Table: ACCOUNT
     */
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS account (" +
            "acc_id VARCHAR(8) PRIMARY KEY, \n" +
            "bank VARCHAR(25) NOT NULL,\n" +
            "acc_holder VARCHAR(25) NOT NULL,\n" +
            "balance NUMERIC(10, 2) DEFAULT 0);";

    /**
     * Information regarding Table: TRANSACTION
     */
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE IF NOT EXISTS transaction_ (" +
            "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "acc_id VARCHAR(8) NOT NULL,\n" +
            "is_expense BOOLEAN NOT NULL,\n" +
            "amount NUMERIC(10, 2) DEFAULT 0,\n" +
            "date VARCHAR(10) NOT NULL,\n" +
            "FOREIGN KEY(acc_id) REFERENCES account(acc_id));";

    /**
     *
     * @param context
     */
    public PersistentExpenseManager(Context context) {
        db = context.openOrCreateDatabase(PersistentExpenseManager.DB_NAME, Context.MODE_PRIVATE, null);
        // db.execSQL("DROP TABLE IF EXISTS account");
        // db.execSQL("DROP TABLE IF EXISTS transaction_");
        db.execSQL(CREATE_TABLE_ACCOUNT); // IF NOT EXIST
        db.execSQL(CREATE_TABLE_TRANSACTION); // IF NOT EXIST
        setup();
    }

    /**
     * This method should be implemented by the concrete implementation of this class. It will dictate how the DAO
     * objects will be initialized.
     */
    @Override
    public void setup() {

        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(db);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(db);
        setAccountsDAO(persistentAccountDAO);

        /*** End ***/
    }
}

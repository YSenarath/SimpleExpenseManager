package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by Yasas Senarath (130556L)
 * For DB Project
 */
public class PersistentExpenseManager extends ExpenseManager {
    // Creates new database if not exist and manage the DB.
    private DBHelper dbHelper;

    /**
     * Constructor
     * @param context MainActivity Application Context
     */
    public PersistentExpenseManager(Context context) {
        // context.deleteDatabase(ExpenseMetaData.DATABASE_NAME);
        dbHelper = new DBHelper(context);
        setup();
    }

    /**
     * This method should be implemented by the concrete implementation of this class. It will dictate how the DAO
     * objects will be initialized.
     */
    @Override
    public void setup() {
        /*** Begin ***/

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(dbHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dbHelper);
        setAccountsDAO(persistentAccountDAO);

        /*** End ***/
    }
}

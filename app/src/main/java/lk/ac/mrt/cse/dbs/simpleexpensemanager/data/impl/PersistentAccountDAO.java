package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.ExpenseMetaData;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Yasas Senarath (130556L)
 * For DB Project
 */
public class PersistentAccountDAO implements AccountDAO {
    private DBHelper dbHelper;

    public PersistentAccountDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Get a list of account numbers.
     *
     * @return - list of account numbers as String
     */
    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Return List
        ArrayList<String> array_list = new ArrayList<>();
        // SELECT (Query)
        Cursor res = db.rawQuery("SELECT * FROM " + ExpenseMetaData.ACCOUNT_TABLE_NAME, null);
        // Iterate through the table to build the list
        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                array_list.add(res.getString(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO)));
                res.moveToNext();
            }
        }
        // Un-refer the database
        db.close();
        return array_list;
    }

    /**
     * Get a list of accounts.
     *
     * @return - list of Account objects.
     */
    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Return List
        ArrayList<Account> array_list = new ArrayList<>();
        // SELECT (Query)
        Cursor res = db.rawQuery("SELECT * FROM " + ExpenseMetaData.ACCOUNT_TABLE_NAME, null);

        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                // Get data
                String acc_id = res.getString(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO));
                String bank = res.getString(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_BANK));
                String acc_holder = res.getString(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_HOLDER));
                Double balance = res.getDouble(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_BALANCE));
                // Build Account - using data
                Account a = new Account(acc_id, bank, acc_holder, balance);
                // Add to return list
                array_list.add(a);
                res.moveToNext();
            }
        }
        return array_list;
    }


    /**
     * Get the account given the account number.
     *
     * @param accountNo as String
     * @return - the corresponding Account
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // SELECT (Query)
        Cursor res = db.rawQuery("SELECT * FROM " + ExpenseMetaData.ACCOUNT_TABLE_NAME +
                " WHERE " + ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO + " = ? ", new String[]{accountNo});
        // Throw exception if Account number is invalid: No accounts
        if (res.getCount() == 0) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        res.moveToFirst();
        // Get data
        String acc_id = res.getString(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO));
        String bank = res.getString(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_BANK));
        String acc_holder = res.getString(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_HOLDER));
        Double balance = res.getDouble(res.getColumnIndex(ExpenseMetaData.ACCOUNT_COLUMN_BALANCE));
        // Build and return the Account
        return new Account(acc_id, bank, acc_holder, balance);
    }

    /**
     * Add an account to the accounts collection.
     *
     * @param account - the account to be added.
     */
    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Content values (Builder)
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO, account.getAccountNo());
        contentValues.put(ExpenseMetaData.ACCOUNT_COLUMN_BANK, account.getBankName());
        contentValues.put(ExpenseMetaData.ACCOUNT_COLUMN_HOLDER, account.getAccountHolderName());
        contentValues.put(ExpenseMetaData.ACCOUNT_COLUMN_BALANCE, account.getBalance());
        /*long i =*/ db.insert(ExpenseMetaData.ACCOUNT_TABLE_NAME, null, contentValues);
        // if (i == -1) // Account No already exist
        // throw  new InvalidAccountException("Invalid account no");
        // Should throw an error
    }

    /**
     * Remove an account from the accounts collection.
     *
     * @param accountNo - of the account to be removed.
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Delete the account with the given account no
        int count = db.delete(ExpenseMetaData.ACCOUNT_TABLE_NAME, ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO + "=" + accountNo, null);
        // db.delete(ExpenseMetaData.ACCOUNT_TABLE_NAME, ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO + " = ? ",new String[]{accountNo});
        if (count == 0) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    /**
     * Update the balance of the given account. The type of the expense is specified in order to determine which
     * action to be performed.
     * <p/>
     * The implementation has the flexibility to figure out how the updating operation is committed based on the type
     * of the transaction.
     *
     * @param accountNo   - account number of the respective account
     * @param expenseType - the type of the transaction
     * @param amount      - amount involved
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Get account to get initial balance
        Account account = getAccount(accountNo);
        // Update balance with new transaction
        double balance = account.getBalance();
        if (expenseType == ExpenseType.EXPENSE) {
            balance -= amount;
        } else {
            balance += amount;
        }
        // Add balance to a content value
        ContentValues cv = new ContentValues();
        cv.put(ExpenseMetaData.ACCOUNT_COLUMN_BALANCE, balance);
        // Set the SELECTION
        String where = ExpenseMetaData.ACCOUNT_COLUMN_ACCOUNT_NO + " = ?";
        String[] whereArgs = new String[]{accountNo};
        // Update the account(DB)
        int upCount = db.update(ExpenseMetaData.ACCOUNT_TABLE_NAME, cv, where, whereArgs);
        // Show Exception if no account has been updated: Account no is invalid
        if (upCount == 0) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
}

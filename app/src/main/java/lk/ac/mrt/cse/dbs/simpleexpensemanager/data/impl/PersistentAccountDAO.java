package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Yasas on 12/3/2015.
 */
public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase db;

    public PersistentAccountDAO(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Get a list of account numbers.
     *
     * @return - list of account numbers as String
     */
    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> array_list = new ArrayList<>();

        Cursor res = db.rawQuery("select * from account", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("acc_id")));
            res.moveToNext();
        }

        return array_list;
    }

    /**
     * Get a list of accounts.
     *
     * @return - list of Account objects.
     */
    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> array_list = new ArrayList<>();

        Cursor res = db.rawQuery("select * from account", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            String acc_id = res.getString(res.getColumnIndex("acc_id"));
            String bank = res.getString(res.getColumnIndex("bank"));
            String acc_holder = res.getString(res.getColumnIndex("acc_holder"));
            Double balance = res.getDouble(res.getColumnIndex("balance"));
            Account a = new Account(acc_id, bank, acc_holder, balance);
            array_list.add(a);
            res.moveToNext();
        }

        return array_list;
    }


    /**
     * Get the account given the account number.
     *
     * @param accountNo as String
     * @return - the corresponding Account
     * @throws lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException - if the account number is invalid
     */
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor res = db.rawQuery("select * from account where acc_id=?", new String[] { accountNo });

        if (res.getCount() == 0) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        res.moveToFirst();
        String acc_id = res.getString(res.getColumnIndex("acc_id"));
        String bank = res.getString(res.getColumnIndex("bank"));
        String acc_holder = res.getString(res.getColumnIndex("acc_holder"));
        Double balance = res.getDouble(res.getColumnIndex("balance"));


        return new Account(acc_id, bank, acc_holder, balance);
    }

    /**
     * Add an account to the accounts collection.
     *
     * @param account - the account to be added.
     */
    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("acc_id", account.getAccountNo());
        contentValues.put("bank", account.getBankName());
        contentValues.put("acc_holder", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        long i = db.insert("account", null, contentValues);
        // if (i == -1)
            // throw  new InvalidAccountException("Invalid");
            // Should throw an error
    }

    /**
     * Remove an account from the accounts collection.
     *
     * @param accountNo - of the account to be removed.
     * @throws lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException - if the account number is invalid
     */
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        int count = db.delete("account", "acc_id" + "=" + accountNo, null);
        // db.delete("account","acc_id=?",new String[]{accountNo});
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
     * @throws lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException - if the account number is invalid
     */
    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
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
        cv.put("balance", balance);
        // Set the SELECTION
        String where = "acc_id = ?";
        String[] whereArgs = new String[] { accountNo  };
        // Update the account(DB)
        int upCount = db.update("account", cv, where, whereArgs);
        // Show Exception if no account has been updated: Account no is invalid
        if (upCount == 0){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
}

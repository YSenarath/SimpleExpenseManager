package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Utility.ExpenseUtils;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.ExpenseMetaData;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Yasas Senarath (130556L)
 * For DB Project
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DBHelper dbHelper;

    public PersistentTransactionDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Log the transaction requested by the user.
     *
     * @param date        - date of the transaction
     * @param accountNo   - account number involved
     * @param expenseType - type of the expense
     * @param amount      - amount involved
     */
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Content value (Builder)
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpenseMetaData.TRANSACTION_COLUMN_ACCOUNT_NO, accountNo);
        contentValues.put(ExpenseMetaData.TRANSACTION_COLUMN_AMOUNT, amount);
        contentValues.put(ExpenseMetaData.TRANSACTION_COLUMN_DATE, date.toString());
        contentValues.put(ExpenseMetaData.TRANSACTION_COLUMN_EXPENSE_TYPE, expenseType == ExpenseType.EXPENSE);
        // Insert to table
        db.insert(ExpenseMetaData.TRANSACTION_TABLE_NAME, null, contentValues);
        db.close();
    }

    /**
     * Return all the transactions logged.
     *
     * @return - a list of all the transactions
     */
    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Transaction> array_list = new ArrayList<>();

        Cursor res = db.rawQuery("SELECT * FROM " + ExpenseMetaData.TRANSACTION_TABLE_NAME +
                " ORDER BY " + ExpenseMetaData.TRANSACTION_COLUMN_TRANSACTION_ID, null);

        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                // Account no
                String acc_id = res.getString(res.getColumnIndex(ExpenseMetaData.TRANSACTION_COLUMN_ACCOUNT_NO));
                // Expense type
                int expenseTypeInt = res.getInt(res.getColumnIndex(ExpenseMetaData.TRANSACTION_COLUMN_EXPENSE_TYPE));
                ExpenseType expenseType = ExpenseUtils.getExpenseType(expenseTypeInt);
                // Transaction Amount
                Double amount = res.getDouble(res.getColumnIndex(ExpenseMetaData.TRANSACTION_COLUMN_AMOUNT));
                // Date
                String strDate = res.getString(res.getColumnIndex(ExpenseMetaData.TRANSACTION_COLUMN_DATE));
                Date date = ExpenseUtils.getDate(strDate);

                // Full Transaction
                Transaction a = new Transaction(date, acc_id, expenseType, amount);
                // Add to return list
                array_list.add(a);
                // Move to next item in selection
                res.moveToNext();
            }
        } /* else {
            // EMPTY: return empty list if there are no transactions
        } */
        db.close();
        return array_list;
    }

    /**
     * Return a limited amount of transactions logged.
     *
     * @param limit - number of transactions to be returned
     * @return - a list of requested number of transactions
     */
    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Transaction> array_list = new ArrayList<>();

        Cursor res = db.rawQuery("SELECT * FROM " + ExpenseMetaData.TRANSACTION_TABLE_NAME +
                " ORDER BY " + ExpenseMetaData.TRANSACTION_COLUMN_TRANSACTION_ID + " DESC LIMIT " + limit, null);

        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                // Account no
                String acc_id = res.getString(res.getColumnIndex(ExpenseMetaData.TRANSACTION_COLUMN_ACCOUNT_NO));
                // Expense type
                int expenseTypeInt = res.getInt(res.getColumnIndex(ExpenseMetaData.TRANSACTION_COLUMN_EXPENSE_TYPE));
                ExpenseType expenseType = ExpenseUtils.getExpenseType(expenseTypeInt);
                // Transaction Amount
                Double amount = res.getDouble(res.getColumnIndex(ExpenseMetaData.TRANSACTION_COLUMN_AMOUNT));
                // Date
                String strDate = res.getString(res.getColumnIndex(ExpenseMetaData.TRANSACTION_COLUMN_DATE));
                Date date = ExpenseUtils.getDate(strDate);

                // Full Transaction
                Transaction a = new Transaction(date, acc_id, expenseType, amount);
                // Add to return list
                array_list.add(a);
                // Move to next item in selection
                res.moveToNext();
            }
    } /* else {
        // EMPTY: return empty list if there are no transactions
    } */
        db.close();
        return array_list;
    }
}

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Yasas on 12/3/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteDatabase db;

    public PersistentTransactionDAO(SQLiteDatabase db) {
        this.db = db;
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
        ContentValues contentValues = new ContentValues();
        contentValues.put("acc_id", accountNo);
        contentValues.put("amount", amount);
        contentValues.put("date", date.toString());
        contentValues.put("is_expense", expenseType == ExpenseType.EXPENSE);
        db.insert("transaction_", null, contentValues);
    }

    /**
     * Return all the transactions logged.
     *
     * @return - a list of all the transactions
     */
    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> array_list = new ArrayList<Transaction>();

        Cursor res = db.rawQuery("select * from transaction_ order by transaction_id desc", null);

        if (res.moveToFirst()) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            while (res.isAfterLast() == false) {
                String acc_id = res.getString(res.getColumnIndex("acc_id"));
                int is_expense = res.getInt(res.getColumnIndex("is_expense"));
                ExpenseType expenseType;
                if (is_expense == 1) {
                    expenseType = ExpenseType.EXPENSE;
                } else {
                    expenseType = ExpenseType.INCOME;
                }
                Double amount = res.getDouble(res.getColumnIndex("amount"));
                String strDate = res.getString(res.getColumnIndex("date"));
                Date date = null;
                try {
                    date = formatter.parse(strDate);
                } catch (ParseException e) {

                }
                Transaction a = new Transaction(date, acc_id, expenseType, amount);
                array_list.add(a);
                res.moveToNext();
            }
        } else {
            // EMPTY: return empty list if there are no transactions
        }
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
        ArrayList<Transaction> array_list = new ArrayList<Transaction>();

        Cursor res = db.rawQuery("select * from transaction_ order by transaction_id desc limit " + limit, null);

        if (res.moveToFirst()) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            while (!res.isAfterLast()) {
                String acc_id = res.getString(res.getColumnIndex("acc_id"));
                int is_expense = res.getInt(res.getColumnIndex("is_expense"));
                ExpenseType expenseType;
                if (is_expense == 1) {
                    expenseType = ExpenseType.EXPENSE;
                } else {
                    expenseType = ExpenseType.INCOME;
                }
                Double amount = res.getDouble(res.getColumnIndex("amount"));
                String strDate = res.getString(res.getColumnIndex("date"));
                Date date = null;
                try {
                    date = formatter.parse(strDate);
                } catch (ParseException e) {
                    // EMPTY: return empty list if there are no transactions
                }
                Transaction a = new Transaction(date, acc_id, expenseType, amount);
                array_list.add(a);
                res.moveToNext();
            }
    } else {
        // EMPTY: return empty list if there are no transactions
    }
        return array_list;
    }
}

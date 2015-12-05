package lk.ac.mrt.cse.dbs.simpleexpensemanager.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Yasas on 12/5/2015.
 */
public class ExpenseUtils {
    private static SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

    /**
     * Get the expense type as integer ans return Enum value
     * @param expenseTypeInt Expense type defined by an int(Stored in database)
     * @return ExpenseType (Enum): if 1 (Expense) else (Income)
     */
    public static ExpenseType getExpenseType(int expenseTypeInt) {
        if (expenseTypeInt == 1) {
            return ExpenseType.EXPENSE;
        } else {
            return ExpenseType.INCOME;
        }
    }

    public static Date getDate(String strDate) {
        try {
            return formatter.parse(strDate);
        } catch (ParseException e) {
            // EMPTY: return empty list if there are no transactions
            return null;
        }
    }
}

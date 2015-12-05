package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

/**
 * Created by Yasas Senarath (130556L)
 * For DB Project
 */
public class ExpenseMetaData {
    public static final String DATABASE_NAME = "130556L.db";
    public static final int DATABASE_VERSION = 1;


    /**
     * Metadata on Account Table
     */
    public static final String ACCOUNT_TABLE_NAME = "account";
    public static final String ACCOUNT_COLUMN_ACCOUNT_NO = "acc_no";
    public static final String ACCOUNT_COLUMN_BANK = "bank";
    public static final String ACCOUNT_COLUMN_HOLDER = "acc_holder";
    public static final String ACCOUNT_COLUMN_BALANCE = "balance";

    /**
     * Metadata on Transaction Table
     */
    public static final String TRANSACTION_TABLE_NAME = "_transaction";
    public static final String TRANSACTION_COLUMN_TRANSACTION_ID = "id";
    public static final String TRANSACTION_COLUMN_ACCOUNT_NO = "acc_no";
    public static final String TRANSACTION_COLUMN_EXPENSE_TYPE = "expense_type";
    public static final String TRANSACTION_COLUMN_AMOUNT = "amount";
    public static final String TRANSACTION_COLUMN_DATE = "date";
}

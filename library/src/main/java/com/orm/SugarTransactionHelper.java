package com.orm;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SugarTransactionHelper {

    public static void transaction(SugarTransactionHelper.Callback callback) {
        SQLiteDatabase database = SugarContext.getSugarContext().getSugarDb().getDB();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (database) {
            database.beginTransaction();

            try {
                Log.d(SugarTransactionHelper.class.getSimpleName(),
                        "Callback executing within transaction");
                callback.manipulateInTransaction(database);
                database.setTransactionSuccessful();
                Log.d(SugarTransactionHelper.class.getSimpleName(),
                        "Callback successfully executed within transaction");
            } catch (Throwable e) {
                Log.e(SugarTransactionHelper.class.getSimpleName(),
                        "Could execute callback within transaction", e);
                throw new RuntimeException(e);
            } finally {
                database.endTransaction();
            }
        }
    }

    public interface Callback {
        void manipulateInTransaction(SQLiteDatabase db);
    }
}

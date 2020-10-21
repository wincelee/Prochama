package manu.apps.prochama.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import manu.apps.prochama.classes.Transactions;
import manu.apps.prochama.daos.TransactionsDao;


@Database(entities = {Transactions.class}, version = 1)
public abstract class TransactionsDb extends RoomDatabase {

    private static TransactionsDb transactionsDbInstance;

    public abstract TransactionsDao transactionDao();

    public static synchronized TransactionsDb getTransactionDbInstance(Context context){
        if (transactionsDbInstance == null){
            transactionsDbInstance = Room.databaseBuilder(context.getApplicationContext(),
                    TransactionsDb.class,"transactions_table")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return transactionsDbInstance;
    }

    private static Callback roomCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(transactionsDbInstance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private TransactionsDao transactionsDao;

        private PopulateDbAsyncTask(TransactionsDb transactionsDb){
            transactionsDao = transactionsDb.transactionDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

}

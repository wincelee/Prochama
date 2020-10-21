package manu.apps.prochama.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import manu.apps.prochama.classes.Transactions;
import manu.apps.prochama.daos.TransactionsDao;
import manu.apps.prochama.database.TransactionsDb;

public class TransactionsRepository {

    private TransactionsDao transactionsDao;
    private LiveData<List<Transactions>> allTransactions;

    public TransactionsRepository(Application application) {
        TransactionsDb tDb = TransactionsDb.getTransactionDbInstance(application);
        transactionsDao = tDb.transactionDao();
        allTransactions = transactionsDao.getAllTransactions();
    }

    public void insert(Transactions transactions){
        new InsertTransactionAsyncTask(transactionsDao).execute(transactions);
    }

    public void update(Transactions transactions){
        new UpdateTransactionAsyncTask(transactionsDao).execute(transactions);
    }

    public void delete(Transactions transactions){
        new DeleteTransactionAsyncTask(transactionsDao).execute(transactions);
    }

    public void deleteAllTransactions(){
        new DeleteAllTransactionsAsyncTask(transactionsDao).execute();
    }

    public LiveData<List<Transactions>> getAllTransactions(){
        return allTransactions;
    }

    private static class InsertTransactionAsyncTask extends AsyncTask<Transactions, Void, Void> {

        private TransactionsDao transactionsDao;

        public InsertTransactionAsyncTask(TransactionsDao transactionsDao) {
            this.transactionsDao = transactionsDao;
        }

        @Override
        protected Void doInBackground(Transactions... transactions) {
            transactionsDao.insert(transactions[0]);
            return null;
        }
    }

    private static class UpdateTransactionAsyncTask extends AsyncTask<Transactions, Void, Void>{

        private TransactionsDao transactionsDao;

        public UpdateTransactionAsyncTask(TransactionsDao transactionsDao) {
            this.transactionsDao = transactionsDao;
        }

        @Override
        protected Void doInBackground(Transactions... transactions) {
            transactionsDao.update(transactions[0]);
            return null;
        }
    }

    private static class DeleteTransactionAsyncTask extends AsyncTask<Transactions, Void, Void>{

        private TransactionsDao transactionsDao;

        public DeleteTransactionAsyncTask(TransactionsDao transactionsDao) {
            this.transactionsDao = transactionsDao;
        }

        @Override
        protected Void doInBackground(Transactions... transactions) {
            transactionsDao.delete(transactions[0]);
            return null;
        }
    }

    private static class DeleteAllTransactionsAsyncTask extends AsyncTask<Void, Void, Void>{

        private TransactionsDao transactionsDao;

        public DeleteAllTransactionsAsyncTask(TransactionsDao transactionsDao) {
            this.transactionsDao = transactionsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            transactionsDao.deleteAllTransactions();
            return null;
        }
    }
}

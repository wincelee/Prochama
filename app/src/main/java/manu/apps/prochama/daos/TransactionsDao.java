package manu.apps.prochama.daos;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import manu.apps.prochama.classes.Transactions;


@Dao
public interface TransactionsDao {

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    void insert(Transactions transactions);

    @Update
    void update(Transactions transactions);

    @Delete
    void delete(Transactions transactions);

    @Query("DELETE FROM transactions_table")
    void deleteAllTransactions();

    @Query("SELECT * FROM transactions_table")
    LiveData<List<Transactions>> getAllTransactions();

}

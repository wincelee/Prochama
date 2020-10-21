package manu.apps.prochama.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import manu.apps.prochama.classes.Transactions;
import manu.apps.prochama.repositories.TransactionsRepository;

public class TransactionsViewModel extends AndroidViewModel {

    private TransactionsRepository tRepository;
    private LiveData<List<Transactions>> allTransactions;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);
        tRepository = new TransactionsRepository(application);
        allTransactions = tRepository.getAllTransactions();
    }

    public void insert(Transactions transactions){
        tRepository.insert(transactions);
    }

    public void update(Transactions transactions){
        tRepository.update(transactions);
    }

    public void delete(Transactions transactions){
        tRepository.delete(transactions);
    }

    public void deleteAllTransactions(){
        tRepository.deleteAllTransactions();
    }

    public LiveData<List<Transactions>> getAllTransactions(){
        return allTransactions;
    }

    public String getTransactionType(){

        String transactionType  = "";

        for(Transactions myTransaction:allTransactions.getValue()){
            transactionType = myTransaction.getTransactionType();
        }

        return transactionType;

    }


    public double getTotalAmount(){

        double totalAmount = 0;
        for(Transactions myTransaction:allTransactions.getValue()){
            totalAmount += myTransaction.getAmount() ;
        }
        return totalAmount;
    }

    /*public List<Map<String, Object>> getMapItems(){

        List<Map<String, Object>> allItems = new ArrayList<>();
        for(Transactions myTransaction:allTransactions.getValue()){
            Map<String, Object> myItem = new HashMap<>();
            myItem.put("transactionCode",myTransaction.getTransactionId());
            myItem.put("amount",myTransaction.getAmount());
            allItems.add(myItem);
        }

        return allItems;
    }*/

}
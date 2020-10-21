package manu.apps.prochama.classes;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "transactions_table")
public class Transactions {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    int transactionId;

    @ColumnInfo(name = "transaction_type")
    String transactionType;

    @ColumnInfo(name = "amount")
    double amount;

    public Transactions() {

    }

    public Transactions(int transactionId, String transactionType, double amount) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

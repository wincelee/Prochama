package manu.apps.prochama.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import manu.apps.prochama.R;
import manu.apps.prochama.classes.Config;
import manu.apps.prochama.classes.Transactions;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>{

    private Context context;
    private List<Transactions> transactionsList = new ArrayList<>();

    OnClick onClick;

    public interface OnClick {

        void onEvent(Transactions transactions, int pos);

    }

    public TransactionsAdapter(Context context, OnClick onClick) {
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transactions, parent, false);
        return new TransactionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, final int position) {

        final Transactions currentTransaction = transactionsList.get(position);

        holder.tvTransactionType.setText(currentTransaction.getTransactionType());

        holder.tvAmount.setText ("Ksh"+" "+ Config.numberFormatter(currentTransaction.getAmount()));

        holder.llTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClick.onEvent(currentTransaction, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return transactionsList.size();
    }


    public void setTransactions(List<Transactions> transactions){
        this.transactionsList = transactions;
        notifyDataSetChanged();
    }

    public Transactions getTransactionAt(int position){
        return transactionsList.get(position);
    }

    public static class TransactionsViewHolder extends RecyclerView.ViewHolder{

        TextView tvTransactionType,tvAmount;
        LinearLayout llTransactions;

        public TransactionsViewHolder(View itemView){
            super(itemView);

            tvTransactionType = itemView.findViewById(R.id.tv_transaction_type);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            llTransactions = itemView.findViewById(R.id.ll_transactions);

        }
    }
}

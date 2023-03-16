package ru.ivanmurzin.debts;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.ivanmurzin.debts.database.room.DebtDAO;
import ru.ivanmurzin.debts.database.room.DebtEntity;
import ru.ivanmurzin.debts.databinding.ItemDebtBinding;

public class MyDebtsAdapter extends RecyclerView.Adapter<MyDebtsAdapter.MyHolder> {

    List<DebtEntity> data;
    public MyDebtsAdapter(List<DebtEntity> data) {
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDebtBinding binding = ItemDebtBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(data.get(position).name);
        holder.money.setText(String.valueOf(data.get(position).money));
        holder.itemView.setOnLongClickListener(view->{
            DebtDAO debtDAO = App.getDatabase().debtDAO();
            new Thread(() -> {
                debtDAO.clear(holder.itemView.getId());
            }).start();
            return true;
        });
        holder.itemView.setOnClickListener(view->{
            new MainActivity().DialogCreate(holder);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView money;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            money = itemView.findViewById(R.id.money);
        }
    }
}

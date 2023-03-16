package ru.ivanmurzin.debts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.ivanmurzin.debts.database.room.DebtDAO;
import ru.ivanmurzin.debts.database.room.DebtEntity;
import ru.ivanmurzin.debts.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DebtDAO debtDAO = App.getDatabase().debtDAO();
        context = MainActivity.this;

        List<DebtEntity> data = new ArrayList<>();
        new Thread(() -> {
            List<DebtEntity> debts = debtDAO.getAll();
            runOnUiThread(() -> {
                data.addAll(debts);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                binding.recyclerView.setAdapter(new MyDebtsAdapter(data));
            });
        }).start();

        binding.add.setOnClickListener(view -> {
            DebtEntity debt = new DebtEntity("123", (int) (Math.random() * 1000));
            data.add(debt);
            new Thread(() -> {
                debtDAO.save(debt);
            }).start();
            binding.recyclerView.getAdapter().notifyItemChanged(data.size() - 1);
        });
    }

    public void DialogCreate(MyDebtsAdapter.MyHolder holder) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert);
        final EditText editTextName = new EditText(context);
        final EditText editTextMoney = new EditText(context);
        dialog.setCancelable(false);
        dialog.setView(editTextName);
        dialog.setView(editTextMoney);
        dialog.setPositiveButton(
                "Ок",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DebtDAO debtDAO = App.getDatabase().debtDAO();
                        new Thread(() -> {
                            Log.d("SSS", "))))))");
                            debtDAO.updateItem(holder.itemView.getId(), editTextName.getText().toString(), Integer.parseInt(editTextMoney.getText().toString()));
                        }).start();
                        dialog.cancel();
                    }
                });
        dialog.setNegativeButton(
                "Отмена",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dialog.create().show();
    }
}


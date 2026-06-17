package com.example.a1221858_1220137_courseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a1221858_1220137_courseproject.R;
import com.example.a1221858_1220137_courseproject.DataBaseHelper;
import com.example.a1221858_1220137_courseproject.User;
import java.util.ArrayList;
import java.util.List;

public class AdminUsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private DataBaseHelper dbHelper;
    private List<User> usersList = new ArrayList<>();
    private AdminUserAdapter adapter;

    public AdminUsersFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);
        recyclerView = view.findViewById(R.id.rv_admin_users);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new DataBaseHelper(getContext());

        refreshList();
        return view;
    }

    private void refreshList() {
        usersList.clear();
        usersList.addAll(dbHelper.getAllUsers());
        if (adapter == null) {
            adapter = new AdminUserAdapter();
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserHolder> {

        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user, parent, false);
            return new UserHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position) {
            User u = usersList.get(position);
            holder.tvName.setText(u.getFirstName() + " " + u.getLastName());
            holder.tvEmail.setText(u.getEmail());

            holder.btnDelete.setOnClickListener(v -> {
                dbHelper.deleteUser(u.getId());
                Toast.makeText(getContext(), "Account access revoked", Toast.LENGTH_SHORT).show();
                refreshList();
            });
        }

        @Override
        public int getItemCount() { return usersList.size(); }

        class UserHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvEmail;
            Button btnDelete;
            UserHolder(View iv) {
                super(iv);
                tvName = iv.findViewById(R.id.user_item_name);
                tvEmail = iv.findViewById(R.id.user_item_email);
                btnDelete = iv.findViewById(R.id.btn_user_delete);
            }
        }
    }
}
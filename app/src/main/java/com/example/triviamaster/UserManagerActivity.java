package com.example.triviamaster;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.triviamaster.adapters.AvatarAdapter;
import com.example.triviamaster.adapters.UserAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.DuplicateRecordException;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityUserManagerBinding;
import com.example.triviamaster.databinding.DialogUserFormBinding;
import com.example.triviamaster.models.User;
import java.util.ArrayList;
import java.util.List;

public final class UserManagerActivity extends AppCompatActivity {

    private ActivityUserManagerBinding binding;
    private PreferencesManager prefsManager;
    private UserRepository userRepository;
    private final List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter;
    private String currentSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefsManager = new PreferencesManager(this);
        if (!prefsManager.isCurrentUserAdmin()) {
            finish();
            return;
        }
        binding = ActivityUserManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userRepository = new UserRepository(this);
        setupRecycler();
        setupListeners();
        loadUsers();
    }

    private void setupRecycler() {
        userAdapter = new UserAdapter(userList, prefsManager, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEditUser(User user) {
                showUserFormDialog(user);
            }

            @Override
            public void onDeleteUser(User user) {
                confirmDeleteUser(user);
            }
        });
        binding.rvUsersList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUsersList.setAdapter(userAdapter);
    }

    private void setupListeners() {
        binding.btnUserManagerBack.setOnClickListener(v -> finish());
        binding.btnAddUser.setOnClickListener(v -> showUserFormDialog(null));
        binding.etSearchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearch = s != null ? s.toString() : "";
                loadUsers();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadUsers() {
        binding.progressUsers.setVisibility(View.VISIBLE);
        userRepository.getAllUsers(currentSearch, new DbCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                if (isFinishing()) {
                    return;
                }
                binding.progressUsers.setVisibility(View.GONE);
                userList.clear();
                if (result != null) {
                    userList.addAll(result);
                }
                userAdapter.notifyDataSetChanged();
                binding.tvEmptyUsers.setVisibility(userList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                binding.progressUsers.setVisibility(View.GONE);
                Toast.makeText(UserManagerActivity.this, getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUserFormDialog(User existingUser) {
        DialogUserFormBinding formBinding = DialogUserFormBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(formBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        boolean isEdit = existingUser != null;
        formBinding.tvUserFormTitle.setText(getString(
                isEdit ? R.string.um_dialog_edit_title : R.string.um_dialog_create_title));

        int initialAvatarId = isEdit ? existingUser.getAvatarId() : 1;
        AvatarAdapter formAvatarAdapter = new AvatarAdapter(
                PreferencesManager.getAvailableAvatars(), initialAvatarId, avatar -> {
        });
        formBinding.rvFormAvatars.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        formBinding.rvFormAvatars.setAdapter(formAvatarAdapter);

        if (isEdit) {
            formBinding.etFormUsername.setText(existingUser.getUsername());
            formBinding.etFormEmail.setText(existingUser.getEmail());
            formBinding.etFormPassword.setText(existingUser.getPassword());
        }

        formBinding.btnFormUserCancel.setOnClickListener(v -> dialog.dismiss());
        formBinding.btnFormUserSave.setOnClickListener(v -> {
            String username = formBinding.etFormUsername.getText() != null
                    ? formBinding.etFormUsername.getText().toString().trim() : "";
            String email = formBinding.etFormEmail.getText() != null
                    ? formBinding.etFormEmail.getText().toString().trim() : "";
            String password = formBinding.etFormPassword.getText() != null
                    ? formBinding.etFormPassword.getText().toString() : "";

            boolean valid = true;
            if (username.isEmpty()) {
                formBinding.tilFormUsername.setError(getString(R.string.error_username_required));
                valid = false;
            } else {
                formBinding.tilFormUsername.setError(null);
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                formBinding.tilFormEmail.setError(getString(R.string.error_email_invalid));
                valid = false;
            } else {
                formBinding.tilFormEmail.setError(null);
            }

            if (password.length() < 4) {
                formBinding.tilFormPassword.setError(getString(R.string.error_password_short));
                valid = false;
            } else {
                formBinding.tilFormPassword.setError(null);
            }

            if (!valid) {
                return;
            }

            int selectedAvatarId = formAvatarAdapter.getSelectedAvatar().getId();
            formBinding.btnFormUserSave.setEnabled(false);

            if (isEdit) {
                existingUser.setUsername(username);
                existingUser.setEmail(email);
                existingUser.setPassword(password);
                existingUser.setAvatarId(selectedAvatarId);
                userRepository.updateUser(existingUser, new DbCallback<User>() {
                    @Override
                    public void onSuccess(User updated) {
                        if (isFinishing()) {
                            return;
                        }
                        if (prefsManager.getCurrentUserId() == updated.getId()) {
                            prefsManager.syncFromUser(updated);
                        }
                        dialog.dismiss();
                        Toast.makeText(UserManagerActivity.this,
                                getString(R.string.um_updated_success), Toast.LENGTH_SHORT).show();
                        loadUsers();
                    }

                    @Override
                    public void onError(Exception error) {
                        if (isFinishing()) {
                            return;
                        }
                        formBinding.btnFormUserSave.setEnabled(true);
                        if (error instanceof DuplicateRecordException) {
                            formBinding.tilFormUsername.setError(getString(R.string.error_username_exists));
                        } else {
                            Toast.makeText(UserManagerActivity.this,
                                    getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                userRepository.registerUser(username, password, email, selectedAvatarId, new DbCallback<User>() {
                    @Override
                    public void onSuccess(User created) {
                        if (isFinishing()) {
                            return;
                        }
                        dialog.dismiss();
                        Toast.makeText(UserManagerActivity.this,
                                getString(R.string.um_created_success), Toast.LENGTH_SHORT).show();
                        loadUsers();
                    }

                    @Override
                    public void onError(Exception error) {
                        if (isFinishing()) {
                            return;
                        }
                        formBinding.btnFormUserSave.setEnabled(true);
                        if (error instanceof DuplicateRecordException) {
                            formBinding.tilFormUsername.setError(getString(R.string.error_username_exists));
                        } else {
                            Toast.makeText(UserManagerActivity.this,
                                    getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void confirmDeleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.um_delete_confirm_title))
                .setMessage(getString(R.string.um_delete_confirm_msg, user.getUsername()))
                .setPositiveButton(getString(R.string.btn_confirm_delete), (dialog, which) ->
                        userRepository.deleteUser(user.getId(), new DbCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean deleted) {
                                if (isFinishing()) {
                                    return;
                                }
                                Toast.makeText(UserManagerActivity.this,
                                        getString(R.string.um_deleted_success), Toast.LENGTH_SHORT).show();
                                if (prefsManager.getCurrentUserId() == user.getId()) {
                                    prefsManager.clearSession();
                                    Intent intent = new Intent(UserManagerActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                                loadUsers();
                            }

                            @Override
                            public void onError(Exception error) {
                                if (isFinishing()) {
                                    return;
                                }
                                Toast.makeText(UserManagerActivity.this,
                                        getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                            }
                        }))
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show();
    }
}

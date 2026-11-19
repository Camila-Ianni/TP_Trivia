package com.example.triviamaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.triviamaster.adapters.AvatarAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.DuplicateRecordException;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityRegisterBinding;
import com.example.triviamaster.models.Avatar;
import com.example.triviamaster.models.User;
import com.example.triviamaster.utils.CountryHelper;

public final class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private PreferencesManager prefsManager;
    private UserRepository userRepository;
    private AvatarAdapter avatarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
        userRepository = new UserRepository(this);
        setupCountrySpinner();
        setupAvatarList();
        setupListeners();
    }

    private void setupCountrySpinner() {
        ArrayAdapter<CountryHelper.CountryOption> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                CountryHelper.getCountries());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spRegCountry.setAdapter(adapter);
    }

    private void setupAvatarList() {
        avatarAdapter = new AvatarAdapter(PreferencesManager.getAvailableAvatars(), 1, avatar -> {
        });
        binding.rvRegAvatars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvRegAvatars.setAdapter(avatarAdapter);
    }

    private void setupListeners() {
        binding.btnRegisterBack.setOnClickListener(v -> finish());
        binding.btnAlreadyAccount.setOnClickListener(v -> finish());
        binding.btnRegisterSubmit.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String username = binding.etRegUsername.getText() != null
                ? binding.etRegUsername.getText().toString().trim() : "";
        String email = binding.etRegEmail.getText() != null
                ? binding.etRegEmail.getText().toString().trim() : "";
        String password = binding.etRegPassword.getText() != null
                ? binding.etRegPassword.getText().toString() : "";
        String confirm = binding.etRegConfirmPassword.getText() != null
                ? binding.etRegConfirmPassword.getText().toString() : "";

        boolean valid = true;
        if (username.isEmpty()) {
            binding.tilRegUsername.setError(getString(R.string.error_username_required));
            valid = false;
        } else {
            binding.tilRegUsername.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilRegEmail.setError(getString(R.string.error_email_invalid));
            valid = false;
        } else {
            binding.tilRegEmail.setError(null);
        }

        if (password.length() < 4) {
            binding.tilRegPassword.setError(getString(R.string.error_password_short));
            valid = false;
        } else {
            binding.tilRegPassword.setError(null);
        }

        if (!confirm.equals(password)) {
            binding.tilRegConfirmPassword.setError(getString(R.string.error_password_mismatch));
            valid = false;
        } else {
            binding.tilRegConfirmPassword.setError(null);
        }

        if (!valid) {
            return;
        }

        Avatar selected = avatarAdapter.getSelectedAvatar();
        CountryHelper.CountryOption selectedCountry =
                (CountryHelper.CountryOption) binding.spRegCountry.getSelectedItem();
        String countryCode = selectedCountry != null ? selectedCountry.getCode() : "AR";
        String selectedRole = binding.rbRoleTeacher.isChecked() ? "TEACHER" : "USER";

        setLoading(true);
        userRepository.registerUser(username, password, email, selected.getId(), countryCode, selectedRole, new DbCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (isFinishing()) {
                    return;
                }
                setLoading(false);
                prefsManager.syncFromUser(user);
                CountryHelper.applyLocaleForCountry(user.getCountry());
                Toast.makeText(RegisterActivity.this,
                        getString(R.string.msg_register_success, user.getUsername()),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MapActivity.class);
                intent.putExtra(MainActivity.EXTRA_USER_NAME, user.getUsername());
                intent.putExtra(MainActivity.EXTRA_AVATAR_ID, selected.getId());
                intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, selected.getIconResId());
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                setLoading(false);
                if (error instanceof DuplicateRecordException) {
                    binding.tilRegUsername.setError(getString(R.string.error_username_exists));
                    binding.etRegUsername.requestFocus();
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setLoading(boolean loading) {
        binding.progressRegister.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnRegisterSubmit.setEnabled(!loading);
    }
}

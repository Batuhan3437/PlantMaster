package com.erenbasar.plantmaster.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.erenbasar.plantmaster.R;
import com.erenbasar.plantmaster.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textView;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mAuth = FirebaseAuth.getInstance();

        // Butonlara tıklama olayları bağlandı
        binding.loginButton.setOnClickListener(this::loginClicked);
        binding.signupButton.setOnClickListener(this::signupClicked);

        return root;
    }
    public void loginClicked (View view){

        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();

        // Email ve şifre boş mu kontrolü
        if (email.isEmpty()) {
            binding.emailInput.setError("Email is required!");
            return;
        }
        if (password.isEmpty()) {
            binding.passwordInput.setError("Password is required!");
            return;
        }

        // Şifre uzunluğunu kontrol
        if (password.length() < 6) {
            binding.passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        // Firebase ile kullanıcı giriş işlemi
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Giriş başarılı, ProfileDetailFragment'e yönlendir
                    navigateToProfileDetail();
                })
                .addOnFailureListener(e -> {
                    // Hata mesajını göster
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });
    }
    // ProfileDetailFragment'e yönlendirme metodu
    private void navigateToProfileDetail() {
        // Navigation işlemi
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.profileDetailFragment);
    }

    public void signupClicked (View view){

        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();

        if (email.isEmpty()) {
            binding.emailInput.setError("Email is required!");
            return;
        }
        if (password.isEmpty()) {
            binding.passwordInput.setError("Password is required!");
            return;
        }

        if (password.length() < 6) {
            binding.passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        // Firebase ile kullanıcı oluşturma
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // Başarılı olduğunda yapılacak işlemler
                Toast.makeText(getActivity(), "Sign Up Successful!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Başarısız olduğunda yapılacak işlemler
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


//        if(email.isEmpty() || password.isEmpty()){
//            // getActivity() ile Fragment in bağlı olduğu Activity nin Context ini alıyoruz
//            Toast.makeText(getActivity(),"Enter email and password",Toast.LENGTH_LONG).show();
//        }else {
//            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
//                    // Başarılı olduğunda yapılacak işlemler
//                    Toast.makeText(getActivity(), "Sign Up Successful!", Toast.LENGTH_LONG).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    // Başarısız olduğunda yapılacak işlemler
//                    Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//                }
//            });
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
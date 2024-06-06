package com.litcove.litcove.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.litcove.litcove.databinding.FragmentProfileBinding
import com.litcove.litcove.ui.authentication.LoginActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val imageProfile = binding.imageProfile
        profileViewModel.imageProfile.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it)
                .into(imageProfile)
        }

        val textName: TextView = binding.textName
        profileViewModel.textName.observe(viewLifecycleOwner) {
            textName.text = it
        }

        val buttonLogout = binding.buttonLogout
        buttonLogout.setOnClickListener {
            logout()
        }

        profileViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(requireContext())
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }
    }
}
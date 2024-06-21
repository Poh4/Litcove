package com.litcove.litcove.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth
import com.litcove.litcove.R
import com.litcove.litcove.databinding.FragmentProfileBinding
import com.litcove.litcove.ui.authentication.ChooseInterestsActivity
import com.litcove.litcove.ui.authentication.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        profileViewModel.loadThemeSetting()

        val imageAvatar = binding.imageAvatar
        profileViewModel.imageAvatar.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(R.drawable.ic_no_profile)
                    .into(imageAvatar)
            } else {
                Glide.with(this)
                    .load(imageUrl)
                    .into(imageAvatar)
            }
        }

        val textName: TextView = binding.textName
        profileViewModel.textName.observe(viewLifecycleOwner) {
            textName.text = it
        }

        val textJoinedSince: TextView = binding.textJoinedSince
        profileViewModel.textJoinedSince.observe(viewLifecycleOwner) { joinedSince ->
            "${getString(R.string.joined_since)} $joinedSince".also { textJoinedSince.text = it }
        }

        val buttonEditProfile: MaterialButton = binding.buttonEditProfile
        buttonEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        val buttonChangePassword: MaterialButton = binding.buttonChangePassword
        buttonChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }

        val buttonForgotPassword: MaterialButton = binding.buttonForgotPassword
        buttonForgotPassword.setOnClickListener {
            startActivity(Intent(requireContext(), ForgotPasswordActivity::class.java))
        }

        val buttonChangeInterests: MaterialButton = binding.buttonChangeInterests
        buttonChangeInterests.setOnClickListener {
            startActivity(Intent(requireContext(), ChooseInterestsActivity::class.java))
        }

        val switchTheme: MaterialSwitch = binding.switchTheme
        profileViewModel.isDarkMode.observe(viewLifecycleOwner) { isChecked ->
            if (isChecked != null) {
                switchTheme.isChecked = isChecked
            }
        }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            switchTheme.isChecked = isChecked
            profileViewModel.saveThemeSetting(switchTheme.isChecked)
        }

        val buttonChangeLanguage: MaterialButton = binding.buttonChangeLanguage
        buttonChangeLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        val buttonLogout = binding.buttonLogout
        buttonLogout.setOnClickListener {
            showLogoutDialog()
        }

        val buttonDeleteAccount = binding.buttonDeleteAccount
        buttonDeleteAccount.setOnClickListener {
            showDeleteAccountDialog()
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        profileViewModel.fetchUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> logout() }
            .setNegativeButton(getString(R.string.no), null)
            .setCancelable(true)
            .show()
    }

    private fun logout() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(requireContext())
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            profileViewModel.clearThemeSetting()
            Toast.makeText(context,
                getString(R.string.logged_out_successfully_theme_setting_cleared), Toast.LENGTH_LONG).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_account)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_your_account_this_action_cannot_be_undone))
            .setPositiveButton(R.string.yes) { _, _ -> deleteAccount() }
            .setNegativeButton(R.string.no, null)
            .setCancelable(true)
            .show()
    }

    private fun deleteAccount() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                profileViewModel.clearThemeSetting()
                Toast.makeText(context,
                    getString(R.string.account_deleted_successfully_theme_setting_cleared), Toast.LENGTH_LONG).show()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity?.finish()
            } else {
                Toast.makeText(context,
                    getString(R.string.failed_to_delete_account), Toast.LENGTH_LONG).show()
                Log.e("ProfileFragment", "Failed to delete account", task.exception)
            }
        }
    }
}
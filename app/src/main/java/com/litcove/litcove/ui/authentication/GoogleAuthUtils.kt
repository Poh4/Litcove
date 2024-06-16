package com.litcove.litcove.ui.authentication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.litcove.litcove.R
import kotlinx.coroutines.launch

object GoogleAuthUtils {

    fun registerWithGoogle(context: Context, auth: FirebaseAuth, lifecycleScope: LifecycleCoroutineScope, onAuthSuccess: (FirebaseUser?) -> Unit) {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                handleSignInResult(result, context, auth, onAuthSuccess)
            } catch (e: Exception) {
                Toast.makeText(context,
                    context.getString(R.string.error_registering_with_google), Toast.LENGTH_SHORT).show()
                Log.e("GoogleAuthUtils", "Error registering with Google: ${e.message}")
            }
        }
    }

    private fun handleSignInResult(result: GetCredentialResponse, context: Context, auth: FirebaseAuth, onAuthSuccess: (FirebaseUser?) -> Unit) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken, context, auth, onAuthSuccess)
                    } catch (e: GoogleIdTokenParsingException) {
                        Toast.makeText(context,
                            context.getString(R.string.error_parsing_google_id_token), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context,
                        context.getString(R.string.unexpected_type_of_credential), Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(context,
                    context.getString(R.string.unexpected_type_of_credential), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, context: Context, auth: FirebaseAuth, onAuthSuccess: (FirebaseUser?) -> Unit) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(context as AppCompatActivity) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context,
                        context.getString(R.string.authentication_successful), Toast.LENGTH_SHORT).show()
                    val user: FirebaseUser? = auth.currentUser
                    onAuthSuccess(user)
                } else {
                    Toast.makeText(context,
                        context.getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show()
                    onAuthSuccess(null)
                }
            }
    }
}
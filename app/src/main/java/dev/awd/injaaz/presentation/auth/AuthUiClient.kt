package dev.awd.injaaz.presentation.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.awd.injaaz.R
import dev.awd.injaaz.domain.models.User
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

interface AuthUiClient {
    fun getSignedInUser(): User?
    suspend fun signOut()
    suspend fun signIn(): IntentSender?
    suspend fun getSignInResultFromIntent(intent: Intent): SignInResult
    suspend fun signInWithIdToken(googleIdToken: String): SignInResult
}

class GoogleAuthUiClient @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthUiClient {

    private val oneTapClient: SignInClient = Identity.getSignInClient(context)
    private val auth = Firebase.auth

    override suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    private fun buildSignInRequest(): BeginSignInRequest =
        BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            ).setAutoSelectEnabled(true)
            .build()

    override suspend fun getSignInResultFromIntent(intent: Intent): SignInResult {
        val credentials = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credentials.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials)
                .await().user
            SignInResult(user = user?.toUserData())
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(errorMessage = e.message)
        }
    }

    override suspend fun signInWithIdToken(googleIdToken: String): SignInResult {
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials)
                .await().user
            SignInResult(user = user?.toUserData())
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(errorMessage = e.message)
        }
    }

    override fun getSignedInUser(): User? =
        auth.currentUser?.toUserData()

    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

}

data class SignInResult(
    val user: User? = null,
    val errorMessage: String? = null
)

fun FirebaseUser.toUserData(): User =
    User(
        userId = uid,
        userName = displayName,
        profilePhotoUrl = photoUrl.toString(),
        email = email,
    )
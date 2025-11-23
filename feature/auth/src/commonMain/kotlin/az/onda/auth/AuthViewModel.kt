package az.onda.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.onda.data.api.model.OtpChannel
import az.onda.data.auth.AuthRepository
import az.onda.shared.util.RequestState
import com.mmk.kmpauth.google.GoogleUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val otpSent: Boolean = false,
    val phoneNumber: String = "",
    val selectedChannel: OtpChannel = OtpChannel.SMS,
    val error: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun signInWithGoogle(
        user: GoogleUser,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = uiState.copy(isLoading = true, error = null)
            val idToken = user.idToken
            when (val result = authRepository.signInWithGoogle(idToken)) {
                is RequestState.Success -> {
                    uiState = uiState.copy(isLoading = false)
                    onSuccess()
                }
                is RequestState.Error -> {
                    uiState = uiState.copy(isLoading = false, error = result.message)
                    onError(result.message)
                }
                else -> Unit
            }
        }
    }

    fun signInWithApple(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = uiState.copy(isLoading = true, error = null)
            when (val result = authRepository.signInWithApple(idToken)) {
                is RequestState.Success -> {
                    uiState = uiState.copy(isLoading = false)
                    onSuccess()
                }
                is RequestState.Error -> {
                    uiState = uiState.copy(isLoading = false, error = result.message)
                    onError(result.message)
                }
                else -> Unit
            }
        }
    }

    fun sendOtp(
        phone: String,
        channel: OtpChannel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = uiState.copy(
                isLoading = true,
                error = null,
                phoneNumber = phone,
                selectedChannel = channel
            )
            when (val result = authRepository.sendOtp(phone, channel)) {
                is RequestState.Success -> {
                    uiState = uiState.copy(isLoading = false, otpSent = true)
                    onSuccess()
                }
                is RequestState.Error -> {
                    uiState = uiState.copy(isLoading = false, error = result.message)
                    onError(result.message)
                }
                else -> Unit
            }
        }
    }

    fun verifyOtp(
        code: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = uiState.copy(isLoading = true, error = null)
            when (val result = authRepository.verifyOtp(uiState.phoneNumber, code)) {
                is RequestState.Success -> {
                    uiState = uiState.copy(isLoading = false, otpSent = false)
                    onSuccess()
                }
                is RequestState.Error -> {
                    uiState = uiState.copy(isLoading = false, error = result.message)
                    onError(result.message)
                }
                else -> Unit
            }
        }
    }

    fun resetOtpState() {
        uiState = uiState.copy(otpSent = false, phoneNumber = "", error = null)
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }
}
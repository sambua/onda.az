package az.onda.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.onda.data.domain.CustomerRepository
import com.mmk.kmpauth.google.GoogleUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class AuthViewModel(
    private val customerRepository: CustomerRepository
): ViewModel() {
    fun createCustomer(
        user: GoogleUser,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.createCustomer(
                user = user,
                onSuccess = onSuccess,
                onError = onError,
            )
        }
    }
}
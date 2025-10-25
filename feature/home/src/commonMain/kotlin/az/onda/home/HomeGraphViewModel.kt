package az.onda.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.onda.data.domain.CustomerRepository
import az.onda.shared.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeGraphViewModel(
    private val customerRepo: CustomerRepository
): ViewModel() {
    fun signOut(
        onError: (String) -> Unit,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                customerRepo.signOut()
            }
            if (result.isSuccess()) {
                onSuccess()
            } else if (result is RequestState.Error) {
                onError(result.getErrorMessage())
            }
        }
    }
}
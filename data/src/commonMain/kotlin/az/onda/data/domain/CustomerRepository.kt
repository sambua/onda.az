package az.onda.data.domain

import az.onda.shared.util.RequestState
import com.mmk.kmpauth.google.GoogleUser

interface CustomerRepository {
    fun getCurrentUserID(): String?
    suspend fun createCustomer(
        user: GoogleUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
    suspend fun signOut(): RequestState<Unit>
}
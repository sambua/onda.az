package az.onda.data

import az.onda.data.domain.CustomerRepository
import az.onda.shared.domain.Customer
import az.onda.shared.util.RequestState
import com.mmk.kmpauth.google.GoogleUser

class CustomerRepositoryImpl: CustomerRepository {
    override fun getCurrentUserID(): String? {
        // TODO("Not yet implemented")
        // should return the current logged in user's ID
        return null
    }
    override suspend fun createCustomer(
        user: GoogleUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (user != null) {
                val customer = Customer(
                    id = user.idToken,
                    firstName = user.displayName.split(" ").firstOrNull() ?: "Unknown",
                    lastName = user.displayName.split(" ").lastOrNull() ?: "Unknown",
                    email = user.email ?: "",
                    image = user.profilePicUrl ?: "",
                    accessToken = user.accessToken ?: ""
                )
                // TODO("Not yet implemented")
                val customerExists = false
                if (!customerExists) {
                    onSuccess()
                } else {
                   // customerCollection.createAsync(customer.id, customer)
                    onSuccess()
                }
            } else {
                onError("GoogleUser is null")
            }
        } catch (e: Exception) {
            onError("Error while creating a new Customer: ${e.message ?: "Unknown error"}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            // TODO("Not yet implemented")
            // Will handle sign out logic here, with Google and other providers if needed
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Error during sign out: ${e.message ?: "Unknown error"}")
        }
    }
}
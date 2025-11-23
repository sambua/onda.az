package az.onda.di

import az.onda.auth.AuthViewModel
import az.onda.data.CustomerRepositoryImpl
import az.onda.data.api.createAuthApi
import az.onda.data.auth.AuthRepository
import az.onda.data.auth.AuthRepositoryImpl
import az.onda.data.auth.TokenManager
import az.onda.data.domain.CustomerRepository
import az.onda.home.HomeGraphViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    // API
    single { createAuthApi() }

    // Token Management
    single { TokenManager() }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<CustomerRepository> { CustomerRepositoryImpl() }

    // ViewModels
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
}

fun initKoinModule(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
        )
    }
}
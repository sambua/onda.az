package az.onda.di

import az.onda.auth.AuthViewModel
import az.onda.data.CustomerRepositoryImpl
import az.onda.data.domain.CustomerRepository
import az.onda.home.HomeGraphViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<CustomerRepository> { CustomerRepositoryImpl() }
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
}

fun initKoinModule(
    config: (KoinApplication.() -> Unit)? = null,
) {
    // Initialize your Koin modules here
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
        )
    }
}
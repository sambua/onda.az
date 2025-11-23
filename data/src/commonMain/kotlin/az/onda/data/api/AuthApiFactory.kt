package az.onda.data.api

fun createAuthApi(): AuthApi {
    return AuthApi(createHttpClient())
}

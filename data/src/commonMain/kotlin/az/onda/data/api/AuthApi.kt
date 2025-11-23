package az.onda.data.api

import az.onda.data.api.model.ApiErrorResponse
import az.onda.data.api.model.OtpSendRequest
import az.onda.data.api.model.OtpSendResponse
import az.onda.data.api.model.OtpVerifyRequest
import az.onda.data.api.model.RefreshTokenRequest
import az.onda.data.api.model.SocialAuthRequest
import az.onda.data.api.model.TokenResponse
import az.onda.data.api.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthApi(private val client: HttpClient) {

    suspend fun sendOtp(request: OtpSendRequest): Result<OtpSendResponse> {
        return safeApiCall {
            client.post(ApiConfig.BASE_URL + ApiConfig.Endpoints.OTP_SEND) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun verifyOtp(request: OtpVerifyRequest): Result<TokenResponse> {
        return safeApiCall {
            client.post(ApiConfig.BASE_URL + ApiConfig.Endpoints.OTP_VERIFY) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun refreshToken(request: RefreshTokenRequest): Result<TokenResponse> {
        return safeApiCall {
            client.post(ApiConfig.BASE_URL + ApiConfig.Endpoints.REFRESH) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun socialAuth(request: SocialAuthRequest): Result<TokenResponse> {
        return safeApiCall {
            client.post(ApiConfig.BASE_URL + ApiConfig.Endpoints.SOCIAL) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun getCurrentUser(accessToken: String): Result<UserResponse> {
        return safeApiCall {
            client.get(ApiConfig.BASE_URL + ApiConfig.Endpoints.ME) {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }
    }

    private suspend inline fun <reified T> safeApiCall(
        crossinline apiCall: suspend () -> HttpResponse
    ): Result<T> {
        return try {
            val response = apiCall()
            if (response.status.isSuccess()) {
                Result.success(response.body<T>())
            } else {
                val errorResponse = response.body<ApiErrorResponse>()
                Result.failure(ApiException(errorResponse.getErrorMessage()))
            }
        } catch (e: ApiException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(ApiException(e.message ?: "Network error"))
        }
    }
}

class ApiException(message: String) : Exception(message)

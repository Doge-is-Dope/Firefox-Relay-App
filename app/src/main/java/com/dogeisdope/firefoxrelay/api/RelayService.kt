package com.dogeisdope.firefoxrelay.api

import com.dogeisdope.firefoxrelay.data.AddressData
import com.dogeisdope.firefoxrelay.data.RelayAddress
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RelayService {
    @GET("relayaddresses/")
    suspend fun getRelayAddresses(): Response<List<RelayAddress>>

    @POST("relayaddresses/")
    suspend fun createRelayAddress(@Body addressData: AddressData): Response<RelayAddress>

    @DELETE("relayaddresses/{id}/")
    suspend fun deleteRelayAddress(@Path("id") addressId: Long): Response<Unit>

    @PATCH("relayaddresses/{id}/")
    suspend fun updateAddress(
        @Path("id") addressId: Long,
        @Body addressData: AddressData
    ): Response<RelayAddress>
}
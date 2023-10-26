package com.dogeisdope.firefoxrelay.repository

import com.dogeisdope.firefoxrelay.api.FirefoxApi
import com.dogeisdope.firefoxrelay.data.CreateAddressData
import com.dogeisdope.firefoxrelay.data.RelayAddress
import retrofit2.Response

class RelayRepository {
    private val relayService = FirefoxApi.relayService

    suspend fun getRelayAddresses(): Response<List<RelayAddress>> {
        return relayService.getRelayAddresses()
    }

    suspend fun createRelayAddress(description: String): Response<RelayAddress> {
        return relayService.createRelayAddress(CreateAddressData(description = description))
    }

    suspend fun deleteRelayAddress(id: Long): Response<Unit> {
        return relayService.deleteRelayAddress(id)
    }
}
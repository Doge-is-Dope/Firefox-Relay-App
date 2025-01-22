package com.dogeisdope.firefoxrelay.repository

import com.dogeisdope.firefoxrelay.api.FirefoxApi
import com.dogeisdope.firefoxrelay.data.AddressData
import com.dogeisdope.firefoxrelay.data.RelayAddress
import retrofit2.Response

class RelayRepository {
    private val relayService = FirefoxApi.relayService

    suspend fun getRelayAddresses(): Response<List<RelayAddress>> {
        return relayService.getRelayAddresses()
    }

    suspend fun createRelayAddress(description: String): Response<RelayAddress> {
        return relayService.createRelayAddress(AddressData(description = description))
    }

    suspend fun deleteRelayAddress(id: Long): Response<Unit> {
        return relayService.deleteRelayAddress(id)
    }

    suspend fun updateRelayAddress(id: Long, description: String): Response<RelayAddress> {
        return relayService.updateAddress(id, AddressData(description = description))
    }
}
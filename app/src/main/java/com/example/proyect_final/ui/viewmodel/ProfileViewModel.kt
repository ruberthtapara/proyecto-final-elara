package com.example.proyect_final.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.proyect_final.StyleGenApplication
import com.example.proyect_final.domain.model.UserProfile
import com.example.proyect_final.domain.model.PaymentMethod
import com.example.proyect_final.domain.model.ShippingAddress
import com.example.proyect_final.domain.model.UserPreferences
import com.example.proyect_final.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ProfileViewModel(
    private val profileRepository: UserProfileRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile?> = profileRepository.getUserProfile()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val paymentMethods: StateFlow<List<PaymentMethod>> = profileRepository.getPaymentMethods()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val shippingAddresses: StateFlow<List<ShippingAddress>> = profileRepository.getShippingAddresses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val selectedLanguage: StateFlow<String> = profileRepository.getLanguage()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Español"
        )

    val userPreferences: StateFlow<UserPreferences> = profileRepository.getUserPreferences()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    fun saveUserProfile(name: String, email: String, phone: String, photoUrl: String) {
        viewModelScope.launch {
            val updated = UserProfile(name, email, phone, photoUrl)
            profileRepository.saveUserProfile(updated)
        }
    }

    fun savePaymentMethod(id: String?, cardNumber: String, cardholderName: String, expiryDate: String, cvv: String) {
        viewModelScope.launch {
            val cardId = id ?: UUID.randomUUID().toString()
            val card = PaymentMethod(cardId, cardNumber, cardholderName, expiryDate, cvv)
            profileRepository.savePaymentMethod(card)
        }
    }

    fun deletePaymentMethod(cardId: String) {
        viewModelScope.launch {
            profileRepository.deletePaymentMethod(cardId)
        }
    }

    fun saveShippingAddress(id: String?, title: String, fullAddress: String, city: String, postalCode: String) {
        viewModelScope.launch {
            val addressId = id ?: UUID.randomUUID().toString()
            val address = ShippingAddress(addressId, title, fullAddress, city, postalCode)
            profileRepository.saveShippingAddress(address)
        }
    }

    fun deleteShippingAddress(addressId: String) {
        viewModelScope.launch {
            profileRepository.deleteShippingAddress(addressId)
        }
    }

    fun saveLanguage(language: String) {
        viewModelScope.launch {
            profileRepository.saveLanguage(language)
        }
    }

    fun updateColorPalette(palette: String) {
        viewModelScope.launch {
            val current = userPreferences.value
            profileRepository.saveUserPreferences(current.copy(colorPalette = palette))
        }
    }

    fun updateFitStyle(fit: String) {
        viewModelScope.launch {
            val current = userPreferences.value
            profileRepository.saveUserPreferences(current.copy(fitStyle = fit))
        }
    }

    fun updateAiCuratorStyle(style: String) {
        viewModelScope.launch {
            val current = userPreferences.value
            profileRepository.saveUserPreferences(current.copy(aiCuratorStyle = style))
        }
    }

    fun updateStockAlerts(enabled: Boolean) {
        viewModelScope.launch {
            val current = userPreferences.value
            profileRepository.saveUserPreferences(current.copy(stockAlerts = enabled))
        }
    }

    fun updateWeeklySummary(enabled: Boolean) {
        viewModelScope.launch {
            val current = userPreferences.value
            profileRepository.saveUserPreferences(current.copy(weeklySummary = enabled))
        }
    }

    fun updateShareDataWithAi(enabled: Boolean) {
        viewModelScope.launch {
            val current = userPreferences.value
            profileRepository.saveUserPreferences(current.copy(shareDataWithAi = enabled))
        }
    }

    fun updateCacheLocalAdvice(enabled: Boolean) {
        viewModelScope.launch {
            val current = userPreferences.value
            profileRepository.saveUserPreferences(current.copy(cacheLocalAdvice = enabled))
        }
    }

    fun wipeLocalProfileData() {
        viewModelScope.launch {
            profileRepository.saveUserProfile(UserProfile("", "", "", ""))
            profileRepository.saveUserPreferences(UserPreferences())
            // Remove payment cards and addresses
            val currentCards = paymentMethods.value
            currentCards.forEach { profileRepository.deletePaymentMethod(it.id) }
            val currentAddresses = shippingAddresses.value
            currentAddresses.forEach { profileRepository.deleteShippingAddress(it.id) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StyleGenApplication)
                ProfileViewModel(profileRepository = application.container.userProfileRepository)
            }
        }
    }
}

package com.example.proyect_final.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.proyect_final.domain.model.Product
import com.example.proyect_final.util.WidgetUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val size: String = "M"
)

/**
 * MODIFICADO PARA EL EJERCICIO FINAL:
 * Adaptado para recibir el Application Context, permitiendo invocar a WidgetUtils
 * para mantener actualizado el Widget de Carrito en la pantalla de inicio en tiempo real.
 */
class CartViewModel(private val application: Application) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private fun updateWidget(items: List<CartItem>) {
        val totalCount = items.sumOf { it.quantity }
        WidgetUtils.updateCartWidget(application, totalCount)
    }

    fun addProduct(product: Product, size: String = "M") {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id && it.size == size }
            if (existingItem != null) {
                currentItems.map { 
                    if (it.product.id == product.id && it.size == size) it.copy(quantity = it.quantity + 1) 
                    else it 
                }
            } else {
                currentItems + CartItem(product, 1, size)
            }
        }
        updateWidget(_cartItems.value)
    }

    fun removeProduct(productId: String, size: String) {
        _cartItems.update { currentItems ->
            currentItems.filterNot { it.product.id == productId && it.size == size }
        }
        updateWidget(_cartItems.value)
    }

    fun updateQuantity(productId: String, size: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeProduct(productId, size)
            return
        }
        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.product.id == productId && it.size == size) it.copy(quantity = newQuantity)
                else it
            }
        }
        updateWidget(_cartItems.value)
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        updateWidget(emptyList())
    }

    val subtotal: Double
        get() = _cartItems.value.sumOf { it.product.price * it.quantity }

    private val sharedPrefs = application.getSharedPreferences("elara_cart_prefs", android.content.Context.MODE_PRIVATE)

    // Daily Check-In state variables
    private val _claimedDaysCount = MutableStateFlow(sharedPrefs.getInt("claimed_days_count", 0))
    val claimedDaysCount: StateFlow<Int> = _claimedDaysCount.asStateFlow()

    private val _lastClaimedDate = MutableStateFlow(sharedPrefs.getString("last_claimed_date", "") ?: "")
    val lastClaimedDate: StateFlow<String> = _lastClaimedDate.asStateFlow()

    // Current selected coupon index (1-based index of day, 0 if no coupon applied)
    private val _selectedCouponDay = MutableStateFlow(sharedPrefs.getInt("selected_coupon_day", 0))
    val selectedCouponDay: StateFlow<Int> = _selectedCouponDay.asStateFlow()

    // Simulated date offset (for testing)
    private val _simulatedDateOffset = MutableStateFlow(sharedPrefs.getInt("simulated_date_offset", 0))
    val simulatedDateOffset: StateFlow<Int> = _simulatedDateOffset.asStateFlow()

    fun getTodayString(): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_YEAR, _simulatedDateOffset.value)
        return java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(calendar.time)
    }

    fun isTodayClaimed(): Boolean {
        return _lastClaimedDate.value == getTodayString()
    }

    fun claimTodayCoupon(): Boolean {
        if (isTodayClaimed()) return false
        val currentCount = _claimedDaysCount.value
        if (currentCount >= 10) return false

        val newCount = currentCount + 1
        val todayStr = getTodayString()

        _claimedDaysCount.value = newCount
        _lastClaimedDate.value = todayStr
        
        // Auto-select the newly claimed coupon
        _selectedCouponDay.value = newCount

        sharedPrefs.edit()
            .putInt("claimed_days_count", newCount)
            .putString("last_claimed_date", todayStr)
            .putInt("selected_coupon_day", newCount)
            .apply()

        return true
    }

    fun selectCoupon(day: Int) {
        if (day in 0.._claimedDaysCount.value) {
            _selectedCouponDay.value = day
            sharedPrefs.edit().putInt("selected_coupon_day", day).apply()
        }
    }

    fun simulateNextDay() {
        val newOffset = _simulatedDateOffset.value + 1
        _simulatedDateOffset.value = newOffset
        sharedPrefs.edit().putInt("simulated_date_offset", newOffset).apply()
    }

    fun resetDailyBonus() {
        _claimedDaysCount.value = 0
        _lastClaimedDate.value = ""
        _selectedCouponDay.value = 0
        _simulatedDateOffset.value = 0
        sharedPrefs.edit()
            .putInt("claimed_days_count", 0)
            .putString("last_claimed_date", "")
            .putInt("selected_coupon_day", 0)
            .putInt("simulated_date_offset", 0)
            .apply()
    }

    fun getCouponPercentageForDay(day: Int): Int {
        if (day < 1 || day > 10) return 0
        return 10 + (day - 1) * 5
    }

    val selectedCouponPercentage: Int
        get() = getCouponPercentageForDay(_selectedCouponDay.value)

    val discountAmount: Double
        get() = subtotal * (selectedCouponPercentage / 100.0)

    val totalAmount: Double
        get() = subtotal - discountAmount

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { 
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                CartViewModel(app)
            }
        }
    }
}

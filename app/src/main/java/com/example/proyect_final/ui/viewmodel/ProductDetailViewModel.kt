package com.example.proyect_final.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.proyect_final.StyleGenApplication
import com.example.proyect_final.domain.model.Product
import com.example.proyect_final.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.proyect_final.domain.repository.AuthRepository
import com.example.proyect_final.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore

sealed interface DetailState {
    object Loading : DetailState
    data class Success(val product: Product) : DetailState
    data class Error(val message: String) : DetailState
}

data class ProductReview(
    val userId: String = "",
    val userName: String = "",
    val comment: String = "",
    val rating: Double = 5.0,
    val timestamp: Long = System.currentTimeMillis()
)

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository,
    private val productId: String
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _currentUserState = MutableStateFlow<User?>(null)
    val currentUserState: StateFlow<User?> = _currentUserState.asStateFlow()

    private val _uiState = MutableStateFlow<DetailState>(DetailState.Loading)
    val uiState: StateFlow<DetailState> = _uiState.asStateFlow()

    private val _recommendedProducts = MutableStateFlow<List<Product>>(emptyList())
    val recommendedProducts: StateFlow<List<Product>> = _recommendedProducts.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _reviews = MutableStateFlow<List<ProductReview>>(emptyList())
    val reviews: StateFlow<List<ProductReview>> = _reviews.asStateFlow()

    private var favoriteListenerRegistration: com.google.firebase.firestore.ListenerRegistration? = null

    init {
        getProduct()
        observeCurrentUser()
        observeFavoriteStatus()
        observeReviews()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _currentUserState.value = user
            }
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            _currentUserState.collect { user ->
                favoriteListenerRegistration?.remove()
                val userId = user?.uid
                if (userId != null) {
                    favoriteListenerRegistration = firestore.collection("users").document(userId)
                        .collection("favorites").document(productId)
                        .addSnapshotListener { snapshot, error ->
                            if (error != null) return@addSnapshotListener
                            val isFav = snapshot?.getBoolean("isFavorite") ?: false
                            _isFavorite.value = isFav
                        }
                } else {
                    _isFavorite.value = false
                }
            }
        }
    }

    private fun observeReviews() {
        firestore.collection("reviews")
            .whereEqualTo("productId", productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val list = snapshot?.documents?.mapNotNull { doc ->
                    val userId = doc.getString("userId") ?: ""
                    val userName = doc.getString("userName") ?: ""
                    val comment = doc.getString("comment") ?: ""
                    val rating = doc.getDouble("rating") ?: 5.0
                    val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                    ProductReview(userId, userName, comment, rating, timestamp)
                } ?: emptyList()
                
                // Merge with any local reviews that failed to save to Firestore
                val currentLocalOnly = _reviews.value.filter { local ->
                    list.none { it.userId == local.userId && it.comment == local.comment }
                }
                _reviews.value = (list + currentLocalOnly).sortedByDescending { it.timestamp }
            }
    }

    fun toggleFavorite() {
        val userId = _currentUserState.value?.uid ?: return
        val nextFavState = !_isFavorite.value
        
        val data = mapOf(
            "productId" to productId,
            "isFavorite" to nextFavState,
            "timestamp" to System.currentTimeMillis()
        )
        
        firestore.collection("users").document(userId)
            .collection("favorites").document(productId)
            .set(data)
            .addOnSuccessListener {
                _isFavorite.value = nextFavState
            }
    }

    fun addReview(rating: Double, comment: String, onComplete: (Result<Unit>) -> Unit) {
        val currentUser = _currentUserState.value
        if (currentUser == null) {
            onComplete(Result.failure(Exception("Debe iniciar sesión para dejar una reseña")))
            return
        }
        val userId = currentUser.uid
        val email = currentUser.email ?: "Anónimo"
        val userName = email.substringBefore("@")
        
        val timestamp = System.currentTimeMillis()
        val reviewData = mapOf(
            "productId" to productId,
            "userId" to userId,
            "userName" to userName,
            "comment" to comment,
            "rating" to rating,
            "timestamp" to timestamp
        )
        
        val docId = "${userId}_${productId}"
        
        firestore.collection("reviews").document(docId)
            .set(reviewData)
            .addOnSuccessListener {
                firestore.collection("users").document(userId)
                    .collection("reviews").document(productId)
                    .set(reviewData)
                    .addOnSuccessListener {
                        onComplete(Result.success(Unit))
                    }
                    .addOnFailureListener {
                        onComplete(Result.success(Unit))
                    }
            }
            .addOnFailureListener { e ->
                // Guardar localmente si falla Firestore (offline/reglas/etc)
                android.util.Log.d("ProductDetailViewModel", "Firestore error, agregando reseña localmente", e)
                val localReview = ProductReview(userId, userName, comment, rating, timestamp)
                _reviews.value = (listOf(localReview) + _reviews.value).distinctBy { it.userId + "_" + it.timestamp }.sortedByDescending { it.timestamp }
                onComplete(Result.success(Unit))
            }
    }

    fun getProduct() {
        viewModelScope.launch {
            _uiState.value = DetailState.Loading
            productRepository.getProductById(productId)
                .onSuccess { product ->
                    _uiState.value = DetailState.Success(product)
                    productRepository.getProducts()
                        .onSuccess { allProducts ->
                            val filtered = allProducts
                                .filter { it.id != product.id && it.category.equals(product.category, ignoreCase = true) }
                                .take(4)
                            _recommendedProducts.value = if (filtered.isNotEmpty()) filtered else allProducts.filter { it.id != product.id }.take(4)
                        }
                }
                .onFailure { error ->
                    _uiState.value = DetailState.Error(error.message ?: "Error desconocido")
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        favoriteListenerRegistration?.remove()
    }

    companion object {
        fun provideFactory(productId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StyleGenApplication)
                val productRepository = application.container.productRepository
                val authRepository = application.container.authRepository
                ProductDetailViewModel(
                    productRepository = productRepository,
                    authRepository = authRepository,
                    productId = productId
                )
            }
        }
    }
}

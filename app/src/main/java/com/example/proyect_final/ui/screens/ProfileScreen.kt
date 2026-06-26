package com.example.proyect_final.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.proyect_final.domain.model.PaymentMethod
import com.example.proyect_final.domain.model.ShippingAddress
import com.example.proyect_final.ui.viewmodel.AuthViewModel
import com.example.proyect_final.ui.viewmodel.ProfileViewModel

sealed class AccountSheet {
    object PersonalInfo : AccountSheet()
    object PaymentMethods : AccountSheet()
    object ShippingAddresses : AccountSheet()
    object Language : AccountSheet()
    object ColorPalette : AccountSheet()
    object FitAndSilhouette : AccountSheet()
    object TrainAiCurator : AccountSheet()
    object ChangePassword : AccountSheet()
    object ManageAiData : AccountSheet()
}

// Translation Helper
fun tr(key: String, lang: String): String {
    val dict = mapOf(
        "Español" to mapOf(
            "settings_title" to "AJUSTES",
            "search_desc" to "Buscar",
            "personal_membership" to "Membresía Elite • Madrid, ES",
            "personal_info" to "Información Personal",
            "payment_methods" to "Métodos de Pago",
            "shipping_addresses" to "Direcciones de Envío",
            "language" to "Idioma",
            "section_account" to "CUENTA",
            "section_ai_style" to "PREFERENCIAS DE ESTILO IA",
            "color_palette" to "Paleta Cromática Personal",
            "fit_silhouette" to "Ajuste y Silueta",
            "train_ai_curator" to "Entrenar Curador de IA",
            "section_notifications" to "NOTIFICACIONES",
            "stock_alerts" to "Alertas de Stock y Drops",
            "weekly_summary" to "Resumen Semanal de Estilo",
            "section_privacy" to "PRIVACIDAD Y SEGURIDAD",
            "change_password" to "Cambiar Contraseña",
            "manage_ai_data" to "Gestión de Datos IA",
            "section_help" to "AYUDA",
            "help_center" to "Centro de Ayuda",
            "terms_conditions" to "Términos y Condiciones",
            "logout" to "Cerrar Sesión",
            "save_changes" to "Guardar Cambios",
            "personal_info_title" to "Información Personal",
            "fullname" to "Nombre Completo",
            "email" to "Correo Electrónico",
            "phone" to "Teléfono de Contacto",
            "choose_avatar" to "Selecciona un avatar de colección:",
            "save_profile_toast" to "Perfil guardado con éxito",
            "save_profile_error" to "Por favor completa Nombre y Correo"
        ),
        "English" to mapOf(
            "settings_title" to "SETTINGS",
            "search_desc" to "Search",
            "personal_membership" to "Elite Membership • Madrid, ES",
            "personal_info" to "Personal Information",
            "payment_methods" to "Payment Methods",
            "shipping_addresses" to "Shipping Addresses",
            "language" to "Language",
            "section_account" to "ACCOUNT",
            "section_ai_style" to "AI STYLE PREFERENCES",
            "color_palette" to "Personal Color Palette",
            "fit_silhouette" to "Fit & Silhouette",
            "train_ai_curator" to "Train AI Curator",
            "section_notifications" to "NOTIFICATIONS",
            "stock_alerts" to "Stock & Drop Alerts",
            "weekly_summary" to "Weekly Style Summary",
            "section_privacy" to "PRIVACY & SECURITY",
            "change_password" to "Change Password",
            "manage_ai_data" to "AI Data Management",
            "section_help" to "HELP",
            "help_center" to "Help Center",
            "terms_conditions" to "Terms & Conditions",
            "logout" to "Log Out",
            "save_changes" to "Save Changes",
            "personal_info_title" to "Personal Information",
            "fullname" to "Full Name",
            "email" to "Email Address",
            "phone" to "Contact Phone",
            "choose_avatar" to "Select an avatar from the collection:",
            "save_profile_toast" to "Profile saved successfully",
            "save_profile_error" to "Please complete Name and Email"
        ),
        "Português" to mapOf(
            "settings_title" to "CONFIGURAÇÕES",
            "search_desc" to "Buscar",
            "personal_membership" to "Assinatura Elite • Madrid, ES",
            "personal_info" to "Informações Pessoais",
            "payment_methods" to "Métodos de Pagamento",
            "shipping_addresses" to "Endereços de Envio",
            "language" to "Idioma",
            "section_account" to "CONTA",
            "section_ai_style" to "PREFERÊNCIAS DE ESTILO IA",
            "color_palette" to "Paleta de Cores Pessoal",
            "fit_silhouette" to "Ajuste e Silhueta",
            "train_ai_curator" to "Treinar Curador de IA",
            "section_notifications" to "NOTIFICAÇÕES",
            "stock_alerts" to "Alertas de Stock e Drops",
            "weekly_summary" to "Resumo Semanal de Estilo",
            "section_privacy" to "PRIVACIDADE E SEGURANÇA",
            "change_password" to "Alterar Senha",
            "manage_ai_data" to "Gestão de Dados IA",
            "section_help" to "AJUDA",
            "help_center" to "Centro de Ajuda",
            "terms_conditions" to "Termos e Condições",
            "logout" to "Sair",
            "save_changes" to "Salvar Alterações",
            "personal_info_title" to "Informações Pessoais",
            "fullname" to "Nome Completo",
            "email" to "E-mail",
            "phone" to "Telefone de Contato",
            "choose_avatar" to "Selecione um avatar da coleção:",
            "save_profile_toast" to "Perfil salvo com sucesso",
            "save_profile_error" to "Por favor preencha Nome e E-mail"
        ),
        "Français" to mapOf(
            "settings_title" to "PARAMÈTRES",
            "search_desc" to "Rechercher",
            "personal_membership" to "Membres Elite • Madrid, ES",
            "personal_info" to "Informations Personnelles",
            "payment_methods" to "Modes de Paiement",
            "shipping_addresses" to "Adresses de Livraison",
            "language" to "Langue",
            "section_account" to "COMPTE",
            "section_ai_style" to "PRÉFÉRENCES DE STYLE IA",
            "color_palette" to "Palette de Couleurs Personnelle",
            "fit_silhouette" to "Coupe et Silhouette",
            "train_ai_curator" to "Entraîner le Curateur IA",
            "section_notifications" to "NOTIFICATIONS",
            "stock_alerts" to "Alertes de Stock et Drops",
            "weekly_summary" to "Résumé Hebdomadaire de Style",
            "section_privacy" to "CONFIDENTIALITÉ ET SÉCURITÉ",
            "change_password" to "Changer de Mot de Passe",
            "manage_ai_data" to "Gestion des Données IA",
            "section_help" to "AIDE",
            "help_center" to "Centre d'Aide",
            "terms_conditions" to "Conditions Générales",
            "logout" to "Se Déconnecter",
            "save_changes" to "Enregistrer",
            "personal_info_title" to "Informations Personnelles",
            "fullname" to "Nom Complet",
            "email" to "Adresse E-mail",
            "phone" to "Téléphone de Contact",
            "choose_avatar" to "Sélectionnez un avatar de la collection :",
            "save_profile_toast" to "Profil enregistré avec succès",
            "save_profile_error" to "Veuillez remplir le nom et l'e-mail"
        ),
        "Deutsch" to mapOf(
            "settings_title" to "EINSTELLUNGEN",
            "search_desc" to "Suchen",
            "personal_membership" to "Elite-Mitgliedschaft • Madrid, ES",
            "personal_info" to "Persönliche Angaben",
            "payment_methods" to "Zahlungsmethoden",
            "shipping_addresses" to "Lieferadressen",
            "language" to "Sprache",
            "section_account" to "KONTO",
            "section_ai_style" to "KI-STILPRÄFERENZEN",
            "color_palette" to "Persönliche Farbpalette",
            "fit_silhouette" to "Passform & Silhouette",
            "train_ai_curator" to "KI-Kurator trainieren",
            "section_notifications" to "BENACHRICHTIGUNGEN",
            "stock_alerts" to "Lagerbestand- & Drop-Benachrichtigungen",
            "weekly_summary" to "Wöchentlicher Stilbericht",
            "section_privacy" to "DATENSCHUTZ & SICHERHEIT",
            "change_password" to "Passwort ändern",
            "manage_ai_data" to "KI-Datenverwaltung",
            "section_help" to "HILFE",
            "help_center" to "Hilfezentrum",
            "terms_conditions" to "Allgemeine Geschäftsbedingungen",
            "logout" to "Abmelden",
            "save_changes" to "Änderungen speichern",
            "personal_info_title" to "Persönliche Angaben",
            "fullname" to "Vollständiger Name",
            "email" to "E-Mail-Adresse",
            "phone" to "Kontakttelefon",
            "choose_avatar" to "Wählen Sie einen Avatar aus der Sammlung:",
            "save_profile_toast" to "Profil erfolgreich gespeichert",
            "save_profile_error" to "Bitte Name und E-Mail ausfüllen"
        ),
        "Italiano" to mapOf(
            "settings_title" to "IMPOSTAZIONI",
            "search_desc" to "Cerca",
            "personal_membership" to "Abbonamento Elite • Madrid, ES",
            "personal_info" to "Informazioni Personali",
            "payment_methods" to "Metodi di Pagamento",
            "shipping_addresses" to "Indirizzi di Spedizione",
            "language" to "Lingua",
            "section_account" to "ACCOUNT",
            "section_ai_style" to "PREFERENZE STILE IA",
            "color_palette" to "Tavolozza dei Colori Personale",
            "fit_silhouette" to "Vestibilità e Silhouette",
            "train_ai_curator" to "Addestra Curatore IA",
            "section_notifications" to "NOTIFICHE",
            "stock_alerts" to "Avvisi di Stock e Drops",
            "weekly_summary" to "Riepilogo Settimanale di Stile",
            "section_privacy" to "PRIVACY E SICUREZZA",
            "change_password" to "Cambia Password",
            "manage_ai_data" to "Gestione Dati IA",
            "section_help" to "AIUTO",
            "help_center" to "Centro Assistenza",
            "terms_conditions" to "Termini e Condizioni",
            "logout" to "Disconnettersi",
            "save_changes" to "Salva Modifiche",
            "personal_info_title" to "Informazioni Personali",
            "fullname" to "Nome Completo",
            "email" to "Indirizzo E-mail",
            "phone" to "Telefono di Contatto",
            "choose_avatar" to "Seleziona un avatar dalla collezione:",
            "save_profile_toast" to "Profilo salvato con successo",
            "save_profile_error" to "Si prega di completare Nome ed E-mail"
        )
    )
    val langMap = dict[lang] ?: dict["Español"]!!
    return langMap[key] ?: key
}

fun translatePalette(palette: String, lang: String): String {
    if (lang == "English") {
        return when (palette) {
            "Monocromo" -> "Monochrome"
            "Azul Aurora" -> "Aurora Blue"
            "Oro Imperial" -> "Imperial Gold"
            "Esmeralda" -> "Emerald"
            else -> palette
        }
    }
    if (lang == "Français") {
        return when (palette) {
            "Monocromo" -> "Monochrome"
            "Azul Aurora" -> "Bleu Aurore"
            "Oro Imperial" -> "Or Impérial"
            "Esmeralda" -> "Émeraude"
            else -> palette
        }
    }
    if (lang == "Português") {
        return when (palette) {
            "Monocromo" -> "Monocromático"
            "Azul Aurora" -> "Azul Aurora"
            "Oro Imperial" -> "Ouro Imperial"
            "Esmeralda" -> "Esmeralda"
            else -> palette
        }
    }
    if (lang == "Deutsch") {
        return when (palette) {
            "Monocromo" -> "Monochrom"
            "Azul Aurora" -> "Aurora Blau"
            "Oro Imperial" -> "Kaiserliches Gold"
            "Esmeralda" -> "Smaragd"
            else -> palette
        }
    }
    if (lang == "Italiano") {
        return when (palette) {
            "Monocromo" -> "Monocromo"
            "Azul Aurora" -> "Blu Aurora"
            "Oro Imperial" -> "Oro Imperiale"
            "Esmeralda" -> "Smeraldo"
            else -> palette
        }
    }
    return palette
}

fun translateFit(fit: String, lang: String): String {
    if (lang == "English") {
        return fit
            .replace("Regular", "Regular Fit")
            .replace("Ajustado", "Slim Fit")
            .replace("Holgado", "Oversized")
            .replace("Deportivo", "Athletic")
    }
    if (lang == "Português") {
        return fit
            .replace("Regular", "Regular Fit")
            .replace("Ajustado", "Ajustado")
            .replace("Holgado", "Folgado")
            .replace("Deportivo", "Esportivo")
    }
    if (lang == "Français") {
        return fit
            .replace("Regular", "Coupe Standard")
            .replace("Ajustado", "Coupe Ajustée")
            .replace("Holgado", "Oversize")
            .replace("Deportivo", "Sportif")
    }
    if (lang == "Deutsch") {
        return fit
            .replace("Regular", "Standardpassform")
            .replace("Ajustado", "Schmale Passform")
            .replace("Holgado", "Oversize")
            .replace("Deportivo", "Sportliche Passform")
    }
    if (lang == "Italiano") {
        return fit
            .replace("Regular", "Vestibilità Standard")
            .replace("Ajustado", "Vestibilità Aderente")
            .replace("Holgado", "Oversize")
            .replace("Deportivo", "Vestibilità Sportiva")
    }
    return fit
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
) {
    val context = LocalContext.current
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)
    val userProfile by profileViewModel.userProfile.collectAsState()
    val selectedLanguage by profileViewModel.selectedLanguage.collectAsState()
    val preferences by profileViewModel.userPreferences.collectAsState()

    var activeSheet by remember { mutableStateOf<AccountSheet?>(null) }
    val lang = selectedLanguage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tr("settings_title", lang), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = MaterialTheme.colorScheme.primary) },
                actions = {
                    IconButton(onClick = { 
                        Toast.makeText(context, tr("search_desc", lang), Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Card (Interactive)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                    .clickable { activeSheet = AccountSheet.PersonalInfo }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = userProfile?.photoUrl ?: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=1000",
                    contentDescription = "Avatar de usuario",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile?.name ?: "Sofia Valery",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = userProfile?.email ?: currentUser?.email ?: "sofia.valery@example.com",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = tr("personal_membership", lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Icon(Icons.Default.Edit, contentDescription = "Editar Perfil", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.secondary)
            }

            SettingsSection(tr("section_account", lang)) {
                SettingsItem(
                    icon = Icons.Default.Person,
                    label = tr("personal_info", lang),
                    onClick = { activeSheet = AccountSheet.PersonalInfo }
                )
                SettingsItem(
                    icon = Icons.Default.Payment,
                    label = tr("payment_methods", lang),
                    onClick = { activeSheet = AccountSheet.PaymentMethods }
                )
                SettingsItem(
                    icon = Icons.Default.LocationOn,
                    label = tr("shipping_addresses", lang),
                    onClick = { activeSheet = AccountSheet.ShippingAddresses }
                )
                SettingsItem(
                    icon = Icons.Default.Language,
                    label = tr("language", lang),
                    value = lang,
                    onClick = { activeSheet = AccountSheet.Language }
                )
            }

            SettingsSection(tr("section_ai_style", lang)) {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    label = tr("color_palette", lang),
                    value = translatePalette(preferences.colorPalette, lang),
                    onClick = { activeSheet = AccountSheet.ColorPalette }
                )
                SettingsItem(
                    icon = Icons.Default.Straighten,
                    label = tr("fit_silhouette", lang),
                    value = translateFit(preferences.fitStyle, lang),
                    onClick = { activeSheet = AccountSheet.FitAndSilhouette }
                )
                SettingsItem(
                    icon = Icons.Default.AutoAwesome,
                    label = tr("train_ai_curator", lang),
                    value = preferences.aiCuratorStyle,
                    onClick = { activeSheet = AccountSheet.TrainAiCurator }
                )
            }

            SettingsSection(tr("section_notifications", lang)) {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    label = tr("stock_alerts", lang),
                    hasSwitch = true,
                    switchChecked = preferences.stockAlerts,
                    onSwitchChange = { profileViewModel.updateStockAlerts(it) }
                )
                SettingsItem(
                    icon = Icons.Default.Email,
                    label = tr("weekly_summary", lang),
                    hasSwitch = true,
                    switchChecked = preferences.weeklySummary,
                    onSwitchChange = { profileViewModel.updateWeeklySummary(it) }
                )
            }

            SettingsSection(tr("section_privacy", lang)) {
                SettingsItem(
                    icon = Icons.Default.Lock,
                    label = tr("change_password", lang),
                    onClick = { activeSheet = AccountSheet.ChangePassword }
                )
                SettingsItem(
                    icon = Icons.Default.Shield,
                    label = tr("manage_ai_data", lang),
                    onClick = { activeSheet = AccountSheet.ManageAiData }
                )
            }

            SettingsSection(tr("section_help", lang)) {
                SettingsItem(
                    icon = Icons.Default.Help,
                    label = tr("help_center", lang),
                    onClick = { Toast.makeText(context, if (lang == "English") "Opening Help Center..." else "Abriendo Centro de Ayuda...", Toast.LENGTH_SHORT).show() }
                )
                SettingsItem(
                    icon = Icons.Default.Description,
                    label = tr("terms_conditions", lang),
                    onClick = { Toast.makeText(context, if (lang == "English") "Opening Terms..." else "Abriendo Términos y Condiciones...", Toast.LENGTH_SHORT).show() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    authViewModel.logout()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2), contentColor = Color(0xFFEF4444))
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(tr("logout", lang), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Text(
                text = "Versión 2.4.0 (AI Engine v8)",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(100.dp))
        }

        // Bottom Sheets for Account settings
        val currentSheet = activeSheet
        if (currentSheet != null) {
            ModalBottomSheet(
                onDismissRequest = { activeSheet = null },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant) }
            ) {
                when (currentSheet) {
                    AccountSheet.PersonalInfo -> PersonalInfoSheetContent(viewModel = profileViewModel, lang = lang) { activeSheet = null }
                    AccountSheet.PaymentMethods -> PaymentMethodsSheetContent(viewModel = profileViewModel, lang = lang)
                    AccountSheet.ShippingAddresses -> ShippingAddressesSheetContent(viewModel = profileViewModel, lang = lang)
                    AccountSheet.Language -> LanguageSheetContent(viewModel = profileViewModel) { activeSheet = null }
                    AccountSheet.ColorPalette -> ColorPaletteSheetContent(viewModel = profileViewModel, lang = lang) { activeSheet = null }
                    AccountSheet.FitAndSilhouette -> FitAndSilhouetteSheetContent(viewModel = profileViewModel, lang = lang) { activeSheet = null }
                    AccountSheet.TrainAiCurator -> TrainAiCuratorSheetContent(viewModel = profileViewModel, lang = lang) { activeSheet = null }
                    AccountSheet.ChangePassword -> ChangePasswordSheetContent(authViewModel = authViewModel, lang = lang) { activeSheet = null }
                    AccountSheet.ManageAiData -> ManageAiDataSheetContent(viewModel = profileViewModel, lang = lang) { activeSheet = null }
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            letterSpacing = 1.sp
        )
        content()
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    label: String,
    value: String? = null,
    hasSwitch: Boolean = false,
    switchChecked: Boolean = false,
    onSwitchChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !hasSwitch, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(8.dp))
        }
        if (hasSwitch) {
            Switch(
                checked = switchChecked,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = Color(0xFFF1F5F9),
                    uncheckedTrackColor = Color(0xFFE2E8F0)
                )
            )
        } else {
            Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
        }
    }
}

// -------------------------------------------------------------
// Personal Info Sheet Content
// -------------------------------------------------------------
@Composable
fun PersonalInfoSheetContent(
    viewModel: ProfileViewModel,
    lang: String,
    onDismiss: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current

    var name by remember(profile) { mutableStateOf(profile?.name ?: "") }
    var email by remember(profile) { mutableStateOf(profile?.email ?: "") }
    var phone by remember(profile) { mutableStateOf(profile?.phone ?: "") }
    var photoUrl by remember(profile) { mutableStateOf(profile?.photoUrl ?: "") }
    var isEditingAvatar by remember { mutableStateOf(false) }

    val avatars = listOf(
        "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=1000",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=1000",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=1000",
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=1000",
        "https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=1000"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = tr("personal_info_title", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Avatar selector
        Box(
            modifier = Modifier
                .size(100.dp)
                .clickable { isEditingAvatar = !isEditingAvatar }
        ) {
            AsyncImage(
                model = photoUrl.ifEmpty { "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=1000" },
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Cambiar avatar",
                    modifier = Modifier.size(14.dp),
                    tint = Color.White
                )
            }
        }

        AnimatedVisibility(visible = isEditingAvatar) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = tr("choose_avatar", lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(avatars) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (photoUrl == url) 2.dp else 1.dp,
                                    color = if (photoUrl == url) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    shape = CircleShape
                                )
                                .clickable {
                                    photoUrl = url
                                    isEditingAvatar = false
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Input Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(tr("fullname", lang)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(tr("email", lang)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(tr("phone", lang)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isBlank() || email.isBlank()) {
                    Toast.makeText(context, tr("save_profile_error", lang), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.saveUserProfile(name, email, phone, photoUrl)
                    Toast.makeText(context, tr("save_profile_toast", lang), Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Text(tr("save_changes", lang), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// -------------------------------------------------------------
// Payment Methods Sheet Content
// -------------------------------------------------------------
@Composable
fun PaymentMethodsSheetContent(
    viewModel: ProfileViewModel,
    lang: String
) {
    val cards by viewModel.paymentMethods.collectAsState()
    var isAddingCard by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Add Card Form State
    var cardNumber by remember { mutableStateOf("") }
    var cardholderName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val headingAdd = if (lang == "English") "Add Card" else "Agregar Tarjeta"
    val headingList = tr("payment_methods", lang)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isAddingCard) headingAdd else headingList,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            if (!isAddingCard) {
                IconButton(onClick = { isAddingCard = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Tarjeta", tint = MaterialTheme.colorScheme.primary)
                }
            } else {
                TextButton(onClick = { isAddingCard = false }) {
                    Text(if (lang == "English") "Cancel" else "Cancelar", color = Color(0xFFEF4444))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isAddingCard) {
            // Live Preview Card
            CreditCardPreview(cardNumber, cardholderName, expiryDate)

            Spacer(modifier = Modifier.height(24.dp))

            // Inputs
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { input ->
                    val digitsOnly = input.filter { it.isDigit() }.take(16)
                    cardNumber = buildString {
                        for (i in digitsOnly.indices) {
                            append(digitsOnly[i])
                            if ((i + 1) % 4 == 0 && i < digitsOnly.length - 1) {
                                append(" ")
                            }
                        }
                    }
                },
                label = { Text(if (lang == "English") "Card Number" else "Número de Tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cardholderName,
                onValueChange = { cardholderName = it },
                label = { Text(if (lang == "English") "Cardholder Name" else "Nombre del Titular") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }.take(4)
                        expiryDate = buildString {
                            for (i in digits.indices) {
                                if (i == 2) append("/")
                                append(digits[i])
                            }
                        }
                    },
                    label = { Text(if (lang == "English") "Expiry Date" else "Vence (MM/YY)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it.filter { c -> c.isDigit() }.take(4) },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (cardNumber.length < 19 || cardholderName.isBlank() || expiryDate.length < 5 || cvv.length < 3) {
                        Toast.makeText(context, if (lang == "English") "Please fill all fields correctly" else "Completa los datos de tarjeta correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.savePaymentMethod(null, cardNumber, cardholderName, expiryDate, cvv)
                        Toast.makeText(context, if (lang == "English") "Card added" else "Tarjeta agregada", Toast.LENGTH_SHORT).show()
                        isAddingCard = false
                        cardNumber = ""
                        cardholderName = ""
                        expiryDate = ""
                        cvv = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(if (lang == "English") "Save Card" else "Guardar Tarjeta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        } else {
            // Card List
            if (cards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(if (lang == "English") "No saved cards" else "No hay tarjetas guardadas", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(cards) { card ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f))
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp, 32.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = card.cardNumber,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "${card.cardholderName.uppercase()} • Exp: ${card.expiryDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                            IconButton(onClick = {
                                viewModel.deletePaymentMethod(card.id)
                                Toast.makeText(context, if (lang == "English") "Card removed" else "Tarjeta eliminada", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreditCardPreview(
    cardNumber: String,
    cardholderName: String,
    expiryDate: String
) {
    val gradientStart = MaterialTheme.colorScheme.primary
    val gradientEnd = MaterialTheme.colorScheme.secondary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(gradientStart, gradientEnd)
                )
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STYLEGEN ACCORD",
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp
                )
                Box(
                    modifier = Modifier
                        .size(36.dp, 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFFCD34D).copy(alpha = 0.8f))
                )
            }
            
            Text(
                text = cardNumber.ifEmpty { "•••• •••• •••• ••••" },
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "TITULAR",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = cardholderName.uppercase().ifEmpty { "SOFIA VALERY" },
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "VENCE",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = expiryDate.ifEmpty { "MM/YY" },
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Shipping Addresses Sheet Content
// -------------------------------------------------------------
@Composable
fun ShippingAddressesSheetContent(
    viewModel: ProfileViewModel,
    lang: String
) {
    val addresses by viewModel.shippingAddresses.collectAsState()
    var isAddingAddress by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Add Address Form State
    var title by remember { mutableStateOf("") }
    var fullAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    val headingAdd = if (lang == "English") "New Address" else "Nueva Dirección"
    val headingList = tr("shipping_addresses", lang)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isAddingAddress) headingAdd else headingList,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            if (!isAddingAddress) {
                IconButton(onClick = { isAddingAddress = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Dirección", tint = MaterialTheme.colorScheme.primary)
                }
            } else {
                TextButton(onClick = { isAddingAddress = false }) {
                    Text(if (lang == "English") "Cancel" else "Cancelar", color = Color(0xFFEF4444))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isAddingAddress) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(if (lang == "English") "Label (e.g. Home, Office)" else "Etiqueta (Ej. Casa, Trabajo)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = fullAddress,
                onValueChange = { fullAddress = it },
                label = { Text(if (lang == "English") "Full Address" else "Dirección Completa") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text(if (lang == "English") "City" else "Ciudad") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    label = { Text(if (lang == "English") "Postal Code" else "Cód. Postal") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isBlank() || fullAddress.isBlank() || city.isBlank() || postalCode.isBlank()) {
                        Toast.makeText(context, if (lang == "English") "Please fill all fields" else "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.saveShippingAddress(null, title, fullAddress, city, postalCode)
                        Toast.makeText(context, if (lang == "English") "Address saved" else "Dirección guardada", Toast.LENGTH_SHORT).show()
                        isAddingAddress = false
                        title = ""
                        fullAddress = ""
                        city = ""
                        postalCode = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(if (lang == "English") "Save Address" else "Guardar Dirección", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        } else {
            // Address List
            if (addresses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(if (lang == "English") "No saved addresses" else "No hay direcciones guardadas", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(addresses) { addr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f))
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = addr.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "${addr.fullAddress}, ${addr.city} (${addr.postalCode})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                            IconButton(onClick = {
                                viewModel.deleteShippingAddress(addr.id)
                                Toast.makeText(context, if (lang == "English") "Address removed" else "Dirección eliminada", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Language Sheet Content
// -------------------------------------------------------------
@Composable
fun LanguageSheetContent(
    viewModel: ProfileViewModel,
    onDismiss: () -> Unit
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val context = LocalContext.current

    val languages = listOf("Español", "English", "Português", "Français", "Deutsch", "Italiano")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
    ) {
        Text(
            text = tr("language", selectedLanguage),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(languages) { lang ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedLanguage == lang) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
                        .clickable {
                            viewModel.saveLanguage(lang)
                            val toastMsg = if (lang == "English") "Language changed to English"
                                           else if (lang == "Português") "Idioma alterado para Português"
                                           else if (lang == "Français") "Langue changée en Français"
                                           else if (lang == "Deutsch") "Sprache in Deutsch geändert"
                                           else if (lang == "Italiano") "Lingua cambiata in Italiano"
                                           else "Idioma cambiado a Español"
                            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lang,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (selectedLanguage == lang) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedLanguage == lang) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                    if (selectedLanguage == lang) {
                        Icon(Icons.Default.Check, contentDescription = "Seleccionado", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// -------------------------------------------------------------
// Helper to get localized palette descriptions
// -------------------------------------------------------------
fun getPaletteSubtitle(palette: String, lang: String): String {
    return when (lang) {
        "English" -> when (palette) {
            "Monocromo" -> "Timeless purity in black and white. Absolute contrast."
            "Azul Aurora" -> "Arctic blue and tech sparks. Modern and futuristic."
            "Oro Imperial" -> "Gold opulence and prestige on charcoal. Ultra-premium."
            "Esmeralda" -> "Organic depth and mystic emerald green. Sophisticated."
            else -> ""
        }
        "Français" -> when (palette) {
            "Monocromo" -> "Pureté intemporelle en noir et blanc. Contraste absolu."
            "Azul Aurora" -> "Éclats de bleu arctique et technologique. Moderne et futuriste."
            "Oro Imperial" -> "Opulence dorée et prestige sur charbon. Ultra-premium."
            "Esmeralda" -> "Profondeur organique et vert émeraude mystique. Sophistiqué."
            else -> ""
        }
        "Português" -> when (palette) {
            "Monocromo" -> "Pureza atemporal em preto e branco. Contraste absoluto."
            "Azul Aurora" -> "Faíscas de azul ártico e tecnológico. Moderno e futurista."
            "Oro Imperial" -> "Opulência dourada e prestígio sobre carvão. Ultra-premium."
            "Esmeralda" -> "Profundidade orgânica e verde esmeralda místico. Sofisticado."
            else -> ""
        }
        "Deutsch" -> when (palette) {
            "Monocromo" -> "Zeitlose Reinheit in Schwarz und Weiß. Absoluter Kontrast."
            "Azul Aurora" -> "Arktisches Blau und Tech-Funken. Modern und futuristisch."
            "Oro Imperial" -> "Goldene Opulenz und Prestige auf Kohle. Ultra-Premium."
            "Esmeralda" -> "Organische Tiefe und mystisches Smaragdgrün. Anspruchsvoll."
            else -> ""
        }
        "Italiano" -> when (palette) {
            "Monocromo" -> "Purezza senza tempo in bianco e nero. Contrasto assoluto."
            "Azul Aurora" -> "Bagliori di blu artico e tecnologico. Moderno e futurista."
            "Oro Imperial" -> "Opulenza dorata e prestigio su carboncino. Ultra-premium."
            "Esmeralda" -> "Profondità organica e misterioso verde smeraldo. Sofisticato."
            else -> ""
        }
        else -> when (palette) { // Spanish
            "Monocromo" -> "Pureza atemporal en blanco y negro. Contraste absoluto."
            "Azul Aurora" -> "Destellos de azul ártico y tecnológico. Moderno y futurista."
            "Oro Imperial" -> "Opulencia y prestigio dorado sobre carbón. Ultra-premium."
            "Esmeralda" -> "Profundidad orgánica y verde esmeralda místico. Sofisticado."
            else -> ""
        }
    }
}

// -------------------------------------------------------------
// Color Palette Sheet Content
// -------------------------------------------------------------
@Composable
fun ColorPaletteSheetContent(
    viewModel: ProfileViewModel,
    lang: String,
    onDismiss: () -> Unit
) {
    val prefs by viewModel.userPreferences.collectAsState()
    val context = LocalContext.current

    val palettes = listOf("Monocromo", "Azul Aurora", "Oro Imperial", "Esmeralda")
    
    val paletteColors = mapOf(
        "Monocromo" to listOf(Color(0xFFFFFFFF), Color(0xFF27272A), Color(0xFF09090B), Color(0xFFFAFAFA)),
        "Azul Aurora" to listOf(Color(0xFF38BDF8), Color(0xFF0EA5E9), Color(0xFF0B132B), Color(0xFFF1F5F9)),
        "Oro Imperial" to listOf(Color(0xFFD4AF37), Color(0xFFFBBF24), Color(0xFF110F0C), Color(0xFFFAFAF9)),
        "Esmeralda" to listOf(Color(0xFF10B981), Color(0xFF34D399), Color(0xFF0A0F0D), Color(0xFFF0FDF4))
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
    ) {
        Text(
            text = tr("color_palette", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(palettes) { palette ->
                val colors = paletteColors[palette] ?: emptyList()
                val isSelected = prefs.colorPalette == palette || (palette == "Monocromo" && prefs.colorPalette !in palettes)
                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            viewModel.updateColorPalette(palette)
                            val nameTranslated = translatePalette(palette, lang)
                            val toastMsg = if (lang == "English") "$nameTranslated selected" else "$nameTranslated seleccionado"
                            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                    color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = translatePalette(palette, lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        
                        Text(
                            text = getPaletteSubtitle(palette, lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Color circle preview row
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                colors.forEach { color ->
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                    )
                                }
                            }
                            
                            // Text contrast preview capsule
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = colors.firstOrNull() ?: MaterialTheme.colorScheme.primary,
                                modifier = Modifier.height(24.dp)
                            ) {
                                Text(
                                    text = if (lang == "English") "Text Preview" else "Texto Vista",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (palette == "Monocromo") Color.Black else if (palette == "Azul Aurora") Color(0xFF0F172A) else if (palette == "Oro Imperial") Color(0xFF1A1A1A) else Color(0xFF022C22),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Fit and Silhouette Sheet Content
// -------------------------------------------------------------
@Composable
fun FitAndSilhouetteSheetContent(
    viewModel: ProfileViewModel,
    lang: String,
    onDismiss: () -> Unit
) {
    val prefs by viewModel.userPreferences.collectAsState()
    val context = LocalContext.current

    var selectedFit by remember { 
        mutableStateOf(
            if (prefs.fitStyle.contains("Ajustado")) "Ajustado"
            else if (prefs.fitStyle.contains("Holgado")) "Holgado"
            else if (prefs.fitStyle.contains("Deportivo")) "Deportivo"
            else "Regular"
        ) 
    }
    
    var height by remember { mutableStateOf(175f) }
    var weight by remember { mutableStateOf(70f) }

    val fits = listOf("Regular", "Ajustado", "Holgado", "Deportivo")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = tr("fit_silhouette", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(
            text = if (lang == "English") "Fit Preference" else "Preferencia de Ajuste",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            fits.forEach { fit ->
                val isSelected = selectedFit == fit
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedFit = fit }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = translateFit(fit, lang),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "${if (lang == "English") "Height" else "Altura"}: ${height.toInt()} cm",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Slider(
            value = height,
            onValueChange = { height = it },
            valueRange = 140f..210f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${if (lang == "English") "Weight" else "Peso"}: ${weight.toInt()} kg",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Slider(
            value = weight,
            onValueChange = { weight = it },
            valueRange = 40f..130f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val valueString = "$selectedFit • ${height.toInt()}cm / ${weight.toInt()}kg"
                viewModel.updateFitStyle(valueString)
                val toastMsg = if (lang == "English") "Silhouette settings saved" else "Ajustes de silueta guardados"
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Text(tr("save_changes", lang), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// -------------------------------------------------------------
// Train AI Curator Sheet Content
// -------------------------------------------------------------
@Composable
fun TrainAiCuratorSheetContent(
    viewModel: ProfileViewModel,
    lang: String,
    onDismiss: () -> Unit
) {
    val prefs by viewModel.userPreferences.collectAsState()
    val context = LocalContext.current

    val styles = listOf(
        "Minimalista" to "Minimalist",
        "Streetwear" to "Streetwear",
        "Clásico" to "Classic",
        "Vanguardista" to "Avant-Garde",
        "Bohemio" to "Bohemian"
    )
    
    val currentPrefs = prefs.aiCuratorStyle
    val selectedStyles = remember { 
        mutableStateListOf<String>().apply {
            styles.forEach { (es, _) ->
                if (currentPrefs.contains(es)) add(es)
            }
            if (isEmpty()) add("Minimalista")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = tr("train_ai_curator", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(
            text = if (lang == "English") "Select styles you want Gemini to prioritize:" else "Selecciona estilos que deseas entrenar en la IA:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        styles.forEach { (esName, enName) ->
            val isChecked = selectedStyles.contains(esName)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (isChecked) {
                            if (selectedStyles.size > 1) selectedStyles.remove(esName)
                        } else {
                            selectedStyles.add(esName)
                        }
                    }
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        if (isChecked) {
                            if (selectedStyles.size > 1) selectedStyles.remove(esName)
                        } else {
                            selectedStyles.add(esName)
                        }
                    },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (lang == "English") enName else esName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val summary = selectedStyles.joinToString(" & ")
                viewModel.updateAiCuratorStyle(summary)
                val toastMsg = if (lang == "English") "AI Curator preferences saved" else "Curador entrenado correctamente"
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Text(if (lang == "English") "Train AI Curator" else "Entrenar Curador de IA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// -------------------------------------------------------------
// Change Password Sheet Content
// -------------------------------------------------------------
@Composable
fun ChangePasswordSheetContent(
    authViewModel: AuthViewModel,
    lang: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = tr("change_password", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text(if (lang == "English") "New Password" else "Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(if (lang == "English") "Confirm Password" else "Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Button(
                onClick = {
                    if (newPassword.length < 6) {
                        Toast.makeText(context, if (lang == "English") "Password must be at least 6 characters" else "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                    } else if (newPassword != confirmPassword) {
                        Toast.makeText(context, if (lang == "English") "Passwords do not match" else "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        authViewModel.updatePassword(
                            password = newPassword,
                            onSuccess = {
                                isLoading = false
                                Toast.makeText(context, if (lang == "English") "Password updated successfully" else "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            },
                            onError = { error ->
                                isLoading = false
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(tr("save_changes", lang), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

// -------------------------------------------------------------
// Manage AI Data Sheet Content
// -------------------------------------------------------------
@Composable
fun ManageAiDataSheetContent(
    viewModel: ProfileViewModel,
    lang: String,
    onDismiss: () -> Unit
) {
    val prefs by viewModel.userPreferences.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = tr("manage_ai_data", lang),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(
            text = if (lang == "English") "StyleGen encrypts all your fashion preferences. Choose what you want Gemini to analyze:"
                   else "StyleGen cifra todas tus preferencias de moda. Elige qué deseas que analice Gemini:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Switch 1: Share closet history
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (lang == "English") "Share closet history with AI" else "Compartir closet con la IA",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (lang == "English") "Allows Gemini to recommend combinations using saved items." else "Permite a Gemini sugerir combinaciones con prendas guardadas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Switch(
                checked = prefs.shareDataWithAi,
                onCheckedChange = { viewModel.updateShareDataWithAi(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        HorizontalDivider(color = Color(0xFFF1F5F9))

        // Switch 2: Cache local advice
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (lang == "English") "Cache advice locally" else "Caché local de consejos",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (lang == "English") "Stores previous AI fashion curation locally for speed." else "Guarda el historial de consejos en el dispositivo para mayor velocidad.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Switch(
                checked = prefs.cacheLocalAdvice,
                onCheckedChange = { viewModel.updateCacheLocalAdvice(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Wipe data button
        Button(
            onClick = {
                viewModel.wipeLocalProfileData()
                Toast.makeText(context, if (lang == "English") "Local profile data wiped" else "Datos locales eliminados con éxito", Toast.LENGTH_SHORT).show()
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2), contentColor = Color(0xFFEF4444))
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (lang == "English") "Wipe Profile & Settings" else "Eliminar Ajustes y Perfil", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

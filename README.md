# Application Android Séries TV

## 📱 Description

Application Android développée en **Jetpack Compose** qui permet de consulter les séries TV populaires via l'API EpisoDate. L'application suit l'architecture **MVVM** et utilise des technologies Android modernes.

### Fonctionnalités Principales

- **Liste des séries populaires** : Affichage en grille avec pagination infinie
- **Détails des séries** : Écran détaillé avec synopsis, informations complètes et épisodes
- **Navigation fluide** : Navigation entre la liste et les détails
- **Chargement d'images** : Gestion optimisée des images avec mise en cache
- **Gestion des états** : Loading, succès, erreur avec retry automatique

## 🏗️ Architecture

### Architecture MVVM

L'application suit le pattern **Model-View-ViewModel** recommandé par Google :

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   View (UI)     │◄───│   ViewModel      │◄───│   Repository    │
│  - Composables  │    │  - State Mgmt    │    │  - Data Source  │
│  - Screens      │    │  - Business Logic│    │  - API Calls    │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                                         ▲
                                                         │
                                               ┌─────────────────┐
                                               │   Data Sources  │
                                               │  - API Service  │
                                               │  - Models       │
                                               └─────────────────┘
```

### Structure du Projet

```
app/src/main/java/com/example/androidseriesproject/
├── 📁 api/
│   └── ApiService.kt                 # Interface Retrofit pour l'API
├── 📁 data/
│   ├── remote/ShowApiService.kt      # Service API alternatif
│   └── repository/
│       ├── ShowRepositoryImpl.kt     # Implémentation repository alternative
│       └── TvShowRepository.kt       # Interface repository alternative
├── 📁 di/
│   └── NetworkModule.kt              # Configuration Dagger-Hilt
├── 📁 model/
│   ├── TvShow.kt                     # Modèle série TV (liste)
│   ├── TvShowDetails.kt              # Modèle détails série + épisodes
│   └── TvShowResponse.kt             # Modèle réponse API
├── 📁 repository/
│   ├── TvShowRepository.kt           # Interface repository principal
│   └── TvShowRepositoryImpl.kt       # Implémentation repository principal
├── 📁 ui/
│   ├── ShowListScreen.kt             # Écran liste des séries
│   ├── ShowDetailsScreen.kt          # Écran détails série
│   ├── UiState.kt                    # États UI pour la liste
│   └── theme/                        # Thème Material Design 3
├── 📁 util/
│   └── Resource.kt                   # Wrapper pour les états de données
├── 📁 viewmodel/
│   ├── ShowViewModel.kt              # ViewModel liste
│   └── ShowDetailsViewModel.kt       # ViewModel détails
├── AndroidSeriesApplication.kt       # Application Hilt
└── MainActivity.kt                   # Activité principale
```

## 🛠️ Technologies Utilisées

| Technologie | Version | Usage |
|-------------|---------|--------|
| **Jetpack Compose** | Latest | Interface utilisateur déclarative |
| **Dagger-Hilt** | 2.x | Injection de dépendances |
| **Retrofit** | 2.x | Clients HTTP et API REST |
| **Coil** | 2.x | Chargement et mise en cache d'images |
| **Coroutines** | 1.x | Programmation asynchrone |
| **StateFlow** | 1.x | Gestion d'état réactive |
| **Material Design 3** | Latest | Design system moderne |

## 🔧 Configuration et Installation

### Prérequis

- **Android Studio** Arctic Fox ou plus récent
- **JDK** 11 ou plus récent
- **Android SDK** niveau API 24+ (Android 7.0)
- **Gradle** 8.0+

### Installation

1. **Cloner le projet**
   ```bash
   git clone https://github.com/votre-username/android-series-project.git
   cd android-series-project
   ```

2. **Ouvrir dans Android Studio**
   - Ouvrir Android Studio
   - File → Open → Sélectionner le dossier du projet
   - Attendre la synchronisation Gradle

3. **Configuration des dépendances**
   
   Ajouter dans `app/build.gradle` :
   ```kotlin
   dependencies {
       // Jetpack Compose
       implementation "androidx.compose.ui:ui:$compose_version"
       implementation "androidx.compose.material3:material3:$material3_version"
       implementation "androidx.activity:activity-compose:$activity_compose_version"
       
       // Architecture
       implementation "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"
       implementation "androidx.hilt:hilt-navigation-compose:$hilt_navigation_compose_version"
       
       // Dagger-Hilt
       implementation "com.google.dagger:hilt-android:$hilt_version"
       kapt "com.google.dagger:hilt-compiler:$hilt_version"
       
       // Networking
       implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
       implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
       
       // Image Loading
       implementation "io.coil-kt:coil-compose:$coil_version"
       
       // Coroutines
       implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
   }
   ```

4. **Permissions réseau**
   
   Dans `AndroidManifest.xml` :
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   ```

5. **Lancer l'application**
   - Connecter un appareil Android ou lancer un émulateur
   - Cliquer sur "Run" ou `Shift + F10`

## 🏛️ Décisions Architecturales

### 1. **Pattern Repository**

**Décision** : Implémentation du pattern Repository pour abstraire les sources de données.

**Avantages** :
- Séparation claire entre logique métier et accès aux données
- Facilite les tests unitaires (mocking)
- Permet de changer facilement de source de données

```kotlin
interface TvShowRepository {
    fun getMostPopularShows(page: Int = 1): Flow<Resource<TvShowResponse>>
    fun getShowDetails(showId: Int): Flow<Resource<TvShowDetails>>
    fun searchShows(query: String, page: Int = 1): Flow<Resource<TvShowResponse>>
}
```

### 2. **Gestion d'État avec Resource Wrapper**

**Décision** : Utilisation d'une classe sealed `Resource<T>` pour encapsuler les états.

**Avantages** :
- États explicites : Loading, Success, Error
- Type-safety avec les sealed classes
- Gestion cohérente des erreurs dans toute l'app

```kotlin
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val exception: Exception? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
```

### 3. **StateFlow pour la Réactivité**

**Décision** : Utilisation de `StateFlow` pour exposer l'état UI depuis les ViewModels.

**Avantages** :
- État observable et réactif
- Intégration native avec Compose (`collectAsState()`)
- Gestion automatique du cycle de vie

### 4. **Pagination Infinie**

**Décision** : Implémentation d'une pagination infinie avec détection de scroll.

**Implémentation** :
- Détection automatique de fin de liste
- Chargement progressif des pages
- Indicateurs visuels de chargement

```kotlin
@Composable
fun InfiniteScrollDetector(
    listState: LazyGridState,
    buffer: Int = 2,
    onLoadMore: () -> Unit,
    hasMoreData: Boolean
) { /* ... */ }
```

### 5. **Injection de Dépendances avec Hilt**

**Décision** : Utilisation de Dagger-Hilt pour l'injection de dépendances.

**Configuration** :
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit { /* ... */ }
    
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService { /* ... */ }
}
```

### 6. **Navigation Simple**

**Décision** : Navigation basée sur l'état local plutôt que Navigation Component.

**Justification** :
- Application simple avec 2 écrans
- Évite la complexité supplémentaire
- Navigation par état dans le composable principal

### 7. **Gestion des Erreurs**

**Décision** : Gestion robuste des erreurs réseau et de parsing.

**Stratégies** :
- Retry automatique sur les erreurs
- Messages d'erreur utilisateur-friendly
- Fallbacks pour les images manquantes

## 🎨 Interface Utilisateur

### Design System

- **Material Design 3** : Design moderne et cohérent
- **Couleurs dynamiques** : Support des couleurs système Android 12+
- **Typography** : Hiérarchie typographique claire
- **Adaptive UI** : Interface responsive pour différentes tailles d'écran

### Composants Principaux

1. **ShowListScreen** : Grille adaptive avec `LazyVerticalGrid`
2. **ShowDetailsScreen** : Interface détaillée avec hero image
3. **TvShowItem** : Carte de série avec image et métadonnées
4. **LoadingMoreItem** : Indicateur de chargement pagination

## 🧪 Tests et Qualité

### Recommandations pour les Tests

```kotlin
// Exemple de test ViewModel
@Test
fun `when getMostPopularShows succeeds, should emit success state`() = runTest {
    // Given
    val mockShows = listOf(/* mock data */)
    coEvery { repository.getMostPopularShows() } returns flowOf(Resource.success(mockShows))
    
    // When
    viewModel.loadShows()
    
    // Then
    verify { uiState.value is UiState.Success }
}
```

### Outils Recommandés

- **Unit Tests** : JUnit, MockK
- **UI Tests** : Espresso, Compose Testing
- **Détection de fuites** : LeakCanary
- **Analyse statique** : Lint, Detekt

## 🚀 Améliorations Futures

### Fonctionnalités Envisagées

1. **Recherche** : Barre de recherche avec suggestions
2. **Favoris** : Système de favoris avec persistance locale
3. **Mode hors-ligne** : Cache local avec Room
4. **Notifications** : Alertes pour nouveaux épisodes
5. **Partage** : Partage de séries sur réseaux sociaux

### Optimisations Techniques

1. **Performance** :
   - Pagination plus intelligente
   - Cache d'images optimisé
   - Lazy loading avancé

2. **Architecture** :
   - Migration vers Navigation Compose
   - Implémentation de UseCase layer
   - Cache multi-niveaux

3. **UX/UI** :
   - Animations de transition
   - Mode sombre adaptatif
   - Accessibilité améliorée

## 📊 Métriques et Performance

### Objectifs de Performance

- **Temps de démarrage** : < 2 secondes
- **Chargement liste** : < 1 seconde
- **Navigation** : < 500ms
- **Chargement images** : Progressive avec placeholder

### Monitoring Recommandé

- Firebase Analytics pour l'usage
- Crashlytics pour les erreurs
- Performance Monitoring

## 📞 Support et Contribution

### Structure du Code

- Code commenté en français
- Nommage explicite des variables et fonctions
- Architecture modulaire et extensible

### Conventions

- **Kotlin** : Conventions officielles JetBrains
- **Compose** : Guidelines Material Design 3
- **Git** : Conventional Commits

---

## 🏆 Conformité au Cahier des Charges

✅ **Architecture MVVM** : Implémentation complète  
✅ **Jetpack Compose** : Interface 100% Compose  
✅ **Retrofit** : Consommation API EpisoDate  
✅ **Coil** : Chargement d'images optimisé  
✅ **Dagger-Hilt** : Injection de dépendances  
✅ **Coroutines** : Programmation asynchrone  
✅ **Gestion d'états** : Loading, Success, Error  
✅ **Pagination** : Chargement infini  

**Bonus implémentés** :
- Navigation vers écran de détails
- Retry automatique sur erreurs

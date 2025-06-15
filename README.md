# 🛒 NewStore - Android E-commerce Application

A modern Android e-commerce application built with **Clean Architecture**, **MVVM pattern**, and **traditional Android Views**. This app demonstrates best practices in Android development with offline-first capabilities, real-time data synchronization, and comprehensive user management.

## 📱 Features

- **🏠 Home Dashboard**: Main navigation with Products and Settings access
- **🛍️ Product Management**: Browse, search, and manage product catalog
- **👤 User Authentication**: Firebase-based login/signup with real-time validation
- **🛒 Shopping Cart**: Add products, manage quantities, and proceed to checkout
- **📦 Order Management**: Track orders, view history, and manage order status
- **👥 User Management**: Admin functionality for user account management
- **💾 Offline-First**: Room database with local caching and background sync
- **🔄 Real-time Data Sync**: Automatic synchronization with remote server
- **📊 Analytics**: Firebase Crashlytics integration for monitoring
- **⏰ Background Operations**: Daily product sync using WorkManager and AlarmManager
- **🏗️ Clean Architecture**: Proper separation of concerns with Use Cases, Repositories, and Data Sources

## 🏗️ Clean Architecture Implementation

This project strictly follows **Clean Architecture** principles with clear separation of concerns:

### **Layer Structure**
- **Presentation Layer**: Fragments, ViewModels, Adapters
- **Domain Layer**: Use Cases, Repository Interfaces, Domain Models
- **Data Layer**: Repository Implementations, Data Sources, Room Database, API Services

### **Dependency Rule**
- **Dependencies point inward**: Presentation → Domain ← Data
- **Domain layer is independent**: No Android or external dependencies
- **Inversion of Control**: Using Dagger Hilt for dependency injection

### **MVVM Pattern Implementation**
- **Model**: Data from Repository (Room + Retrofit)
- **View**: Fragments with XML layouts
- **ViewModel**: Business logic and state management
- **LiveData**: Reactive data observation
- **Data Binding**: Connecting UI with data

## 🔄 Offline-First Architecture

### **Data Synchronization Strategy**
- **Single Source of Truth**: Room Database
- **Repository Pattern**: Coordinates between local and remote data
- **Cache-First Approach**: Always show cached data first
- **Background Sync**: Daily automatic synchronization at 12:00 AM
- **Conflict Resolution**: Remote data overwrites local on sync

### **WorkManager Integration**
- **Daily Product Sync**: Scheduled background work
- **Network-Aware**: Only syncs when connected
- **Battery Optimized**: Uses WorkManager constraints
- **Retry Logic**: Automatic retry on failure

## 📂 Project Structure

```
app/
├── 📁 base/                          # Base classes
│   ├── BaseDialogFragment
│   └── BaseFragment
├── 📁 common/                        # Common utilities
│   ├── DataStatus
│   └── Result.kt
├── 📁 constants/                     # App constants
│   ├── ApiConstants
│   ├── Constants
│   └── DBConstants
├── 📁 extensions/                    # Kotlin extensions
│   └── ScopeExtensions.kt
├── 📁 utils/                         # Utility classes
│   ├── AppLogger
│   ├── AppUtils
│   ├── DialogUtils.kt
│   ├── ExtensionUtils.kt
│   └── ToastUtils.kt
└── 📁 data/                          # Data layer
    ├── 📁 local/                     # Local data source
    │   ├── 📁 dao/                   # Room DAOs
    │   │   ├── AddToCartDao
    │   │   ├── CartSaleDao
    │   │   ├── OrderDao
    │   │   ├── ProductDao
    │   │   └── UserDao
    │   ├── 📁 entity/                # Room entities
    │   │   ├── AddToCartEntity.kt
    │   │   ├── CartSaleEntity
    │   │   ├── OrderEntity
    │   │   ├── ProductEntity.kt
    │   │   ├── UserEntity.kt
    │   │   ├── Convertors.kt
    │   │   └── StoreDataBase
    │   └── 📁 model/                 # Data models
    │       ├── Product.kt
    │       └── User.kt
    ├── 📁 network/                   # Network layer
    │   ├── 📁 Apirequest/
    │   ├── 📁 apiresponse/           # API responses
    │   │   ├── BaseResponse
    │   │   ├── ProductResponse
    │   │   └── UserResponse
    │   ├── StoreApi
    │   └── UserApi
    └── 📁 repository/                # Repository implementations
        ├── 📁 dataSource/            # Data source interfaces
        │   ├── AddToCartLocalDataSource
        │   ├── CartSaleLocalDataSource
        │   ├── OrderLocalDataSource
        │   ├── ProductLocalDataSource
        │   ├── ProductRemoteDataSource
        │   ├── UserLocalDataSource
        │   └── UserRemoteDataSource
        ├── 📁 dataSourceImpl/        # Data source implementations
        │   ├── AddToCartLocalDataSourceImpl
        │   ├── CartSaleLocalDataSourceImpl
        │   ├── OrderLocalDataSourceImpl
        │   ├── ProductLocalDataSourceImpl
        │   ├── ProductRemoteDataSourceImpl
        │   ├── UserLocalDataSourceImpl
        │   └── UserRemoteDataSourceImpl
        ├── AddToCartRepositoryImpl
        ├── OrderRepositoryImpl
        ├── ProductRepositoryImpl
        └── UserRepositoryImpl
    └── 📁 util/
└── 📁 domain/                        # Domain layer
    ├── 📁 repository/                # Repository interfaces
    │   ├── AddToCartRepository
    │   ├── OrderRepository
    │   ├── ProductRepository
    │   └── UserRepository
    └── 📁 useCase/                   # Use cases
        ├── AddToCartUseCase
        ├── OrderUseCase
        ├── ProductUseCase
        └── UserUseCase
└── 📁 presentation/                  # Presentation layer
    ├── 📁 adapter/                   # RecyclerView adapters
    │   ├── CartsAdapter
    │   ├── CheckOutAdapter
    │   ├── OrderAdapter
    │   ├── ProductAdapter
    │   └── UserAdapter
    ├── 📁 di/                        # Dependency injection
    │   ├── ApiModule
    │   ├── AppModule
    │   ├── DataSourceModule
    │   ├── DbModule
    │   └── RepositoryModule
    └── 📁 ui/                        # UI components
        ├── 📁 auth/                  # Authentication screens
        │   ├── LoginFragment
        │   └── SignUpFragment
        ├── 📁 carts/                 # Cart management
        │   ├── AddToCartViewModel
        │   └── CartsFragment
        ├── 📁 checkout/              # Checkout process
        │   └── CheckOutFragment
        ├── 📁 Home/                  # Home screen
        │   └── HomeFragment
        ├── 📁 order/                 # Orders management
        │   ├── OrderDetailsFragment
        │   ├── OrderFragment
        │   └── OrderViewModel
        ├── 📁 payment/               # Payment processing
        │   └── PaymentFragment
        ├── 📁 products/              # Product management
        │   ├── AddProductDialogFragment
        │   ├── ProductDetailsFragment
        │   ├── ProductFragment
        │   └── ProductsViewModel
        └── 📁 settings/              # App settings
            └── SettingsFragment
```

## 🔄 Data Flow Architecture

### 1. **App Navigation Flow**
```
Authentication → Home → Products/Settings → Feature Screens → Actions
     ↓              ↓         ↓                    ↓            ↓
Login/Signup → Home Screen → Products/Settings → Detail Views → Buy/Cart
```

### 2. **User Interaction Flow**
```
User Input → Fragment → ViewModel → Use Case → Repository → Data Source → API/Database
     ↓                                                                         ↓
UI Update ← Observer ← LiveData ← Result ← Repository ← Response ← API/Database
```

### 3. **Settings Module Structure**
```
Settings Screen
├── 🛒 Carts Management
│   ├── View cart items
│   ├── Update quantities
│   └── Proceed to checkout
├── 📦 Orders Management
│   ├── Order history
│   ├── Order details
│   └── Order status tracking
└── 👥 Users Management
    ├── User profiles
    ├── Account settings
    └── User authentication
```

## 🛠️ Tech Stack

### **Core Technologies**
- **Kotlin** - Primary programming language
- **Android SDK** - Traditional Android development
- **Clean Architecture** - Separation of concerns
- **MVVM Pattern** - Presentation layer architecture
- **Fragments** - UI components
- **XML Layouts** - UI design

### **Dependency Injection**
- **Dagger Hilt** - Dependency injection framework

### **Database & Storage**
- **Room Database** - Local data persistence
- **SharedPreferences** - Simple data storage

### **Networking**
- **Retrofit** - HTTP client
- **OkHttp** - Network interceptor
- **Gson** - JSON parsing

### **Background Processing**
- **WorkManager** - Background task scheduling
- **AlarmManager** - Scheduled operations (daily sync at 12:00)

### **Firebase Services**
- **Firebase Authentication** - User authentication
- **Firebase Crashlytics** - Crash reporting and analytics
- **Firebase Realtime Database** - Real-time data sync

### **UI Components**
- **Fragment Navigation** - Screen navigation
- **RecyclerView** - List presentations
- **Material Design** - UI components

## 🎯 Key Learning Outcomes

This project demonstrates mastery of:

### **Android Development Fundamentals**
- **Clean Architecture** implementation in Android
- **MVVM pattern** with LiveData and Data Binding
- **Fragment-based navigation** and lifecycle management
- **RecyclerView** with custom adapters and ViewHolders

### **Database Management**
- **Room Database** setup and configuration
- **Entity relationships** and foreign keys
- **DAO patterns** for data access
- **Database migrations** and versioning
- **Type converters** for complex data types

### **Network Operations**
- **Retrofit** configuration and API integration
- **HTTP methods** (GET, POST, PUT, DELETE) implementation
- **Error handling** and network state management
- **JSON parsing** with Gson
- **API response handling** with proper data models

### **Dependency Injection**
- **Dagger Hilt** setup and configuration
- **Module creation** for different components
- **Scoping** and lifecycle management
- **Interface injection** for testability

### **Background Processing**
- **WorkManager** for background tasks
- **AlarmManager** for scheduled operations
- **Foreground services** for long-running tasks
- **Background sync** strategies

### **Firebase Integration**
- **Firebase Authentication** for user management
- **Realtime Database** for live data sync
- **Crashlytics** for crash reporting
- **Performance monitoring** and analytics

## 💡 Project Highlights

### **Navigation Architecture**
- **Home-Centered Design**: Clean main dashboard approach
- **Feature Segregation**: Products and Settings as main modules
- **Intuitive Flow**: Logical user journey from browsing to purchasing
- **Settings Integration**: All management features centralized in settings

### **E-commerce Functionality**
- **Complete Shopping Experience**: From product browsing to order completion
- **Flexible Purchase Options**: Both immediate buying and cart-based shopping
- **Admin Features**: User and order management through settings
- **Real-time Updates**: Live synchronization of cart and order data

### **Technical Excellence**
- **Clean Architecture**: Proper separation of concerns across all layers
- **MVVM Implementation**: Robust presentation layer architecture
- **Dependency Injection**: Full Dagger Hilt integration for testability
- **Database Design**: Efficient Room database with proper relationships
- **API Integration**: Complete CRUD operations with Retrofit
- **Background Processing**: Smart synchronization with WorkManager

### **Performance & UX**
- **Offline-First**: Complete functionality without internet connectivity
- **Smart Caching**: Efficient data management and storage
- **Responsive Design**: Smooth user interactions and transitions
- **Error Handling**: Comprehensive error management and user feedback
- **Loading States**: Clear indication of all ongoing operations

## 🔧 Setup Instructions

### **Prerequisites**
- Android Studio Arctic Fox or later
- JDK 11 or higher
- Android SDK 21+
- Firebase project setup

### **Installation Steps**

1. **Clone the Repository**
```bash
git clone https://github.com/vasireddysrimanth/NewStore.git
cd NewStore
git checkout develop
```

2. **Firebase Configuration**
- Create a new Firebase project
- Add your Android app to Firebase
- Download `google-services.json`
- Place it in the `app/` directory

3. **API Configuration**
- Update base URL in `ApiConstants`
- Configure API endpoints
- Set up authentication tokens

4. **Build and Run**
```bash
./gradlew clean build
./gradlew installDebug
```
Or simply run from Android Studio

## 📊 App Flow Diagram

```
┌─────────────────┐
│   Splash Screen │
└─────────────────┘
         │
         ▼
┌─────────────────┐
│   Login/Signup  │
└─────────────────┘
         │
         ▼
┌─────────────────┐
│   Home Screen   │
└─────────────────┘
         │
    ┌────┴────┐
    ▼         ▼
┌─────────┐ ┌─────────┐
│Products │ │Settings │
└─────────┘ └─────────┘
     │           │
     ▼           └─────┬─────────┬─────────┐
┌─────────────┐       ▼         ▼         ▼
│Product      │   ┌───────┐ ┌───────┐ ┌───────┐
│Details      │   │ Carts │ │Orders │ │ Users │
└─────────────┘   └───────┘ └───────┘ └───────┘
     │                │         │         │
     └─────┬─────┐    ▼         ▼         ▼
           ▼     ▼  ┌─────────┐ ┌─────────┐ ┌─────────┐
    ┌───────────┐  │Checkout │ │ Order   │ │  User   │
    │  Buy Now  │  │Process  │ │Details  │ │Management│
    └───────────┘  └─────────┘ └─────────┘ └─────────┘
           │            │
           ▼            ▼
    ┌───────────┐  ┌───────────┐
    │  Payment  │  │  Payment  │
    └───────────┘  └───────────┘
```

## 🧪 Testing Strategy

### **Unit Tests**
- Repository implementations
- Use case logic
- Data transformations
- Business logic validation

### **Integration Tests**
- Database operations
- API interactions
- Data flow between layers

### **UI Tests**
- User interaction flows
- Screen navigation
- Form validations

## 📈 Performance Optimizations

### **Database Optimizations**
- Proper indexing on frequently queried columns
- Lazy loading for large datasets
- Efficient pagination implementation

### **Network Optimizations**
- Request/response caching
- Image loading optimization
- Background sync scheduling

### **Memory Management**
- ViewHolder pattern in adapters
- Proper lifecycle management
- Image memory optimization

## 🔐 Security Considerations

- **Firebase Authentication** for secure user management
- **Token-based** API authentication
- **Data encryption** for sensitive information
- **Network security** with certificate pinning
- **Input validation** and sanitization

## 📱 Supported Android Versions

- **Minimum SDK**: 21 (Android 5.0 Lollipop)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developer

**Srimanth Vasireddy**
- GitHub: [@vasireddysrimanth](https://github.com/vasireddysrimanth)
- Email: vasireddysrimanth@gmail.com

## 🙏 Acknowledgments

- Android development team for excellent architecture components
- Firebase team for robust backend services
- Material Design team for beautiful UI guidelines
- Open source community for valuable libraries and tools

---

**Built with ❤️ by Srimanth Vasireddy**

# Spotify Search App

**Overview**<br>
This project aims to create a search page utilizing Spotify's search API to allow users to search for albums, artists, playlists, and tracks. 
It utilizes the Single Activity Multiple Fragments architecture,
making use of Kotlin, Coroutine, Flow, Dagger Hilt, Sealed class, Enum, Retrofit, and Room for caching to ensure offline support.<br>

<img src="https://github.com/Bhupendrapatel98/Grow-Task/assets/55411086/e2128a97-a048-4d26-b97e-34d5c02b67af" alt="Image 1" width="100">

# Project Structure
 **data**: This directory manages data operations.
    - **api**: Houses the ApiService for making network requests.
    - **model**: Contains interceptors for network requests.
    - **local**: Handles local data storage, including converters, DAO interfaces, and database setup.
    - **model**: Stores Plain Old Java Objects (POJOs) used for data modeling.
    - **repository**: Contains repositories responsible for data management, including the search repository, user detail repository, and token repository.
 **di**: This directory handles Dependency Injection using Dagger Hilt.
    -  **AppModule.kt**: Defines application-level dependencies such as Retrofit.
 **ui**: This directory holds user interface-related components.
    -  **view**: Contains activities and fragments.
    -  **viewmodel**: Houses ViewModels responsible for managing UI-related data.
 **util**: This directory contains utility classes and resources.
    -  **constant**: Stores constant values used throughout the application.
    -  **networkutil**: Contains utilities for network operations.
    -  **Resource**: Handles resource management.
    -  **BaseApplication**: The Application class responsible for Dagger setup and initialization.

# Libraries Used
**Kotlin**: Primary language for development.<br>
**Coroutine**: For asynchronous and non-blocking programming.<br>
**Flow**: For reactive data streams.<br>
**Dagger Hilt**: For dependency injection.<br>
**Retrofit**: For making network requests.<br>
**Room**: For local caching and offline support.<br>

# Architecture
The application follows the Single Activity Multiple Fragments architecture using Navigation Component. Each feature is divided into fragments, 
making the app modular and scalable.<br>

# Caching Strategy
Room Database is used for caching search results. Each time a user performs a search, the results are saved in the database.
When offline or open the app, the app displays the last searched results fetched from the database.

# Sealed Class and Enum
Sealed class and Enum are utilized for managing different types of search results (albums, artists, playlists, tracks) and their respective details.

# Dependency Injection
Dependency Injection is implemented using Dagger Hilt for better modularization, testability, and maintainability of the codebase.

# Conclusion
The project successfully implements a search feature using Spotify's API with offline support and follows best practices in terms of architecture, 
code quality, and efficiency. Further enhancements and optimizations can be made to make the app more robust and user-friendly.


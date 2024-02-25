package com.instagram_clone.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.instagram_clone.repos.AuthRepository
import com.instagram_clone.repos.AuthRepositoryImpl
import com.instagram_clone.repos.FireStoreRepository
import com.instagram_clone.repos.FireStoreRepositoryImpl
import com.instagram_clone.repos.StorageRepository
import com.instagram_clone.repos.StorageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFireStoreRepository(impl: FireStoreRepositoryImpl): FireStoreRepository = impl

    @Provides
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideStorageRepository(impl: StorageRepositoryImpl): StorageRepository = impl
}
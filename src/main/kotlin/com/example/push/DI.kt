package com.example.push

import com.example.core.AppConfig
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.module
import java.io.FileInputStream

val repositoryModules = module {
    single { Repository() }
}

val controllerModules = module {
    single { Controller(get()) }
}

fun provideFirebaseMessaging(): FirebaseMessaging {
    val googleCredentials = GoogleCredentials.fromStream(FileInputStream(AppConfig.Firebase.firebaseSA))
    val firebaseOptions = FirebaseOptions.builder()
        .setCredentials(googleCredentials)
        .build()
    val firebaseApp = FirebaseApp.initializeApp(firebaseOptions)
    return FirebaseMessaging.getInstance(firebaseApp)
}

val fcmModules = module {
    single {
        provideFirebaseMessaging()
    }
}

val providerModules = module {
    single<PushProvider> {
        FCMProvider(fcm = get(), repository = get())
    }
}

val pushModules = listOf(repositoryModules, controllerModules, fcmModules, providerModules)
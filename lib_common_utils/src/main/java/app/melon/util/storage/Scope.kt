package app.melon.util.storage

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class RegistrationStorage

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class LoginStorage

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationStorage
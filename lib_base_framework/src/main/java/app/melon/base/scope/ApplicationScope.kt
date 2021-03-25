package app.melon.base.scope

import javax.inject.Scope

/**
 * Dagger scope for dependencies that should only have a single instance created for the entire
 * application.
 */
@Scope
@MustBeDocumented
annotation class ApplicationScope 
package app.melon.base.scope

import javax.inject.Scope

/**
 * Dagger scope for dependencies that should only have a single instance created per activity.
 */
@Scope
@MustBeDocumented
annotation class ActivityScope 
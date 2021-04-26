package app.melon.composer.di

import dagger.Module


@Module(
    includes = [
        ComposerBuilder::class
    ]
)
class ComposerModule
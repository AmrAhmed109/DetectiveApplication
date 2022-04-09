object DI {
    private const val koin_version = "3.1.2"
    //koin
    const val koinCore = "io.insert-koin:koin-core:$koin_version"
    const val koinTest = "io.insert-koin:koin-test:$koin_version"
    const val koinJUnit4 = "io.insert-koin:koin-test-junit4:$koin_version"
    const val koinJUnit5 = "io.insert-koin:koin-test-junit5:$koin_version"
    const val koinAndroid = "io.insert-koin:koin-android:$koin_version"
    const val koinWorkManager = "io.insert-koin:koin-androidx-workmanager:$koin_version"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:$koin_version"
    const val koinViewModel = "io.insert-koin:koin-android-viewmodel:$koin_version"

    //hilt
    const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT_VERSION}"
    const val hiltFragment = ("androidx.hilt:hilt-navigation-fragment:1.0.0")
    const val hiltCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.HILT_VERSION}"
    const val hiltTesting = "com.google.dagger:hilt-android-testing:${Versions.HILT_VERSION}"
    const val hiltAndroidCompiler =
        "com.google.dagger:hilt-android-compiler:${Versions.HILT_VERSION}"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.HILT_VERSION}"
    const val hiltAnnotation = "androidx.hilt:hilt-compiler:${Versions.HILT_VERSION}"
}
object TestDi {
    const val truth = "com.google.truth:truth:1.1.3"

    const val hiltTesting = "com.google.dagger:hilt-android-testing:${Versions.HILT_VERSION}"

    const val testRunner = "androidx.test:runner:${Versions.TEST_RUNNER_VERSION}"
    const val androidCoreTesting =
        "androidx.arch.core:core-testing:${Versions.ANDROIDX_CORE_Testing_VERSION}"

    const val junit4 = "junit:junit:${Versions.TEST_JUNIT_VERSION}"
    const val junitExt = "androidx.test.ext:junit:1.1.2"

    const val fragmentTest =
        "androidx.fragment:fragment-testing:${Versions.ANDROIDX_FRAGMENT_TESTING_VERSION}"
    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_VERSION}"

    const val espressoCore = ("androidx.test.espresso:espresso-core:3.4.0")
    const val testRules = ("androidx.test:rules:1.4.0")

    val testlib_junit = "junit:junit:${Versions.junit}"
    val testandroidx_junit = "androidx.test.ext:junit:${Versions.androidx_ext_junit}"
    val testandroidx_runner = "androidx.test:runner:${Versions.androidx_testing}"
    val testandroidx_rules = "androidx.test:rules:${Versions.androidx_testing}"
    val fragments_testing = "androidx.fragment:fragment-testing:${Versions.fragments_testing}"
    val androidx_testing_core = "androidx.test:core:${Versions.testing_core}"
    val koin_test = "org.koin:koin-test:${Versions.koin}"
    val mockk = "io.mockk:mockk:${Versions.mockk}"
    val mockk_android = "io.mockk:mockk-android:${Versions.mockk}"
    const val livedata_testing = "com.jraska.livedata:testing-ktx:${Versions.livedata_testing_lib}"
    val coroutines_testing =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_test}"
    val arch_core_testing =
        "androidx.arch.core:core-testing:${Versions.arch_core_testing}"


    val testLibs = arrayOf(
        arch_core_testing,
        coroutines_testing,
        mockk,
        koin_test,
        livedata_testing,
        testlib_junit
    )

    val uiTestLibs = arrayOf(
        testandroidx_runner,
        testandroidx_junit,
        testandroidx_rules,
        arch_core_testing,
        coroutines_testing,
        mockk_android,
        koin_test,
        livedata_testing
    )
}



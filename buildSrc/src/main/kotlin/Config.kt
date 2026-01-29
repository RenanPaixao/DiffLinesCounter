object Config {
    const val pluginGroup: String = "com.fappslab"
    const val pluginName: String = "LinesDiffTracker"
    const val pluginVersion: String = "2.3.0"
    const val pluginSinceBuild: String = "222"
    const val pluginUntilBuild: String = "241.*"

    const val platformType: String = "IC" // Target IDE Platform
    const val platformVersion: String = "2024.1"
    const val shouldDownloadPlatformSources: Boolean = true
    const val shouldAutoUpdateSinceUntilBuild: Boolean = false

    val testedIdeVersions: List<String> = listOf(
        "2022.2",  // Build 222
        "2022.3",  // Build 223
        "2023.1",  // Build 231
        "2023.2",  // Build 232
        "2023.3",  // Build 233
        "2024.1"   // Build 241
    )
}

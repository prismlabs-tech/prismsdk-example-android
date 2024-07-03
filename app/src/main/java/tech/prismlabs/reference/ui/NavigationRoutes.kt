package tech.prismlabs.reference.ui

enum class Screen {
    SPLASH,
    SCAN_LIST,
    SCAN_DETAILS
}

enum class OnboardingScreen {
    USER_INFO,
    PRIVACY,
    PREPARE,
}

sealed class NavigationItem(val route: String) {
    data object Splash : NavigationItem(Screen.SPLASH.name)
    data object ScanList : NavigationItem(Screen.SCAN_LIST.name)
    data object ScanDetails : NavigationItem(Screen.SCAN_DETAILS.name)

    sealed class Onboarding(route: String) : NavigationItem(route) {
        data object UserInfo : Onboarding(OnboardingScreen.USER_INFO.name)
        data object Privacy : Onboarding(OnboardingScreen.PRIVACY.name)
        data object Prepare : Onboarding(OnboardingScreen.PREPARE.name)
    }
}

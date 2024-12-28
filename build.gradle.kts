// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false


}
configurations.all {
    resolutionStrategy {
        force ("com.android.support:support-v4:27.1.1")
    }
}
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:6.0.3")
    testImplementation("junit:junit:4.13")}

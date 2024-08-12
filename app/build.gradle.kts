plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("io.realm.kotlin")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.nomaddeveloper.examini"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nomaddeveloper.examini"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "GEMINI_API_KEY",
                "\"${project.findProperty("GEMINI_API_KEY")}\""
            )
            buildConfigField(
                "String",
                "WEB_CLIENT_ID",
                "\"396725680907-n7ph3le7flufv06hqltk50hthv772aca.apps.googleusercontent.com\""
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "GEMINI_API_KEY",
                "\"${project.findProperty("GEMINI_API_KEY")}\""
            )
            buildConfigField(
                "String",
                "WEB_CLIENT_ID",
                "\"396725680907-n7ph3le7flufv06hqltk50hthv772aca.apps.googleusercontent.com\""
            )
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            applicationIdSuffix = ".dev"
            buildConfigField(
                "String",
                "BASE_URL",
                "\"http://dev-universitehazirlik.eu-central-1.elasticbeanstalk.com/api/v1/\""
            )
            buildConfigField("boolean", "SKIP_LOGIN", "true")
            buildConfigField(
                "String",
                "CLIENT_ID",
                "\"396725680907-ki93q5v98cvbdj5s3rgqt910ih9b194m.apps.googleusercontent.com\""
            )
            manifestPlaceholders["app_name"] = "Examini Dev"
        }
        create("prod") {
            dimension = "version"
            applicationIdSuffix = ""
            buildConfigField(
                "String",
                "BASE_URL",
                "\"http://dev-universitehazirlik.eu-central-1.elasticbeanstalk.com/api/v1/\""
            )
            buildConfigField("boolean", "SKIP_LOGIN", "false")
            buildConfigField(
                "String",
                "CLIENT_ID",
                "\"396725680907-c9n42i9vvnmd92540766kafu7s1lnfh3.apps.googleusercontent.com\""
            )
            manifestPlaceholders["app_name"] = "Examini"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    //OkHttp
    implementation(libs.okhttp)
    //Glide
    implementation(libs.glide)
    ksp(libs.ksp)
    //Gemini AI
    implementation(libs.generativeai)
    implementation(libs.common)
    //Realm
    implementation(libs.library.base)
    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    //Google Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    //Biometric
    implementation(libs.androidx.biometric)
    //Gson
    implementation(libs.gson)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
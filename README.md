![moko-biometry](https://user-images.githubusercontent.com/5010169/128705751-e76a78a4-e367-4d4f-a643-b90e250a6f22.png)  
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Download](https://img.shields.io/maven-central/v/dev.icerock.moko/biometry) ](https://repo1.maven.org/maven2/dev/icerock/moko/biometry) ![kotlin-version](https://kotlin-version.aws.icerock.dev/kotlin-version?group=dev.icerock.moko&name=biometry)

# Mobile Kotlin biometry

This is a Kotlin Multiplatform library that provides authentication by FaceId and TouchId(Fingerprint)

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Samples](#samples)
- [Set Up Locally](#setup-locally)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Biometric user authentication** - allows you to use familiar user authentication methods from business logic
- **Compose Multiplatform** support (partly, mobile platforms: Android, iOS)

## Requirements

- Gradle version 6.8+
- Android API 16+
- iOS version 11.0+

## Installation

root build.gradle

```groovy
allprojects {
    repositories {
        mavenCentral()
    }
}
```

project build.gradle

```groovy
dependencies {
    commonMainApi("dev.icerock.moko:biometry:0.4.0")

    // Compose Multiplatform
    commonMainApi("dev.icerock.moko:biometry-compose:0.4.0")

    // Jetpack Compose (only for android, if you don't use multiplatform)
    implementation("dev.icerock.moko:biometry-compose:0.4.0")
}
```

## Usage

**common**

In `commonMain` we should create `ViewModel` like:

```kotlin
class SampleViewModel(
    val biometryAuthenticator: BiometryAuthenticator
) : ViewModel() {

    fun tryToAuth() = viewModelScope.launch {
        try {
            val isSuccess = biometryAuthenticator.checkBiometryAuthentication(
                requestTitle = "Biometry".desc(),
                requestReason = "Just for test".desc(),
                failureButtonText = "Oops".desc(),
                allowDeviceCredentials = false // true - if biometric permission is not granted user can authorise by device creds
            )

            if (isSuccess) {
                // Do something onSuccess
            }
        } catch (throwable: Throwable) {
            // Do something onFailed
        }
    }
}
```

After create ViewModel, let's integrate on platform.

**Android**

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SampleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create viewModel from common code.
        viewModel = getViewModel {
            SampleViewModel(
                // Pass platform implementation of the Biometry Authenticator
                // to a common code
                biometryAuthenticator = BiometryAuthenticator(
                    applicationContext = applicationContext
                )
            )
        }

        // Binds the Biometry Authenticator to the view lifecycle
        viewModel.biometryAuthenticator.bind(
            lifecycle = this@MainActivity.lifecycle,
            fragmentManager = supportFragmentManager
        )
    }
}
```

**Compose:**

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContent {
            val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()

            // Create viewModel from common code
            val viewModel = getViewModel {
                SampleViewModel(
                    // Pass platform implementation of the Biometry Authenticator
                    // to a common code
                    biometryAuthenticator = biometryFactory.createBiometryAuthenticator()
                )
            }

            // Binds the Biometry Authenticator to the view lifecycle
            BindBiometryAuthenticatorEffect(viewModel.biometryAuthenticator)

            // Same screen content here
        }
    }
}
```

**iOS:**

```swift
class SampleViewController: UIViewController {
    
    private var viewModel: SampleViewModel!
        
    override func viewDidLoad() {
        super.viewDidLoad()

        self.viewModel = SampleViewModel(
            biometryAuthenticator: BiometryBiometryAuthenticator(),
        )
    }
    
    @IBAction private func loginAction() {
        self.viewModel.tryToAuth()
    }
}
```

Additionally, you need add `NSFaceIDUsageDescription` key in Info.plist of your project:

```swift
<key>NSFaceIDUsageDescription</key>
<string>$(PRODUCT_NAME) Authentication with TouchId or FaceID</string>
```

**Compose Multiplatform:**

```kotlin
@Composable
fun BiometryScreen() {
    val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()
    
    BiometryScreen(
        viewModel = getViewModel(
            key = "biometry-screen",
            factory = viewModelFactory {
                BiometryViewModel(
                    biometryAuthenticator = biometryAuthenticatorFactory.createBiometryAuthenticator()
                )
            }
        )
    )
}

@Composable
private fun BiometryScreen(
    viewModel: BiometryViewModel
) = NavigationScreen(title = "moko-biometry") { paddingValues ->
    BindBiometryAuthenticatorEffect(viewModel.biometryAuthenticator)

    val text: String by viewModel.result.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)

        Button(onClick = viewModel::onButtonClick) {
            Text(text = "Click on me")
        }
    }
}
```

## Samples

Please see more examples in the [sample directory](sample).

## Set Up Locally

- The [biometry directory](biometry) contains the `biometry` library;
- The [sample directory](sample) contains sample apps for Android and iOS; plus the mpp-library connected to the apps.

## Contributing

All development (both new features and bug fixes) is performed in the `develop` branch. This way `master` always
contains the sources of the most recently released version. Please send PRs with bug fixes to the `develop` branch.
Documentation fixes in the markdown files are an exception to this rule. They are updated directly in `master`.

The `develop` branch is pushed to `master` on release.

For more details on contributing please see the [contributing guide](CONTRIBUTING.md).

## License

    Copyright 2021 IceRock MAG Inc.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

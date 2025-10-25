package az.onda.project

import androidx.compose.ui.window.ComposeUIViewController
import az.onda.di.initKoinModule

fun MainViewController() = ComposeUIViewController(
    configure = { initKoinModule () }
) { App() }
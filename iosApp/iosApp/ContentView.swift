import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        // Register the Kotlin iOS analytics logger so screen events are printed on iOS
        let deviceName = UIDevice.current.name
        let osVersion = UIDevice.current.systemVersion
        AnalyticsIosKt.registerIosAnalytics(deviceName: deviceName, osVersion: osVersion)
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}

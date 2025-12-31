package com.sadeghtahani.notebox

import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIWindow
import platform.UIKit.popoverPresentationController

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun openFile(path: String) {
    val fileUrl = NSURL.fileURLWithPath(path)
    val activityController = UIActivityViewController(
        activityItems = listOf(fileUrl),
        applicationActivities = null
    )

    val keyWindow = UIApplication.sharedApplication.windows
        .mapNotNull { it as? UIWindow }
        .firstOrNull { it.isKeyWindow() }
        ?: UIApplication.sharedApplication.keyWindow

    val rootViewController = keyWindow?.rootViewController

    var topController = rootViewController
    while (topController?.presentedViewController != null) {
        topController = topController.presentedViewController
    }

    activityController.popoverPresentationController?.sourceView = topController?.view

    topController?.presentViewController(activityController, animated = true, completion = null)
}

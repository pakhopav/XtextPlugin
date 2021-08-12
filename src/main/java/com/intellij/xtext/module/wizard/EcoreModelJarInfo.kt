package com.intellij.xtext.module.wizard

import java.io.File
import java.util.jar.JarFile

class EcoreModelJarInfo(
    val uri: String,
    var path: String? = null,
    var targetPath: String? = null,
    var jarFile: JarFile? = null,
    var file: File? = null,
    var byUser: Boolean = false
) {
}
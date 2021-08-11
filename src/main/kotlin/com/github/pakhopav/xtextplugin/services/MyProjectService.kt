package com.github.pakhopav.xtextplugin.services

import com.github.pakhopav.xtextplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

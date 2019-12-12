package com.mrcd.language

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class LanguagePluginEnter implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getExtensions().getByType(AppExtension).registerTransform(new LanguageTransform(project))
    }
}

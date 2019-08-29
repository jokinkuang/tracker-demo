package com.uc.plugin

import com.android.build.gradle.AppExtension
import com.uc.plugin.transform.MainTransform
import org.gradle.api.Project
import org.gradle.api.Plugin

class PluginEntry implements Plugin<Project> {
    @Override
    void apply(Project project) {

        if(! project.android) {
            throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!');
        }

        PluginSettings.apply(project);

        project.android.applicationVariants.all{
            variant ->
                variant.outputs.all{
                    outputFileName = "${variant.name}-${variant.versionName}.apk"
                }
        }

        project.afterEvaluate {
            println(PluginSettings.get())
        }

        project.getExtensions().findByType(AppExtension)
                .registerTransform(new MainTransform());
    }
}
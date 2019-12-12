package com.mrcd.language

import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.Project

import java.util.regex.Pattern

/**
 * 代码注入器基类
 */
abstract class BaseCodeInjector implements CodeInjector {

    public static final ClassPool POOL = ClassPool.getDefault()

    BaseCodeInjector(Project project) {
        //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        POOL.appendClassPath(project.android.bootClasspath[0].toString())
    }

    def addClassPath = { String path ->
        POOL.appendClassPath(path)
    }

    /**
     * 根据类文件路径，加载CtClass对象
     * @param classPath 类文件路径
     * @return CtClass对象
     */
    protected CtClass getTarget(String classPath) {
        String className = classPath.replaceAll(".class", "").replaceAll(File.separator, ".")
        try {
            CtClass ctClass = POOL.get(className)
            if (null != ctClass) {
                return ctClass
            } else {
                return null
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 过滤文件
     * 除去R文件，以及support库下的文件
     * @param file 文件对象
     * @return 是否符合要求
     */
    protected boolean filterFile(File file) {
        String path = file.absolutePath
        Pattern androidSupport = Pattern.compile("android/support.*")
        if (androidSupport.matcher(path).matches()) {
            return false
        }
        Pattern androidX = Pattern.compile("androidx.*")
        if (androidX.matcher(path).matches()) {
            return false
        }
        Pattern androidR = Pattern.compile("^.*R\\.class.*\$")
        if (androidR.matcher(path).matches()) {
            return false
        }
        Pattern androidR$ = Pattern.compile("^.*R\\\$.*\\.class.*\$")
        if (androidR$.matcher(path).matches()) {
            return false
        }
        return true
    }

    protected void print(CtClass ctClass) {
        String logMsg = "\n====================================\n" +

                "\n===================================="
    }
}

package com.mrcd.language


import org.apache.commons.io.IOUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * 针对jar包的代码注入方式
 */
class SimpleCodeInjector extends BaseCodeInjector {

    SimpleCodeInjector(Project project) {
        super(project)
    }

    @Override
    void doInject(File src) {
        def optJar = new File(src.getParent(), src.name + ".opt")
        if (optJar.exists())
            optJar.delete()

        def jar = new JarFile(src)
        Enumeration enumeration = jar.entries()
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.name
            ZipEntry zipEntry = new ZipEntry(entryName)
            InputStream inputStream = jar.getInputStream(jarEntry)
            jarOutputStream.putNextEntry(zipEntry)

            jarOutputStream.write(IOUtils.toByteArray(inputStream))
            inputStream.close()
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        sun.tools.jar.resources.jar.close()
        if (src.exists()) {
            src.delete()
        }
        optJar.renameTo(src)
    }

}

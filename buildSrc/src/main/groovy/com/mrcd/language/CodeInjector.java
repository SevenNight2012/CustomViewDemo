package com.mrcd.language;

import java.io.File;

/**
 * 代码注入
 * <p>
 */
public interface CodeInjector {

    /**
     * 注入代码
     *
     * @param target class或者jar文件
     */
    void doInject(File target);

}

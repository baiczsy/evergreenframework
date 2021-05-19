package org.evergreen.web.utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

/**
 * @author wangl
 * @date 2021/5/19
 */
public class ScanUtils {

    public static ClassInfoList scan(String packageName) {
        ScanResult result = new ClassGraph().acceptPackages(packageName).scan();
        return result.getAllClasses();
    }
}

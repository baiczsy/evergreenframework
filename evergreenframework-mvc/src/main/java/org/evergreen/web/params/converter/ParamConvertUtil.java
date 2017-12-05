package org.evergreen.web.params.converter;

import org.evergreen.web.ActionDefinition;
import org.evergreen.web.ActionMapper;
import org.evergreen.web.exception.ParamMappingException;
import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.params.converter.ParamsConvertHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ParamConvertUtil {

    public static Object[] convert(ActionMapper mapper) {
        ActionDefinition definition = mapper.getDefinition();
        Object[] params = new Object[definition.getParamInfo().size()];
        for (int i = 0; i < params.length; i++) {
            ParamInfo paramInfo = definition.getParamInfo().get(i);
            params[i] = doExecute(paramInfo);
            // 如果映射参数不成功,且参数类型是基本类型,则抛出异常信息
            isPrimitive(params[i], paramInfo);
        }
        return params;
    }

    // 执行批量参数映射
    private static Object doExecute(ParamInfo paramInfo) {
        // 使用ServiceLoader来加载SPI实现类
        // 对应的实现类名称必须放在META-INF\services目录下
        ServiceLoader<ParamsConvertHandler> loader = ServiceLoader.load(ParamsConvertHandler.class);
        for (ParamsConvertHandler handler : loader) {
            Object param = handler.execute(paramInfo);
            // 如果映射成功,则返回该参数,如果为null,
            // 则继续循环使用下一个命令执行映射
            if (param != null)
                return param;
        }
        return null;
    }

    // 抛出基本类型的异常信息
    private static void isPrimitive(Object param, ParamInfo paramInfo) {
        if (param == null && paramInfo.getParamType().isPrimitive())
            throw new ParamMappingException(paramInfo
                    .getParamType().toString(), paramInfo.getParamName());

    }
}

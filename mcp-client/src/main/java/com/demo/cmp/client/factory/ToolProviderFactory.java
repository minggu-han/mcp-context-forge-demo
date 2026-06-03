package com.demo.cmp.client.factory;

import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 工具提供类工厂
 */
@Component
public class ToolProviderFactory implements ApplicationContextAware {

    private final List<ToolProvider> toolProviderList = new CopyOnWriteArrayList();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ToolProvider> serviceMap = applicationContext.getBeansOfType(ToolProvider.class);
        for (ToolProvider service : serviceMap.values()) {
            if (service != null ) {
                toolProviderList.add(service);
            }
        }
    }

    /**
     * 获取原始服务
     */
    public List<ToolProvider> getToolProviderList() {
        return toolProviderList;
    }
}

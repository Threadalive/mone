package com.xiaomi.mone.log.manager.service.nacos;

import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.xiaomi.data.push.nacos.NacosNaming;
import com.xiaomi.mone.log.manager.common.MilogConfig;
import com.xiaomi.youpin.docean.anno.Component;
import com.xiaomi.youpin.docean.plugin.config.anno.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author wtt
 * @version 1.0
 * @description
 * @date 2021/10/18 15:09
 */
@Component
@Slf4j
public class MultipleNacosConfig {

    @Value("$nacosAddr")
    private String nacosAdders;

    private static Map<String, ConfigService> nacosServiceMap = new HashMap<>();
    private static Map<String, NacosNaming> nacosNamingMap = new HashMap<>();

    public void init() {
        Arrays.stream(StringUtils.split(nacosAdders, "\\$")).forEach(address -> {
            try {
                nacosServiceMap.put(address, ConfigFactory.createConfigService(address));
                nacosNamingMap.put(address, MilogConfig.buildNacosNaming(address));
            } catch (NacosException e) {
                log.error(String.format("multiple nacos address init error:address:%s", address), e);
            }
        });
        log.info("multiple nacos service address:{}", nacosServiceMap);
        log.info("multiple nacos naming address:{}", nacosNamingMap);
    }

    public static List<String> getAllNachosAdders() {
        return new ArrayList<>(nacosServiceMap.keySet());
    }

    public static ConfigService getConfigService(String nacosAddress) {
        return nacosServiceMap.get(nacosAddress);
    }

    public static NacosNaming getNacosNaming(String nacosAddress) {
        return nacosNamingMap.get(nacosAddress);
    }

}

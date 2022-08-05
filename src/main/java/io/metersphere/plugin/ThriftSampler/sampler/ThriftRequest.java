package io.metersphere.plugin.ThriftSampler.sampler;

import lombok.Data;

/**
 * @author fit2cloudzhao
 * @date 2022/8/3 17:31
 * @description:
 */
@Data
public class ThriftRequest {

    private String serverIp;

    private Integer serverPort;

    private String requestParam;

}

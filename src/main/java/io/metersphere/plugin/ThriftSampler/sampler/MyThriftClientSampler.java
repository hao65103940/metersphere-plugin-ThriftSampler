package io.metersphere.plugin.ThriftSampler.sampler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.thrift.demo.jmeterTest.ThriftSampler;
import io.metersphere.plugin.ThriftSampler.util.ElementUtil;
import io.metersphere.plugin.core.MsParameter;
import io.metersphere.plugin.core.MsTestElement;
import io.metersphere.plugin.core.utils.LogUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;

import java.util.LinkedList;
import java.util.List;

/**
 * @author fit2cloudzhao
 * @date 2022/8/3 17:27
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyThriftClientSampler extends MsTestElement {

    public MyThriftClientSampler() {

    }

    private String clazzName = MyThriftClientSampler.class.getCanonicalName();


    @JSONField(ordinal = 10)
    private String serverIp;
    @JSONField(ordinal = 11)
    private Integer serverPort;
    @JSONField(ordinal = 12)
    private String requestParam;


    @Override
    public void toHashTree(HashTree tree, List<MsTestElement> hashTree, MsParameter config) {

        LogUtil.info("===========开始转换MsThriftSample ==================");
        if (!this.isEnable()) {
            return;
        }
        ThriftSampler initDummySampler = initThriftSampler();
        if (initDummySampler != null) {
            tree.add(initDummySampler);
            final HashTree mqttTree = tree;
            if (CollectionUtils.isNotEmpty(hashTree)) {
                for (MsTestElement el : hashTree) {
                    el.toHashTree(mqttTree, el.getHashTree(), config);
                }
            }
        } else {
            LogUtil.error("Connect Sampler 生成失败");
            throw new RuntimeException("Connect Sampler生成失败");
        }
    }


    public ThriftSampler initThriftSampler() {
        try {
            ThriftSampler thriftSampler = new ThriftSampler();
            // base 执行时需要的参数
            thriftSampler.setProperty("MS-ID", this.getId());
            String indexPath = this.getIndex();
            thriftSampler.setProperty("MS-RESOURCE-ID", this.getResourceId() + "_" + ElementUtil.getFullIndexPath(this.getParent(), indexPath));
            List<String> id_names = new LinkedList<>();
            ElementUtil.getScenarioSet(this, id_names);
            thriftSampler.setProperty("MS-SCENARIO", JSON.toJSONString(id_names));
            // 自定义插件参数转换
            thriftSampler.setEnabled(this.isEnable());
            thriftSampler.setName(StringUtils.isNotEmpty(this.getName()) ? this.getName() : "ThriftSampler");

            // jmx 中 ThriftSampler 对应的 guiclass 和 testclass 属性
            thriftSampler.setProperty(TestElement.GUI_CLASS, "com.thrift.demo.gui.ThriftSamplerUI");
            thriftSampler.setProperty(TestElement.TEST_CLASS, "com.thrift.demo.jmeterTest.ThriftSampler");

            thriftSampler.setProperty("server_ip", this.getServerIp());
            thriftSampler.setProperty("port", this.getServerPort());
            thriftSampler.setProperty("request_param", this.getRequestParam());
            thriftSampler.setProperty("RESULT_CLASS", "org.apache.jmeter.samplers.SampleResult");


            return thriftSampler;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

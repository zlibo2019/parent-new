package com.weds.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;

@Configuration
// @ComponentScan({"com.weds.bean.mqtt"})
@ConditionalOnProperty(name = "weds.mqtt.active", havingValue = "true")
@EnableConfigurationProperties(MqttParams.class)
public class MqttConfig {
    private Logger log = LogManager.getLogger();

    @Resource
    private MqttParams mqttParams;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        String[] serverURIs = mqttParams.getOutbound().getUrls().split(",");
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setServerURIs(serverURIs);
        factory.setCleanSession(false);
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(mqttParams.getOutbound().getClientId(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttParams.getOutbound().getTopic());
        return messageHandler;
    }

    // @Bean
    // public IntegrationFlow mqttOutboundFlow() {
    //     return IntegrationFlows.from(mqttOutboundChannel()).handle(mqttOutbound()).get();
    // }


    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mqttInbound() {
        String[] inboundTopics = mqttParams.getInbound().getTopics().split(",");
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttParams.getInbound().getUrl(), mqttParams.getInbound().getClientId(),
                        inboundTopics);
        // MqttPahoMessageDrivenChannelAdapter adapter =
        //         new MqttPahoMessageDrivenChannelAdapter(mqttParams.getInbound().getClientId(), mqttClientFactory(),
        //                 inboundTopics);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handler() {
        return message -> log.info((String) message.getPayload());
    }

    // @Bean
    // public IntegrationFlow mqttInFlow() {
    //     return IntegrationFlows.from(mqttInbound())
    //             .transform(p -> p + ", received from MQTT")
    //             .handle(logger())
    //             .get();
    // }
    //
    // private LoggingHandler logger() {
    //     LoggingHandler loggingHandler = new LoggingHandler("INFO");
    //     loggingHandler.setLoggerName("siSample");
    //     return loggingHandler;
    // }
}

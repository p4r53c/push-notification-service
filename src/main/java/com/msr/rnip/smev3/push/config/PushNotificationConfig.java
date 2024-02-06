package com.msr.rnip.smev3.push.config;

import com.msr.rnip.smev3.push.interceptor.PushNotificationLoggingInterceptor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;

import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

/**
 * @author p4r53c
 * @created 07.08.2020
 */
@EnableWs
@Configuration
public class PushNotificationConfig extends WsConfigurerAdapter {

    //Загрузка всех интерцепторов
    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        PushNotificationLoggingInterceptor pushNotificationLoggingInterceptor = new PushNotificationLoggingInterceptor();
        interceptors.add(pushNotificationLoggingInterceptor);

        PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
        validatingInterceptor.setValidateRequest(true);
        validatingInterceptor.setValidateResponse(true);
        validatingInterceptor.setXsdSchema(pushNotificationSchema());
        interceptors.add(validatingInterceptor);

    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/smev3/rnip/*");
    }

    //Загрузка готового статического документа WSDL, вместо динамической генерации по XSD в DefaultWsdl11Definition
    @Bean(name = "PushNotificationService")
    public SimpleWsdl11Definition SmevPushService() {
        SimpleWsdl11Definition simpleWsdl11Definition = new SimpleWsdl11Definition();
        simpleWsdl11Definition.setWsdl(new ClassPathResource("xjc/smev3/push/contract/PushNotificationService.wsdl"));
        return simpleWsdl11Definition;
    }

    //XSD как бин. Используется для резолвинга в WSDL и для валидации в PayloadValidatingInterceptor.
    @Bean(name = "PushNotificationSchema")
    public XsdSchema pushNotificationSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xjc/smev3/push/contract/PushNotificationSchema.xsd"));
    }

    //Фикс дефолтного JNDI для корректно подключения к JtaTransactionManager.
    /*
    @Bean
    public JtaTransactionManager transactionManager() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManagerName("java:/TransactionManager");
        return transactionManager;
    }
     */
}

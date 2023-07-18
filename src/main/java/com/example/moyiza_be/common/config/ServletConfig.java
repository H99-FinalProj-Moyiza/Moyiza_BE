//package com.example.moyiza_be.config;
//
//import org.apache.catalina.connector.Connector;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ServletConfig {
//    @Bean
//    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> connectorCustomizer() {
//        return (tomcat) -> tomcat.addAdditionalTomcatConnectors(createStandardConnector());
//    }
//
//    private Connector createStandardConnector () {
//        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//        connector.setPort(8080);
//        return connector;
//    }
//}
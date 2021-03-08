package com.epf.rentmanager.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.epf.rentmanager.dao","com.epf.rentmanager.service"})
public class AppConfiguration {

}

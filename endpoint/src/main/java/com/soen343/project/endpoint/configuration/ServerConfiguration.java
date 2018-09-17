package com.soen343.project.endpoint.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Kevin Tan 2018-09-17
 */

@ComponentScan(basePackages = {"com.soen343.project.service"})
@Configuration
public class ServerConfiguration {}

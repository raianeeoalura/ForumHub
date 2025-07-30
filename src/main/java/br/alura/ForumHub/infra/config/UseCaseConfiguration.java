package br.alura.ForumHub.infra.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "br.alura.ForumHub.application.usecase")
public class UseCaseConfiguration {

}

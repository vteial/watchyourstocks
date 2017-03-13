package io.wybis.watchyourstocks

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        SecurityCheckInterceptor si = new SecurityCheckInterceptor();
//        registry.addInterceptor(si).addPathPatterns("/**");
//
//        super.addInterceptors(registry);
//    }

//    @Bean
//    Template displayTemplate() throws Exception {
//        Template tmpl = null
//
//        ClassPathResource cpr = new ClassPathResource("templates/display.html")
//        SimpleTemplateEngine ste = this.simpleTemplateEngine()
//        tmpl = ste.createTemplate(cpr.getFile())
//
//        return tmpl
//    }

//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
//                .authorizeRequests().antMatchers("/console/**").permitAll();
//
//        httpSecurity.csrf().disable();
//        httpSecurity.headers().frameOptions().disable();
//    }

//    @Bean
//    ServletRegistrationBean h2servletRegistration(){
//        ServletRegistrationBean registrationBean = null
//
//        registrationBean = new ServletRegistrationBean( new WebServlet());
//        registrationBean.addUrlMappings("/h2-console/*");
//
//        return registrationBean;
//    }
}

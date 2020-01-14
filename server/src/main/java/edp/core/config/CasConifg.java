package edp.core.config;

import com.xhqb.sso.cas.SingleSignOutFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xhqb.sso.cas.AuthenticationFilter;

/**
 * cas配置
 * @author zhangheliang
 */
@Configuration
public class CasConifg {
    //@Value("${cas.casServer.loginUrl}")
    public String casServerLoginUrl="http://192.168.1.184:8081/cas/login";
    //@Value("${cas.casServer.urlPrefix}")
    public String casServerUrlPrefix="http://192.168.1.184:8081/cas";
    //@Value("${cas.localServer.serverName}")
    public String serverName="http://127.0.0.1:8990";
    /**
     * SingleSignOutFilter filter
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new SingleSignOutFilter());
        filterBean.addUrlPatterns("/*");
        return filterBean;
    }

    /**
     * AuthenticationFilter filter
     */
    @Bean
    public FilterRegistrationBean authenticationFilterFilter() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new AuthenticationFilter());
        filterBean.addInitParameter("casServerLoginUrl", this.casServerLoginUrl);
        filterBean.addInitParameter("serverName", this.serverName);
        filterBean.addUrlPatterns("/*");
        return filterBean;
    }

    /**
     * Cas20ProxyReceivingTicketValidationFilter filter
     */
    @Bean
    public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        filterBean.addInitParameter("casServerUrlPrefix", this.casServerUrlPrefix);
        filterBean.addInitParameter("serverName", this.serverName);
        filterBean.addUrlPatterns("/*");
        return filterBean;
    }

    /**
     * HttpServletRequestWrapperFilter filter
     */
    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new HttpServletRequestWrapperFilter());
        filterBean.addUrlPatterns("/*");
        return filterBean;
    }

    /**
     * AssertionThreadLocalFilter filter
     */
    @Bean
    public FilterRegistrationBean assertionThreadLocalFilter() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new AssertionThreadLocalFilter());
        filterBean.addUrlPatterns("/*");
        return filterBean;
    }




}

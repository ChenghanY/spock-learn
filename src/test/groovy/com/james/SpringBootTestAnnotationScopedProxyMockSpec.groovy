package com.james

import com.james.service.HelloWorldService
import org.spockframework.spring.ScanScopedBeans
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.annotation.RequestScope
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

/**
 * Tests enabled scanning of scoped and proxied beans.
 */
@ScanScopedBeans
@SpringBootTest(properties = ['spring.main.allow-bean-definition-overriding=true'])
class SpringBootTestAnnotationScopedProxyMockSpec extends Specification {
  @Autowired
  ApplicationContext context

  @Autowired
  HelloWorldService helloWorldService

  /**
   * spring boot 的 mock
   */
  @TestConfiguration
  static class MockConfig {
    def detachedMockFactory = new DetachedMockFactory()

    // spring boot 接管了application中的bean并包装成scope的class
    @Bean @RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    HelloWorldService helloWorldService() {
      return detachedMockFactory.Mock(HelloWorldService)
    }
  }

  def "验证是否产生新的bean"() {
    expect:
    context != null
    context.containsBean("helloWorldService")
    context.containsBean("scopedTarget.helloWorldService")
    context.containsBean("simpleBootApp")
  }

  def "验证代理类的mock能覆盖被代理的类"() {
    given: "获取HelloWorldService的代理类"
    def helloWorldServiceMock = context.getBean("scopedTarget.helloWorldService") as HelloWorldService

    expect: "代理类是spring的动态代理"
    helloWorldService.class.simpleName.startsWith('HelloWorldService$$EnhancerBySpringCGLIB$$')

    when: "调用被代理类的那一刻"
    def result = helloWorldService.helloMessage

    then: "验证将代理类的值给mock掉是否成功"
    1 * helloWorldServiceMock.getHelloMessage() >> 'sup?'
    result == 'sup?'
  }

}

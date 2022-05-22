/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.james

import com.james.service.HelloWorldService
import org.spockframework.spring.ScanScopedBeans
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import spock.lang.Specification
import spock.mock.DetachedMockFactory

/**
 * Tests enabled scanning of scoped beans.
 */
@ScanScopedBeans
@SpringBootTest(properties = ['spring.main.allow-bean-definition-overriding=true'])
class SpringBootTestAnnotationScopedMockSpec extends Specification {
  @Autowired
  ApplicationContext context

  @Autowired
  HelloWorldService helloWorldService

  def "验证spring.main.allow-bean-definition-overriding=true成功"() {
    expect:
    context != null
    context.containsBean("helloWorldService")
    !context.containsBean("scopedTarget.helloWorldService") // bean同名重写成功，忽略了spring的注解
    context.containsBean("simpleBootApp")
  }

  def "验证当前注入的bean不是spring的代理类，且mock返回值成功"() {
    expect:
    // 验证bean不是spring动态代理的产物
    !helloWorldService.class.simpleName.startsWith('HelloWorldService$$EnhancerBySpringCGLIB$$')

    when:
    def result = helloWorldService.helloMessage

    then:
    // 1 * 表示调用一次;  >>  表示 mock了 getHelloMessage()的返回值
    1 * helloWorldService.getHelloMessage() >> 'sup?'
    result == 'sup?'
  }


  @TestConfiguration
  static class MockConfig {
    def detachedMockFactory = new DetachedMockFactory()

    @Bean
    HelloWorldService helloWorldService() {
      return detachedMockFactory.Mock(HelloWorldService)
    }
  }
}

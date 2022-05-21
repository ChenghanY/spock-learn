package com.james

import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext


@SpringBootTest(classes = SimpleBootApp)
class SimpleBootAppIntegrationSpec extends Specification {

  @Autowired
  ApplicationContext context

  def "test context loads"() {
    expect:
    context != null
    context.containsBean("helloWorldService")
    context.containsBean("helloWorldController")
    context.containsBean("simpleBootApp")
  }
}

package com.james

import com.james.service.HelloWorldService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Integration tests for ensuring compatibility with Spring-Boot's {@link WebMvcTest} annotation
 * in conjunction with {@link SpringBean}.
 */
//tag::include[]
@WebMvcTest
class SpringBeanIntegrationSpec extends Specification {

  @Autowired
  MockMvc mvc

  /**
   * Stub() 比 Mock() 轻量。 Mock()不仅可以模拟方法返回结果，还可以模拟方法行为: https://javakk.com/264.html
   */
  @SpringBean
  HelloWorldService helloWorldService = Stub()

  def "测试mvc场景下，返回值的mock"() {
    given:
    helloWorldService.getHelloMessage() >> 'hello world'

    expect: "controller is available"
    mvc.perform(MockMvcRequestBuilders.get("/"))
      .andExpect(status().isOk())
      .andExpect(content().string("hello world"))
  }
}
//end::include[]

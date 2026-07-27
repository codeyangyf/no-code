package com.lc.bootstrap.interceptor;

import com.lc.common.annotation.TenantCheck;
import com.lc.common.context.UserContext;
import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TenantInterceptorTest {

    private TenantInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TenantInterceptor();
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    @DisplayName("非HandlerMethod类型直接放行")
    void shouldPassWhenNotHandlerMethod() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();
        assertTrue(interceptor.preHandle(request, response, handler));
    }

    @Test
    @DisplayName("方法无@TenantCheck注解时放行")
    void shouldPassWhenNoTenantCheckAnnotation() throws NoSuchMethodException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "noAnnotation");
        assertTrue(interceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    @DisplayName("当前用户tenantId为null时放行（超级管理员）")
    void shouldPassWhenTenantIdIsNull() throws NoSuchMethodException {
        UserContext.set(UserContext.builder().userId(1L).tenantId(null).username("admin").build());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("tenantId")).thenReturn("999");
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
        assertTrue(interceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    @DisplayName("请求参数中无tenantId时放行")
    void shouldPassWhenNoTenantIdParam() throws NoSuchMethodException {
        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("tenantId")).thenReturn(null);
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
        assertTrue(interceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    @DisplayName("tenantId一致时放行")
    void shouldPassWhenTenantIdMatches() throws NoSuchMethodException {
        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("tenantId")).thenReturn("1");
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
        assertTrue(interceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    @DisplayName("tenantId不一致时抛出PERMISSION_DENIED")
    void shouldThrowWhenTenantIdMismatch() throws NoSuchMethodException {
        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("tenantId")).thenReturn("999");
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
        BusinessException ex = assertThrows(BusinessException.class, () ->
            interceptor.preHandle(request, response, handlerMethod));
        assertEquals(GlobalErrorCode.PERMISSION_DENIED.getCode(), ex.getCode());
    }

    @Test
    @DisplayName("query parameter缺失时从path variable提取且一致时放行")
    void shouldPassWhenPathVariableMatches() throws NoSuchMethodException {
        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("tenantId")).thenReturn(null);
        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
            .thenReturn(Map.of("tenantId", "1"));
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
        assertTrue(interceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    @DisplayName("query parameter缺失时从path variable提取且不一致时抛PERMISSION_DENIED")
    void shouldThrowWhenPathVariableMismatch() throws NoSuchMethodException {
        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("tenantId")).thenReturn(null);
        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
            .thenReturn(Map.of("tenantId", "999"));
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
        BusinessException ex = assertThrows(BusinessException.class, () ->
            interceptor.preHandle(request, response, handlerMethod));
        assertEquals(GlobalErrorCode.PERMISSION_DENIED.getCode(), ex.getCode());
    }

    @Test
    @DisplayName("query parameter与path variable均缺失时放行")
    void shouldPassWhenBothQueryAndPathVariableAbsent() throws NoSuchMethodException {
        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("tenantId")).thenReturn(null);
        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
            .thenReturn(null);
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
        assertTrue(interceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    @DisplayName("query parameter优先级高于path variable")
    void shouldPreferQueryParameterOverPathVariable() throws NoSuchMethodException {
        UserContext.set(UserContext.builder().userId(1L).tenantId(1L).username("user").build());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("tenantId")).thenReturn("1");
        when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
            .thenReturn(Map.of("tenantId", "999"));
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), "withTenantCheck");
        assertTrue(interceptor.preHandle(request, response, handlerMethod));
    }

    // 测试用Controller
    static class TestController {
        public void noAnnotation() {}

        @TenantCheck(tenantIdParam = "tenantId")
        public void withTenantCheck() {}
    }
}

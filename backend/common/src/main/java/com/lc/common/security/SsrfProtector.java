package com.lc.common.security;

import com.lc.common.exception.BusinessException;
import com.lc.common.exception.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 * SSRF（服务端请求伪造）防护工具。
 * <p>
 * 在服务端发起外网请求前，通过 {@link #validateUrl(String)} 或 {@link #isSafeUrl(String)}
 * 校验目标 URL：禁止协议非法、指向内网/回环/链路本地/保留地址的目标，并对 host 做 DNS
 * 解析后再次检查解析结果，避免使用域名绕过 IP 段校验。
 */
@Slf4j
@Component
public class SsrfProtector {

    /**
     * 校验 URL 是否安全（非内网、协议合法）。
     *
     * @param url 待校验的 URL
     * @throws BusinessException 当 URL 指向内网或协议非法时
     */
    public void validateUrl(String url) {
        String host = doCheck(url);
        if (host != null) {
            throw new BusinessException(GlobalErrorCode.PERMISSION_DENIED.getCode(),
                    "URL 不允许访问: " + host);
        }
    }

    /**
     * 判断 URL 是否安全（不抛异常，返回 boolean）。
     *
     * @param url 待校验的 URL
     * @return true 表示安全，false 表示不安全或校验异常
     */
    public boolean isSafeUrl(String url) {
        try {
            return doCheck(url) == null;
        } catch (Exception e) {
            log.debug("SSRF isSafeUrl check failed for url={}", url, e);
            return false;
        }
    }

    /**
     * 执行校验逻辑。
     *
     * @return null 表示安全；非 null 表示不安全，返回值为拒绝原因中使用的 host
     */
    private String doCheck(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }

        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            log.debug("SSRF check: invalid url syntax {}", url);
            return "";
        }

        String scheme = uri.getScheme();
        if (scheme == null
                || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
            log.debug("SSRF check: illegal scheme {}", scheme);
            return "";
        }

        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            return "";
        }
        String lowerHost = host.toLowerCase();

        // 1. 基于字符串的内网/保留地址判定
        if (isReservedHost(lowerHost)) {
            return host;
        }

        // 2. DNS 解析后再判定，防止用域名绕过
        InetAddress[] addresses;
        try {
            addresses = InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            // DNS 解析失败：拒绝，避免解析失败绕过
            log.debug("SSRF check: DNS resolution failed for {}", host);
            return host;
        }
        for (InetAddress addr : addresses) {
            if (addr.isSiteLocalAddress() || addr.isLoopbackAddress()
                    || addr.isLinkLocalAddress() || addr.isAnyLocalAddress()) {
                return host;
            }
        }

        return null;
    }

    /**
     * 基于字符串判定 host 是否属于内网/回环/链路本地/保留地址。
     */
    private boolean isReservedHost(String host) {
        if ("localhost".equals(host) || host.endsWith(".localhost")) {
            return true;
        }
        if ("127.0.0.1".equals(host) || host.startsWith("127.")) {
            return true;
        }
        if (host.startsWith("10.")) {
            return true;
        }
        if (host.startsWith("192.168.")) {
            return true;
        }
        if (host.startsWith("169.254.")) {
            return true;
        }
        if ("0.0.0.0".equals(host)) {
            return true;
        }
        // IPv6 回环
        if ("::1".equals(host) || "[::1]".equals(host)) {
            return true;
        }
        // IPv6 ULA（fc00::/7）
        if (host.startsWith("fc") || host.startsWith("fd")) {
            return true;
        }
        // 172.16.0.0 ~ 172.31.255.255
        if (host.startsWith("172.")) {
            String[] parts = host.split("\\.");
            if (parts.length >= 2) {
                try {
                    int second = Integer.parseInt(parts[1]);
                    if (second >= 16 && second <= 31) {
                        return true;
                    }
                } catch (NumberFormatException ignore) {
                    // 非 IPv4 数字段，交给 DNS 解析判定
                }
            }
        }
        return false;
    }
}

package com.lc.bootstrap.config;

import com.lc.common.util.PasswordEncoderUtil;
import com.lc.system.entity.SysTenant;
import com.lc.system.entity.SysUser;
import com.lc.system.repository.SysTenantRepository;
import com.lc.system.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SysTenantRepository tenantRepository;
    private final SysUserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    @Override
    public void run(String... args) {
        if (tenantRepository.count() == 0) {
            log.info("初始化默认租户...");
            SysTenant tenant = new SysTenant();
            tenant.setTenantCode("default");
            tenant.setTenantName("默认租户");
            tenant.setStatus(1);
            tenant.setCreatedTime(LocalDateTime.now());
            tenant = tenantRepository.save(tenant);
            log.info("默认租户创建成功，ID: {}", tenant.getId());

            log.info("初始化管理员用户...");
            SysUser admin = new SysUser();
            admin.setTenantId(tenant.getId());
            admin.setUsername("admin");
            admin.setPassword(passwordEncoderUtil.encode("admin123"));
            admin.setRealName("管理员");
            admin.setEmail("admin@example.com");
            admin.setPhone("13800138000");
            admin.setStatus(1);
            admin.setCreatedTime(LocalDateTime.now());
            userRepository.save(admin);
            log.info("管理员用户创建成功，用户名: admin, 密码: admin123");
        }
    }
}

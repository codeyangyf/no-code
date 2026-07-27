package com.lc.common.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    private Long userId;
    private Long tenantId;
    private String username;

    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();

    public static void set(UserContext context) {
        HOLDER.set(context);
    }

    public static UserContext get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static Long getUserId() {
        UserContext ctx = HOLDER.get();
        return ctx != null ? ctx.userId : null;
    }

    public static Long getTenantId() {
        UserContext ctx = HOLDER.get();
        return ctx != null ? ctx.tenantId : null;
    }

    public static String getUsername() {
        UserContext ctx = HOLDER.get();
        return ctx != null ? ctx.username : null;
    }
}

package com.vueblog.shiro;

import org.apache.shiro.SecurityUtils;

public class ShiroUtil {
    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}

package org.gg.service;

import org.gg.utils.HashUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Override
    public String login() {
        //TODO:
//        HashUtil.verifyPassword(user.getPassword, user.getSalt(), HashUtil.)
//        return null;
        return null;
    }
    @Override
    public String register() {
        return null;
    }
}

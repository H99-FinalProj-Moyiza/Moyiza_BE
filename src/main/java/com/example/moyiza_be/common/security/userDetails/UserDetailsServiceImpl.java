package com.example.moyiza_be.common.security.userDetails;

import com.example.moyiza_be.domain.user.entity.User;
import com.example.moyiza_be.domain.user.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ValidationUtil validationUtil;
    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = validationUtil.findUserByEmail(email);
        return new UserDetailsImpl(user);
    }
}
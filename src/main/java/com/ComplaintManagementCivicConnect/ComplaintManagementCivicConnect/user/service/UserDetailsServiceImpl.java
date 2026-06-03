package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.service;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.entity.UserPrinciple;
import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserPrinciple::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found:" + email));
    }
}

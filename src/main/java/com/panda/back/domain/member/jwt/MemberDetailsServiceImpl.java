package com.panda.back.domain.member.jwt;

import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String membername) throws UsernameNotFoundException {
        Member member = memberRepository.findByMembername(membername)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + membername));

        return new MemberDetailsImpl(member);
    }
}

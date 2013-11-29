package com.alibaba.lp.dao.member;

import java.util.List;

import com.alibaba.lp.domain.member.Member;

public interface MemberDao
{
    public Member findById(Long id);

    public Member findByEmail(String email);

    public List<Member> findAllOrderedByName();

    public void register(Member member);
}

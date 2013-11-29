package com.alibaba.lp.mvc.member;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.lp.dao.member.MemberDao;
import com.alibaba.lp.domain.member.Member;

@Controller
@RequestMapping("/rest/members")
public class MemberRestController {
    @Resource
    private MemberDao memberDao;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Member> listAllMembers() {
        return memberDao.findAllOrderedByName();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Member lookupMemberById(@PathVariable("id") Long id) {
        return memberDao.findById(id);
    }
}

package com.cxfly.lp.mvc.member;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cxfly.lp.dao.member.MemberDao;
import com.cxfly.lp.domain.member.Member;

@Controller
@RequestMapping(value = "/")
public class MemberController {
    @Resource
    private MemberDao memberDao;

    @RequestMapping(method = RequestMethod.GET)
    public String displaySortedMembers(Model model) {
        model.addAttribute("newMember", new Member());
        model.addAttribute("members", memberDao.findAllOrderedByName());
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerNewMember(@Valid @ModelAttribute("newMember") Member newMember,
                                    BindingResult result, Model model) {
        if (!result.hasErrors()) {
            //  newMember.setId(System.currentTimeMillis());
            memberDao.register(newMember);
            return "redirect:/";
        } else {
            model.addAttribute("members", memberDao.findAllOrderedByName());
            return "index";
        }
    }
}

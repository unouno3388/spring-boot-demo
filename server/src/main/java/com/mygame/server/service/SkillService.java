package com.mygame.server.service;

import com.mygame.server.skill.FireballSkill;
import com.mygame.server.skill.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SkillService {
    private final Map<String, Skill> skills = new HashMap<>();

    @Autowired
    public SkillService(FireballSkill fireballSkill) {
        // 在 Service 初始化時註冊可用的技能
        skills.put("fireball", fireballSkill);
        // 可以添加更多技能，並在這裡注入
    }

    public Skill getSkill(String skillName) {
        return skills.get(skillName.toLowerCase()); // 忽略大小寫
    }
}
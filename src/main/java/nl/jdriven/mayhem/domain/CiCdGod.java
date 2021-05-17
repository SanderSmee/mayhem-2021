package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;

import java.util.List;

class CiCdGod extends Hero {

    public CiCdGod() {
        super("CI/CD god", 300, 200, "", 10, 0, 20, List.of(
            new Skill(0, "yamlize", "", -20, 2000, 0, 0, -50, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(1, "containerize", "", -25, 5000, 0, 0, -75, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(2, "svn2git", "", 10, 3500, 7000, 0, -20, Skill.EffectType.power, Skill.AllowedTarget.all),
            new Skill(3, "infraascode", "", -25, 1500, 10_000, 7500, 35, Skill.EffectType.resistance, Skill.AllowedTarget.all),
            new Skill(4, "multicloud", "", -15, 1500, 0, 15_000, -15, Skill.EffectType.resistance, Skill.AllowedTarget.all),
            new Skill(5, "greenflied", "", 0, 100, 120_000, 0, 200, Skill.EffectType.power, Skill.AllowedTarget.self)
        ));
    }
}

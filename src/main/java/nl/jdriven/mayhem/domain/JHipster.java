package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;

import java.util.List;

class JHipster extends Hero {

    public JHipster() {
        super("JHipster", 200, 300, "", 15, 0, 25, List.of(
            new Skill(0, "coffee", "", -20, 0, 0, 0, 25, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(1, "yogaclass", "", -40, 0, 0, 0, 60, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(2, "glasses", "", -40, 500, 20_000, 2000, 100, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(3, "meditation", "", 0, 100, 60_000, 0, 300, Skill.EffectType.health, Skill.AllowedTarget.self),
            new Skill(4, "svelte", "", -5, 500, 3000, 0, 10, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(5, "wet", "", 5, 3000, 0, 0, -5, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(5, "kotlin", "", -15, 1500, 0, 30_000, 100, Skill.EffectType.health, Skill.AllowedTarget.all)
        ));
    }
}

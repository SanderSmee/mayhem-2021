package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;

import java.util.List;

import static ninja.robbert.mayhem.api.Hero.Skill.AllowedTarget.all;
import static ninja.robbert.mayhem.api.Hero.Skill.AllowedTarget.self;
import static ninja.robbert.mayhem.api.Hero.Skill.EffectType.health;

class JHipster extends Hero {

    public JHipster() {
        super("JHipster", 200, 300, "", 15, 0, 25, List.of(
            new Skill(0, "coffee", "", -20, 0, 0, 0, 25, health, all),
            new Skill(1, "yogaclass", "", -40, 0, 0, 0, 60, health, all),
            new Skill(2, "glasses", "", -40, 500, 20_000, 2000, 100, health, all),
            new Skill(3, "meditation", "", 0, 100, 60_000, 0, 300, health, self),
            new Skill(4, "svelte", "", -5, 500, 3000, 0, 10, health, all),
            new Skill(5, "wet", "", 5, 3000, 0, 0, -5, health, all),
            new Skill(5, "kotlin", "", -15, 1500, 0, 30_000, 100, health, all)
        ));
    }
}

package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;

import java.util.List;

import static ninja.robbert.mayhem.api.Hero.Skill.AllowedTarget.all;
import static ninja.robbert.mayhem.api.Hero.Skill.AllowedTarget.self;
import static ninja.robbert.mayhem.api.Hero.Skill.EffectType.*;

class CiCdGod extends Hero {

    public CiCdGod() {
        super("CI/CD god", 300, 200, "", 10, 0, 20, List.of(
            new Skill(0, "yamlize", "", -20, 2000, 0, 0, -50, health, all),
            new Skill(1, "containerize", "", -25, 5000, 0, 0, -75, health, all),
            new Skill(2, "svn2git", "", 10, 3500, 7000, 0, -20, power, all),
            new Skill(3, "infraascode", "", -25, 1500, 10_000, 7500, 35, resistance, all), //buff
            new Skill(4, "multicloud", "", -15, 1500, 0, 15_000, -15, resistance, all), //buff
            new Skill(5, "greenfield", "", 0, 100, 120_000, 0, 200, power, self)
        ));
    }
}

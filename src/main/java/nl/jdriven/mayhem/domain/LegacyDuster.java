package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;

import java.util.List;

import static ninja.robbert.mayhem.api.Hero.Skill.AllowedTarget.*;
import static ninja.robbert.mayhem.api.Hero.Skill.EffectType.armor;
import static ninja.robbert.mayhem.api.Hero.Skill.EffectType.health;

class LegacyDuster extends Hero {

    public LegacyDuster() {
        super("Legacy Duster", 500, 50, "", 0, 20, 0, List.of(
            new Skill(0, "dust mainframe", "", -50, 1500, 0, 15_000, 30, armor, others),
            new Skill(1, "COBOL compiled", "", -30, 1500, 4000, 5000, 100, armor, others),
            new Skill(2, "PL/SQL Hell", "", -10, 3000, 0, 0, -30, health, all),
            new Skill(3, "code handover", "", 10, 1500, 0, 0, -15, health, all),
            new Skill(4, "reboot", "", 0, 100, 90_000, 15_000, 60, armor, self),
            new Skill(5, "ejbejbejb", "", -20, 3000, 0, 30_000, -15, armor, all)
        ));
    }
}

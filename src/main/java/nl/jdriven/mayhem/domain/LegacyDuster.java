package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;

import java.util.List;

class LegacyDuster extends Hero {

    public LegacyDuster() {
        super("Legacy Duster", 500, 50, "", 0, 20, 0, List.of(
            new Skill(0, "dust mainframe", "", -50, 1500, 0, 15_000, 30, Skill.EffectType.armor, Skill.AllowedTarget.others),
            new Skill(1, "COBOL compiled", "", -30, 1500, 4000, 5000, 100, Skill.EffectType.armor, Skill.AllowedTarget.others),
            new Skill(2, "PL/SQL Hell", "", -10, 3000, 0, 0, -30, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(3, "code handover", "", 10, 1500, 0, 0, -15, Skill.EffectType.health, Skill.AllowedTarget.all),
            new Skill(4, "reboot", "", 0, 100, 90_000, 15_000, 60, Skill.EffectType.armor, Skill.AllowedTarget.self),
            new Skill(5, "ejbejbejb", "", -20, 3000, 0, 30_000, -15, Skill.EffectType.armor, Skill.AllowedTarget.all)
        ));
    }
}

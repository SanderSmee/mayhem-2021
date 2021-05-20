package nl.jdriven.mayhem.behavior;

import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;
import nl.jdriven.mayhem.subsumption.Behavior;
import nl.jdriven.mayhem.util.Randoms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PrepareAtReadyBehavior implements Behavior {
    private final Logger logger = LoggerFactory.getLogger(PrepareAtReadyBehavior.class);

    private final Arena arena;
    private boolean suppressed;

    public PrepareAtReadyBehavior(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean takeControl() {
        return
            StatusMessage.FightStatus.ready == arena.currentStatus().getStatus()
            && arena.nextActions.isEmpty()
            ;
    }

    @Override
    public void action() {
        var heroes = arena.currentStatus().getYou();
        var enemies = arena.currentStatus().getOpponent();

        var multicloud = Skills.get("multicloud", Heroes.getCiCdGod(heroes).getSkills()); // -resistance
        var infraascode = Skills.get("infraascode", Heroes.getCiCdGod(heroes).getSkills()); // + resistance

        var kotlin = Skills.get("kotlin", Heroes.getJHipster(heroes).getSkills()); // +health
        var glasses = Skills.get("glasses", Heroes.getJHipster(heroes).getSkills()); // +armor

        var ejbejbejb = Skills.get("ejbejbejb", Heroes.getLegacyDuster(heroes).getSkills()); // -armor
        var reboot = Skills.get("reboot", Heroes.getLegacyDuster(heroes).getSkills()); // +armor

        var actions = Arrays.asList(
            new ActionMessage(Heroes.getCiCdGod(heroes).getId(), multicloud.getId(), Heroes.getJHipster(enemies).getId(), false),
//            new ActionMessage(Heroes.getCiCdGod(heroes).getId(), infraascode.getId(), Heroes.getCiCdGod(heroes).getId(), false),

            new ActionMessage(Heroes.getJHipster(heroes).getId(), kotlin.getId(), Heroes.getJHipster(heroes).getId(), false),
//            new ActionMessage(Heroes.getJHipster(heroes).getId(), glasses.getId(), Heroes.getCiCdGod(heroes).getId(), false),

            new ActionMessage(Heroes.getLegacyDuster(heroes).getId(), ejbejbejb.getId(), Heroes.getLegacyDuster(enemies).getId(), false)
//            new ActionMessage(Heroes.getLegacyDuster(heroes).getId(), reboot.getId(), Heroes.getCiCdGod(heroes).getId(), false)
        );

        arena.nextActions.addAll(actions);
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}

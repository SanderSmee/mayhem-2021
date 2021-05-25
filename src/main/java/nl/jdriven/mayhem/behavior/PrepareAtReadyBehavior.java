package nl.jdriven.mayhem.behavior;

import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Action;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;
import nl.jdriven.mayhem.subsumption.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

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
            new Action(Heroes.getCiCdGod(heroes), multicloud, Heroes.getJHipster(enemies), false),
            new Action(Heroes.getJHipster(heroes), kotlin, Heroes.getJHipster(heroes), false),
            new Action(Heroes.getLegacyDuster(heroes), ejbejbejb, Heroes.getJHipster(enemies), false)
        );

        arena.nextActions.addAll(actions);
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}

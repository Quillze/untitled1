package spotlight.spotlight.utils.items;

import com.ronanplugins.utils.items.ItemCreator.ITEM_TYPE;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum Spotlight {
    POWER(1, "Power Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 1, false, false)}),
    RESISTANCE(2, "Resistant Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 1, false, false)}),
    SPEED(3, "Speed Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.SPEED, 1200, 1, false, false)}),
    INVISIBLE(4, "Invisible Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 1, false, false)}, new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    DOLPHIN(5, "Dolphin Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1200, 3, false, false), new PotionEffect(PotionEffectType.CONDUIT_POWER, 1200, 1, false, false)}),
    DRILL(7, "Drill Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 1, false, false)}),
    PUSH(12, "Push Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.WEAKNESS, 6000, 1)}, new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    FIRE(13, "Fire Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 1)}, new Trigger_Type[]{Trigger_Type.LEFT_CLICK, Trigger_Type.HELD_IN_HAND}),
    SATURATION(22, "Saturation Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.SATURATION, 1200, 1, false, false)}),
    FLIGHT(6, "Flight Spotlight"),
    ALTER(11, "Alter Spotlight", new Trigger_Type[]{Trigger_Type.HELD_IN_HAND}),
    SUMMON(27, "Summon Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    WEATHER(17, "Weather Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    TIME(21, "Time Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    TELEPORT(25, "Teleport Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    ORE(26, "Ore Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    MOB_FRIENDLY(24, "Mob Friendly Spotlight", new Trigger_Type[]{Trigger_Type.HELD_IN_HAND}),
    FREEZE(14, "Freeze Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK, Trigger_Type.HELD_IN_HAND}),
    PHASE(28, "Phase Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    LIGHTNING(19, "Lightning Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 2, false, false)}, new Trigger_Type[]{Trigger_Type.HELD_IN_HAND, Trigger_Type.RIGHT_CLICK}),
    AWAY(18, "Away Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    PULL(32, "Pull Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 2, false, false)}, new Trigger_Type[]{Trigger_Type.RIGHT_CLICK, Trigger_Type.HELD_IN_HAND}),
    LAVA(30, "Lava Spotlight", new PotionEffect[]{new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 2, false, false)}, new Trigger_Type[]{Trigger_Type.HELD_IN_HAND}),
    AIR(20, "Air Spotlight", new Trigger_Type[]{Trigger_Type.HELD_IN_HAND, Trigger_Type.LEFT_CLICK}),
    AVOIDER(31, "Avoider Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK}),
    RIPTIDE(23, "Riptide Spotlight", new Trigger_Type[]{Trigger_Type.RIGHT_CLICK});

    private final String label;
    private final int modelID;
    private final PotionEffect[] potionEffect;
    private final Trigger_Type[] triggerType;

    private Spotlight(int modelID, String label, PotionEffect[] effect, Trigger_Type[] triggerType) {
        this.label = label;
        this.modelID = modelID;
        this.potionEffect = effect;
        this.triggerType = triggerType;
    }

    private Spotlight(int modelID, String label, PotionEffect[] effect) {
        this(modelID, label, effect, new Trigger_Type[]{Trigger_Type.HELD_IN_HAND});
    }

    private Spotlight(int modelID, String label) {
        this(modelID, label, (PotionEffect[])null, new Trigger_Type[]{Trigger_Type.HELD_IN_HAND});
    }

    private Spotlight(int modelID, String label, Trigger_Type[] triggerType) {
        this(modelID, label, (PotionEffect[])null, triggerType);
    }

    public static List<Spotlight> getRerollable() {
        List<Spotlight> powers = new ArrayList();
        Spotlight[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Spotlight p = var1[var3];
            if (p != ALTER) {
                powers.add(p);
            }
        }

        return powers;
    }

    public ItemStack item() {
        return (new ItemCreator(Material.LIGHT)).setModelID(this.getModelID()).setDisplayName(this.getLabel()).create(ITEM_TYPE.SPOTLIGHT);
    }

    public String getLabel() {
        return this.label;
    }

    public int getModelID() {
        return this.modelID;
    }

    public PotionEffect[] getPotionEffect() {
        return this.potionEffect;
    }

    public Trigger_Type[] getTriggerType() {
        return this.triggerType;
    }
}
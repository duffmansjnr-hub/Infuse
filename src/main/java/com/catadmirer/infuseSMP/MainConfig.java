package com.catadmirer.infuseSMP;

import com.catadmirer.infuseSMP.effects.InfuseEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainConfig {
    public final File file;
    public final FileConfiguration config;
    public final Infuse plugin;

    public MainConfig(Infuse plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Reloads the configuration.
     *
     * @return Whether the configuration was loaded successfully or not.
     */
    public boolean load() {
        // Creating the file if it doesn't exist.
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), true);
        }

        // Loading the config
        try {
            config.load(file);
            Infuse.LOGGER.info("Successfully loaded {}", file.getName());
            return true;
        } catch (InvalidConfigurationException e) {
            Infuse.LOGGER.warn("{} contains an invalid YAML configuration.  Verify the contents of the file.", file.getName());
        } catch (IOException e) {
            Infuse.LOGGER.error("Could not find {}.  Check that it exists.", file.getName(), e);
        }

        return false;
    }

    /**
     * Writes the config to the file.
     *
     * @return Whether the config was successfully written or not.
     */
    public boolean save() {
        // Creating the file if it doesn't exist.
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

        // Saving the config
        try {
            config.save(file);
            Infuse.LOGGER.info("Saved {}", file.getName());
            return true;
        } catch (IOException e) {
            Infuse.LOGGER.warn("Could not save {}.  Make sure the user has write permissions.", file.getName());
        }

        return false;
    }

    public List<NamespacedKey> getBlacklistedWorlds(InfuseEffect effect) {
        return config.getStringList(effect.getPlainKey() + ".blacklisted_worlds")
            .stream()
            .filter(Objects::nonNull)
            .map(NamespacedKey::fromString)
            .toList();
    }

    public String lang() {
        return config.getString("lang", "en_US");
    }

    public boolean allowInfiniteEffects() {
        return config.getBoolean("allow_infinite_effects");
    }

    public boolean emptyEffectIcon() {
        return config.getBoolean("empty_effect_icon");
    }

    public boolean playerHeadDrops() {
        return config.getBoolean("player_head_drops");
    }

    public int ritualDuration() {
        return config.getInt("rituals.duration", 600);
    }

    public int ritualDurationEnder() {
        return config.getInt("rituals.ender_duration", 3600);
    }

    public boolean regularBroadcast() {
        return config.getBoolean("rituals.broadcast_regular", true);
    }

    public boolean enableDiscordBroadcasts() {
        return config.getBoolean("rituals.send_webhooks", false);
    }

    public String discordWebhookUrl() {
        return config.getString("rituals.webhook_url", "");
    }

    public boolean ritualBeacon() {
        return config.getBoolean("rituals.beacon", true);
    }

    public boolean useImmortalBrewers() {
        return config.getBoolean("rituals.immortal_brewing_stands", true);
    }

    public boolean brewingGui() {
        return config.getBoolean("brewing_gui");
    }

    public String effectDrops() {
        return config.getString("effect_drops");
    }

    public boolean joinEffectsEnabled() {
        return config.getBoolean("join_effects_enabled");
    }

    public boolean dropOnNaturalDeath() {
        return config.getBoolean("drop_on_natural_death", true);
    }

    public List<InfuseEffect> joinEffects() {
        return config.getStringList("join_effects").stream().map(InfuseEffect::fromString).filter(Objects::nonNull).toList();
    }

    public boolean enableBetterTeams() {
        return config.getBoolean("betterteams.enabled", false);
    }

    public boolean betterTeamsTrustAllies() {
        return config.getBoolean("betterteams.trust_allies", false);
    }

    public boolean enableApophis() {
        return config.getBoolean("extra_effects.Apophis");
    }

    public boolean enableThief() {
        return config.getBoolean("extra_effects.Thief");
    }

    /**
     * Gets the amount of each effect that can be crafted
     *
     * @param effect The effect to check
     *
     * @return The number of effects that can be crafted of the specified {@link InfuseEffect}.
     */
    public int getCraftLimit(InfuseEffect effect) {
        List<Integer> craftLimits = config.getIntegerList("craft_limits." + effect.getPlainKey());

        if (craftLimits.size() != 2) {
            Infuse.LOGGER.error("Craft limits are required to be a list of 2 integers.  Found {} entries for effect {}", craftLimits.size(), effect.getPlainKey());
            Infuse.LOGGER.error("Returning default limits");

            return effect.isAugmented() ? 1 : 3;
        }

        return craftLimits.get(effect.isAugmented() ? 0 : 1);
    }

    public double emeraldLockDurationSeconds() {
        return config.getDouble("emerald.lock_duration_seconds", 10);
    }

    public boolean invisHideKills() {
        return config.getBoolean("invis.hide_kills");
    }

    public boolean invisHideDeaths() {
        return config.getBoolean("invis.hide_deaths");
    }

    public long cooldown(InfuseEffect effect) {
        return config.getLong(effect.getPlainKey() + ".cooldown." + (effect.isAugmented() ? "augmented" : "default"));
    }

    public long duration(InfuseEffect effect) {
        return config.getLong(effect.getPlainKey() + ".duration." + (effect.isAugmented() ? "augmented" : "default"));
    }

    public int speedDashMultiplier() {
        return config.getInt("speed.spark.dash_multiplier");
    }

    public int speedPlayerVelocityMultiplier() {
        return config.getInt("speed.spark.player_velocity_multiplier");
    }

    public int oceanPullInterval() {
        return config.getInt("ocean_pulling.pull.interval");
    }

    public int oceanPullRadius() {
        return config.getInt("ocean_pulling.pull.radius");
    }

    public double oceanPullStrength() {
        return config.getDouble("ocean_pulling.pull.strength");
    }

    public int hitCounterDecaySeconds() {
        return config.getInt("hit_counter_decay_seconds");
    }

    public int emeraldExpPerHit() {
        return config.getInt("emerald.passive.xp_stolen_per_hit");
    }

    public float emeraldExpPercent() {
        return Math.clamp((float) config.getDouble("emerald.passive.xp_stolen_percent"), 0, 1);
    }

    public float emeraldPercentExpToShare() {
        return Math.clamp((float) config.getDouble("emerald.passive.percent_xp_to_share"), 0, 1);
    }

    public int apophisExpPerHit() {
        return config.getInt("apophis.passive.xp_stolen_per_hit");
    }

    public float apophisExpPercent() {
        return Math.clamp((float) config.getDouble("apophis.passive.xp_stolen_percent"), 0, 1);
    }

    public float apophisPercentExpToShare() {
        return Math.clamp((float) config.getDouble("apophis.passive.percent_xp_to_share"), 0, 1);
    }

    public double apophisLockDurationSeconds() {
        return config.getDouble("apophis.passive.lock_duration_seconds", 10);
    }

    public int apophisLootingLevel() {
        return config.getInt("apophis.passive.looting_level");
    }

    public double apophisSparkRadius() {
        return config.getDouble("apophis.spark.radius", 5);
    }

    public double apophisSparkExplosionRadius() {
        return config.getDouble("apophis.spark.explosion_radius", 5);
    }

    public double apophisLavaWalkSpeed() {
        return config.getDouble("apophis.passive.lava_walk_speed", 0.6);
    }

    public int apophisXpMultiplierStandard() {
        return config.getInt("apophis.passive.multiplier_xp", 2);
    }
    public int apophisXpMultiplierSpark() {
        return config.getInt("apophis.spark.multiplier_xp", 4);
    }

    public int emeraldLootingLevel() {
        return config.getInt("emerald.passive.looting_level");
    }

    public int hasteFortuneLevel() {
        return config.getInt("haste.enchantment.fortune_level");
    }

    public int hasteEfficiencyLevel() {
        return config.getInt("haste.enchantment.efficiency_level");
    }

    public int hasteUnbreakingLevel() {
        return config.getInt("haste.enchantment.unbreaking_level");
    }

    public double emeraldMultiplierStandard() {
        return config.getDouble("emerald.passive.xp_multiplier");
    }

    public double emeraldMultiplierUseEffect() {
        return config.getDouble("emerald.spark.xp_multiplier");
    }

    public double enderPassiveRadius() {
        return config.getDouble("ender.passive.radius");
    }

    public int enderSparkMaxDistance() {
        return config.getInt("ender.spark.max_distance");
    }

    public double featherLandRadius() {
        return config.getDouble("feather.land.radius");
    }

    public double featherLandDamage() {
        return config.getDouble("feather.land.damage");
    }

    public double firePassiveWalkSpeed() {
        return config.getDouble("fire.passive.lava_walk_speed");
    }

    public double fireSparkRadius() {
        return config.getDouble("fire.spark.radius");
    }

    public double fireSparkExplosionRadius() {
        return config.getDouble("fire.spark.explosion_radius");
    }

    public int frostPassiveSnowChangingRadius() {
        return config.getInt("frost.passive.snow_changing_radius");
    }

    public double frostPassiveWalkSpeed() {
        return config.getDouble("frost.passive.powdered_snow_walk_speed");
    }

    public double frostSparkRadius() {
        return config.getDouble("frost.spark.radius");
    }

    public int oceanPassiveDrownStrength() {
        return config.getInt("ocean.passive.drown_strength");
    }

    public int oceanPassiveDrownDamage() {
        return config.getInt("ocean.passive.drown_damage");
    }

    public int oceanSparkDrownStrength() {
        return config.getInt("ocean.spark.drown_strength");
    }

    public int oceanSparkDrownDamage() {
        return config.getInt("ocean.spark.drown_damage");
    }

    public double regenSparkHealTrustedRadius() {
        return config.getDouble("regen.spark.heal_trusted_radius");
    }

    public double thunderSparkBaseRadius() {
        return config.getDouble("thunder.spark.base_radius");
    }

    public double thunderSparkPerPlayerBoostRadius() {
        return config.getDouble("thunder.spark.per_player_boost_radius");
    }

    public void changeConfigValue(String old, String key) {
        if (config.get(key) == null) config.set(key, config.get(old));
        config.set(old, null);

        final Configuration defaults = config.getDefaults();
        if (defaults != null) defaults.set(old, null);

        // very scuffed method for removing the old path, but oh well it works
        String current = old;
        while (current.contains(".")) {
            final String path = current.substring(0, current.lastIndexOf("."));
            final ConfigurationSection section = config.getConfigurationSection(path);

            if (section != null && section.getKeys(false).isEmpty()) {
                config.set(path, null);
                if (defaults != null) defaults.set(path, null);
                current = path;
            } else {
                break;
            }
        }
    }

    public void applyUpdates() {
        if (this.config.getString("config_version") == null) {
            Infuse.LOGGER.info("Old configuration version has been found, updating configuration. This may take a minute.");
            this.config.set("config_version", "1.0");

            changeConfigValue("apophis.percent_xp_to_share", "apophis.passive.percent_xp_to_share");
            changeConfigValue("apophis.xp_stolen_per_hit", "apophis.passive.xp_stolen_per_hit");
            changeConfigValue("apophis.xp_stolen_percent", "apophis.passive.xp_stolen_percent");
            changeConfigValue("apophis.looting_level", "apophis.passive.looting_level");
            changeConfigValue("apophis.lock_duration_seconds", "apophis.passive.lock_duration_seconds");
            changeConfigValue("apophis.enchantment.looting_level", "apophis.passive.looting_level");
            changeConfigValue("apophis.passive.walk-speed", "apophis.passive.lava_walk_speed");
            changeConfigValue("apophis.spark.explosion-radius", "apophis.spark.explosion_radius");
            changeConfigValue("apophis.multiplier-xp.standard", "apophis.passive.xp_multiplier");
            changeConfigValue("apophis.multiplier-xp.use-effect", "apophis.spark.xp_multiplier");

            changeConfigValue("emerald.xp_stolen_per_hit", "emerald.passive.xp_stolen_per_hit");
            changeConfigValue("emerald.xp_stolen_percent", "emerald.passive.xp_stolen_percent");
            changeConfigValue("emerald.percent_xp_to_share", "emerald.passive.percent_xp_to_share");
            changeConfigValue("emerald.lock_duration_seconds", "emerald.passive.lock_duration_seconds");
            changeConfigValue("emerald.enchantment.looting_level", "emerald.passive.looting_level");
            changeConfigValue("emerald.multiplier-xp.standard", "emerald.passive.xp_multiplier");
            changeConfigValue("emerald.multiplier-xp.use-effect", "emerald.spark.xp_multiplier");

            changeConfigValue("ender.spark.max-distance", "ender.spark.max_distance");

            changeConfigValue("fire.passive.walk-speed", "fire.passive.lava_walk_speed");
            changeConfigValue("fire.spark.explosion-radius", "fire.spark.explosion_radius");

            changeConfigValue("frost.passive.snow-changing-radius", "frost.passive.snow_changing_radius");
            changeConfigValue("frost.passive.walk-speed", "frost.passive.powdered_snow_walk_speed");

            changeConfigValue("ocean.passive.drown-strength", "ocean.passive.drown_strength");
            changeConfigValue("ocean.passive.drown-damage", "ocean.passive.drown_damage");

            changeConfigValue("ocean.spark.drown-strength", "ocean.passive.drown_strength");
            changeConfigValue("ocean.spark.drown-damage", "ocean.passive.drown_damage");
            changeConfigValue("regen.spark.heal-trusted-radius", "regen.spark.heal_trusted_radius");

            changeConfigValue("speed.dashMultiplier", "speed.spark.dash_multiplier");
            changeConfigValue("speed.playerVelocityMultiplier", "speed.spark.player_velocity_multiplier");

            changeConfigValue("thunder.spark.base-radius", "thunder.spark.base_radius");
            changeConfigValue("thunder.spark.per-player-boost-radius", "thunder.spark.per_player_boost_radius");

            changeConfigValue("apophis.blacklisted-worlds", "apophis.blacklisted_worlds");
            changeConfigValue("thief.blacklisted-worlds", "thief.blacklisted_worlds");
            changeConfigValue("emerald.blacklisted-worlds", "emerald.blacklisted_worlds");
            changeConfigValue("ender.blacklisted-worlds", "ender.blacklisted_worlds");
            changeConfigValue("feather.blacklisted-worlds", "feather.blacklisted_worlds");
            changeConfigValue("fire.blacklisted-worlds", "fire.blacklisted_worlds");
            changeConfigValue("frost.blacklisted-worlds", "frost.blacklisted_worlds");
            changeConfigValue("haste.blacklisted-worlds", "haste.blacklisted_worlds");
            changeConfigValue("heart.blacklisted-worlds", "heart.blacklisted_worlds");
            changeConfigValue("invis.blacklisted-worlds", "invis.blacklisted_worlds");
            changeConfigValue("ocean.blacklisted-worlds", "ocean.blacklisted_worlds");
            changeConfigValue("regen.blacklisted-worlds", "regen.blacklisted_worlds");
            changeConfigValue("speed.blacklisted-worlds", "speed.blacklisted_worlds");
            changeConfigValue("strength.blacklisted-worlds", "strength.blacklisted_worlds");
            changeConfigValue("thunder.blacklisted-worlds", "thunder.blacklisted_worlds");

            Infuse.LOGGER.info("Configuration update successful!");
        }

        if (config.contains("ritual_duration")) {
            config.set("rituals.duration", config.get("ritual_duration"));
            config.set("rituals.ender_duration", config.get("ritual_duration_ender"));
            config.set("rituals.broadcast_regular", config.get("regular_effect_broadcast"));
            config.set("rituals.send_webhooks", config.get("enable_discord_broadcasts"));
            config.set("rituals.webhook_url", config.get("discord_webhook_url"));
            config.set("rituals.beacon", config.get("ritual_beacon"));
            config.set("rituals.immortal_brewing_stands", true);
        }

        if (!config.contains("invis_deaths")) config.set("invis_deaths", null);
        if (!config.contains("invis.hide_kills")) config.set("invis.hide_kills", false);
        if (!config.contains("invis.hide_deaths")) config.set("invis.hide_deaths", false);

        if (!config.contains("haste.enchantment.looting_level")) config.set("haste.enchantment.looting_level", 5);
        if (!config.contains("haste.enchantment.fortune_level")) config.set("haste.enchantment.fortune_level", 5);
        if (!config.contains("haste.enchantment.efficiency_level")) config.set("haste.enchantment.efficiency_level", 10);
        if (!config.contains("haste.enchantment.unbreaking_level")) config.set("haste.enchantment.unbreaking_level", 5);

        if (!config.contains("hit_counter_decay_seconds")) config.set("hit_counter_decay_seconds", 15);

        if (!config.contains("emerald.passive.xp_stolen_per_hit")) config.set("emerald.xp_stolen_per_hit", 15);
        if (!config.contains("emerald.passive.xp_stolen_percent")) config.set("emerald.xp_stolen_percent", 1);
        if (!config.contains("emerald.passive.percent_xp_to_share")) config.set("emerald.percent_xp_to_share", 0.5);
        if (!config.contains("emerald.passive.xp_multiplier")) config.set("emerald.passive.xp_multiplier", 2);
        if (!config.contains("emerald.spark.xp_multiplier")) config.set("emerald.spark.xp_multiplier", 4);

        if (!config.contains("apophis.passive.percent_xp_to_share")) config.set("apophis.percent_xp_to_share", 0.5);
        if (!config.contains("apophis.passive.xp_stolen_per_hit")) config.set("apophis.xp_stolen_per_hit", 15);
        if (!config.contains("apophis.passive.xp_stolen_percent")) config.set("apophis.xp_stolen_percent", 1);
        if (!config.contains("apophis.passive.looting_level")) config.set("apophis.passive.looting_level", 5);
        if (!config.contains("apophis.passive.lock_duration_seconds")) config.set("apophis.lock_duration_seconds", 10);
        if (!config.contains("apophis.spark.radius")) config.set("apophis.spark.radius", 5);
        if (!config.contains("apophis.spark.explosion_radius")) config.set("apophis.spark.explosion_radius", 5);
        if (!config.contains("apophis.passive.lava_walk_speed")) config.set("apophis.passive.lava_walk_speed", 0.6);
        if (!config.contains("apophis.passive.xp_multiplier")) config.set("emerald.passive.xp_multiplier", 2);
        if (!config.contains("apophis.spark.xp_multiplier")) config.set("emerald.spark.xp_multiplier", 4);

        if (!config.contains("ender.passive.radius")) config.set("ender.passive.radius", 10);
        if (!config.contains("ender.spark.max_distance")) config.set("ender.spark.max_distance", 15);

        if (!config.contains("feather.land.radius")) config.set("feather.land.radius", 4);
        if (!config.contains("feather.land.damage")) config.set("feather.land.damage", 8);

        if (!config.contains("fire.passive.walk_speed")) config.set("fire.passive.walk_speed", 0.6);
        if (!config.contains("fire.spark.radius")) config.set("fire.spark.radius", 5);
        if (!config.contains("fire.spark.explosion_radius")) config.set("fire.spark.explosion_radius", 5);

        if (!config.contains("frost.passive.snow_changing_radius")) config.set("frost.passive.snow_changing_radius", 3);
        if (!config.contains("frost.passive.powdered_snow_walk_speed")) config.set("frost.passive.powdered_snow_walk_speed", 0.6);
        if (!config.contains("frost.spark.radius")) config.set("frost.spark.radius", 5);

        if (!config.contains("ocean.passive.drown_strength")) config.set("ocean.passive.drown_strength", 5);
        if (!config.contains("ocean.passive.drown_damage")) config.set("ocean.passive.drown_damage", 1);
        if (!config.contains("ocean.spark.drown_strength")) config.set("ocean.spark.drown_strength", 20);
        if (!config.contains("ocean.spark.drown_damage")) config.set("ocean.spark.drown_damage", 2);

        if (!config.contains("regen.spark.heal_trusted_radius")) config.set("regen.spark.heal_trusted_radius", 5);

        if (!config.contains("thunder.spark.base_radius")) config.set("thunder.spark.base_radius", 10);
        if (!config.contains("thunder.spark.per_player_boost_radius")) config.set("thunder.spark.per_player_boost_radius", 0.3);

        if (!config.contains("apophis.blacklisted_worlds")) config.set("apophis.blacklisted_worlds", List.of());
        if (!config.contains("thief.blacklisted_worlds")) config.set("theif.blacklisted_worlds", List.of());
        if (!config.contains("emerald.blacklisted_worlds")) config.set("emerald.blacklisted_worlds", List.of());
        if (!config.contains("ender.blacklisted_worlds")) config.set("ender.blacklisted_worlds", List.of());
        if (!config.contains("feather.blacklisted_worlds")) config.set("feather.blacklisted_worlds", List.of());
        if (!config.contains("fire.blacklisted_worlds")) config.set("fire.blacklisted_worlds", List.of());
        if (!config.contains("frost.blacklisted_worlds")) config.set("frost.blacklisted_worlds", List.of());
        if (!config.contains("haste.blacklisted_worlds")) config.set("haste.blacklisted_worlds", List.of());
        if (!config.contains("heart.blacklisted_worlds")) config.set("heart.blacklisted_worlds", List.of());
        if (!config.contains("invis.blacklisted_worlds")) config.set("invis.blacklisted_worlds", List.of());
        if (!config.contains("ocean.blacklisted_worlds")) config.set("ocean.blacklisted_worlds", List.of());
        if (!config.contains("regen.blacklisted_worlds")) config.set("regen.blacklisted_worlds", List.of());
        if (!config.contains("speed.blacklisted_worlds")) config.set("speed.blacklisted_worlds", List.of());
        if (!config.contains("strength.blacklisted_worlds")) config.set("strength.blacklisted_worlds", List.of());
        if (!config.contains("thunder.blacklisted_worlds")) config.set("thunder.blacklisted_worlds", List.of());

        save();
    }
}

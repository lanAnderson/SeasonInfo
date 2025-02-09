package club.iananderson.seasonhud.impl.seasons.mods;

import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.Calendar;
import club.iananderson.seasonhud.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import sereneseasons.config.SeasonsConfig;

public class SereneSeasonsHelper {
  public static Item CALENDAR = Registry.ITEM.get(new ResourceLocation("sereneseasons", "calendar"));

  private SereneSeasonsHelper() {
  }

  public static boolean isTropicalSeason(Player player) {
    return false;
  }

  public static String getCurrentSubSeason(Player player) {
    return Services.PLATFORM.getCurrentSubSeason(player); //1.16.5 Forge weirdness
  }

  public static String getCurrentSeason(Player player) {
    return Services.PLATFORM.getCurrentSeason(player); //1.16.5 Forge weirdness
  }

  public static long getDate(Player player) {
    return Services.PLATFORM.getSeasonDate(player); //1.16.5 Forge weirdness
  }

  public static int seasonDuration(Player player) {
    int duration = SeasonsConfig.subSeasonDuration.get() * 3;

    if (isTropicalSeason(player)) {
      duration *= 2; //Tropical seasons are twice as long (Default 48 days)
    }
    if (Config.getShowSubSeason() && Calendar.validDetailedMode()) {
      duration /= 3; //3 sub-seasons per season
    }

    return duration;
  }
}

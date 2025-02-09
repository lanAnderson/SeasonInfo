package club.iananderson.seasonhud.impl.seasons.mods;

import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.Calendar;
import club.iananderson.seasonhud.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;

public class SereneSeasonsHelper {
  public static Item CALENDAR = Registry.ITEM.get(new ResourceLocation("sereneseasons", "calendar"));

  private SereneSeasonsHelper() {
  }

  public static boolean isTropicalSeason(Player player) {
    boolean showTropicalSeasons = Config.getShowTropicalSeason();
    boolean isInTropicalSeason = sereneseasons.api.season.SeasonHelper.usesTropicalSeasons(
        player.level.getBiome(player.getOnPos()));

    return showTropicalSeasons && isInTropicalSeason;
  }

  public static String getCurrentSubSeason(Player player) {
    ISeasonState currentSeasonState = sereneseasons.api.season.SeasonHelper.getSeasonState(player.level);

    if (isTropicalSeason(player)) {
      return currentSeasonState.getTropicalSeason().toString();
    }
    else {
      return currentSeasonState.getSubSeason().toString();
    }
  }

  public static String getCurrentSeason(Player player) {
    ISeasonState currentSeasonState = sereneseasons.api.season.SeasonHelper.getSeasonState(player.level);
    if (isTropicalSeason(player)) {
      // Removes the "Early", "Mid", "Late" from the tropical season.
      String currentSubSeason = getCurrentSubSeason(player);

      return currentSubSeason.substring(currentSubSeason.length() - 3);
    }
    else {
      return currentSeasonState.getSeason().toString();
    }
  }

  public static long getDate(Player player) {
    ISeasonState currentSeasonState = SeasonHelper.getSeasonState(player.level);
    long seasonDay = currentSeasonState.getDay(); //Current day out of the year (Default 24 days * 4 = 96 days)
    long subSeasonDuration = ServerConfig.subSeasonDuration.get(); //In case the default duration is changed
    long subSeasonDate = (seasonDay % subSeasonDuration) + 1; //Default 8 days in each sub-season (1 week)
    long seasonDate = (seasonDay % (subSeasonDuration * 3)) + 1; //Default 24 days in a season (8 days * 3)

    if (Config.getShowSubSeason()) {
      if (isTropicalSeason(player)) {
        // Default 16 days in each tropical "sub-season".
        // Starts are "Early Dry" (Summer 1), so need to offset Spring 1 -> Summer 1 (subSeasonDuration * 3)
        subSeasonDate = ((seasonDay + (subSeasonDuration * 3)) % (subSeasonDuration * 2)) + 1;
      }
      return subSeasonDate;
    }
    else {
      if (isTropicalSeason(player)) {
        // Default 48 days in each tropical season.
        // Starts are "Early Dry" (Summer 1), so need to offset Spring 1 -> Summer 1 (subSeasonDuration * 3)
        seasonDate = ((seasonDay + (subSeasonDuration * 3)) % (subSeasonDuration * 6)) + 1;
      }
      return seasonDate;
    }
  }

  public static int seasonDuration(Player player) {
    int duration = ServerConfig.subSeasonDuration.get() * 3;

    if (isTropicalSeason(player)) {
      duration *= 2; //Tropical seasons are twice as long (Default 48 days)
    }
    if (Config.getShowSubSeason() && Calendar.validDetailedMode()) {
      duration /= 3; //3 sub-seasons per season
    }

    return duration;
  }
}

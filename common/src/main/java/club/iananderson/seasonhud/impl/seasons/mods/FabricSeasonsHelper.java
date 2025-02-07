package club.iananderson.seasonhud.impl.seasons.mods;

import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.platform.Services;
import io.github.lucaargolo.seasons.FabricSeasons;
import io.github.lucaargolo.seasons.utils.Season;
import io.github.lucaargolo.seasonsextras.FabricSeasonsExtras;
import java.time.LocalDateTime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class FabricSeasonsHelper {
  private FabricSeasonsHelper(){
  }

  public static Item CALENDAR = Services.PLATFORM.calendar();

  public static boolean isSeasonTiedWithSystemTime = FabricSeasons.CONFIG.isSeasonTiedWithSystemTime();

  public static String getCurrentSubSeason(Player player) {
    Season currentSeasonState = FabricSeasons.getCurrentSeason(player.level);

    if (currentSeasonState.toString().equalsIgnoreCase("fall")) {
      return "Autumn";
    }
    else {
      return currentSeasonState.toString();
    }
  }

  public static String getCurrentSeason(Player player) {
    Season currentSeasonState = FabricSeasons.getCurrentSeason(player.level);

    if (currentSeasonState.toString().equalsIgnoreCase("fall")) {
      return "Autumn";
    }
    else {
      return currentSeasonState.toString();
    }
  }

  public static long getDate(Player player) {
    long dayLength = Config.getDayLength();
    long seasonLength = FabricSeasons.CONFIG.getSpringLength();
    long worldTime = Math.toIntExact(player.level.getDayTime());

    // Get the current day of month from the system. Used with fabric seasons' system time tied with season option
    if (FabricSeasonsHelper.isSeasonTiedWithSystemTime) {
      return LocalDateTime.now().getDayOfMonth();
    }
    else {
      return ((worldTime - (worldTime / seasonLength * seasonLength)) % seasonLength / dayLength) + 1;
    }
  }

  public static int seasonDuration(Player player) {
    int dayLength = Config.getDayLength();

    return FabricSeasons.CONFIG.getSpringLength() / dayLength;
  }
}

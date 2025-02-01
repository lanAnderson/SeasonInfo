package club.iananderson.seasonhud.impl.seasons;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.config.Config;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.lucaargolo.seasons.FabricSeasons;
import io.github.lucaargolo.seasons.utils.Season;
import io.github.lucaargolo.seasonsextras.FabricSeasonsExtras;
import io.wispforest.accessories.api.AccessoriesCapability;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.Month;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import sereneseasons.api.SSItems;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.init.ModConfig;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class CommonSeasonHelper {
  /**
   * Checks if the tropical season should be displayed (SereneSeasons only). Always false for FabricSeasons.
   *
   * @return If the tropical season should be displayed for the platform.
   */
  public static boolean isTropicalSeason(Player player) {
    if (Common.sereneSeasonsLoaded()) {
      boolean showTropicalSeasons = Config.getShowTropicalSeason();
      boolean isInTropicalSeason = sereneseasons.api.season.SeasonHelper.usesTropicalSeasons(
          player.level().getBiome(player.getOnPos()));

      return showTropicalSeasons && isInTropicalSeason;
    }
    else {
      return false;
    }
  }

  /**
   * Checks if "isSeasonTiedWithSystemTime" config option is enabled (FabricSeasons only). Always false for
   * SereneSeasons.
   *
   * @return If "isSeasonTiedWithSystemTime" config option is enabled for the platform.
   */
  public static boolean isSeasonTiedWithSystemTime() {
    if (Common.fabricSeasonsLoaded()) {
      return FabricSeasons.CONFIG.isSeasonTiedWithSystemTime();
    }
    else {
      return false;
    }
  }

  /**
   * Gets the name of the current sub-season for the platform (if applicable).
   *
   * @return The name of the current season for the platform.
   */
  public static String getCurrentSubSeason(Player player) {
    String subSeason = "MID_NULL";

    if (Common.fabricSeasonsLoaded()) {
      Season currentSeasonState = FabricSeasons.getCurrentSeason(player.level());

      if (currentSeasonState.toString().equalsIgnoreCase("fall")) {
        subSeason = "Autumn";
      }
      else {
        subSeason = currentSeasonState.toString();
      }
    }

    if (Common.sereneSeasonsLoaded()) {
      ISeasonState currentSeasonState = sereneseasons.api.season.SeasonHelper.getSeasonState(player.level());

      if (isTropicalSeason(player)) {
        subSeason = currentSeasonState.getTropicalSeason().toString();
      }
      else {
        subSeason = currentSeasonState.getSubSeason().toString();
      }
    }

    if(Common.terrafirmacraftLoaded()){
      subSeason = Calendars.CLIENT.getCalendarMonthOfYear().getSeason().getSerializedName();
    }
    return subSeason;
  }

  /**
   * Gets the name of the current season for the platform.
   *
   * @return The name of the current season for the platform.
   */
  public static String getCurrentSeason(Player player) {
    String season = "NULL";

    if (Common.fabricSeasonsLoaded()) {
      Season currentSeasonState = FabricSeasons.getCurrentSeason(player.level());

      if (currentSeasonState.toString().equalsIgnoreCase("fall")) {
        season = "Autumn";
      }
      else {
        season = currentSeasonState.toString();
      }
    }

    if (Common.sereneSeasonsLoaded()) {
      ISeasonState currentSeasonState = sereneseasons.api.season.SeasonHelper.getSeasonState(player.level());
      if (isTropicalSeason(player)) {
        // Removes the "Early", "Mid", "Late" from the tropical season.
        String currentSubSeason = getCurrentSubSeason(player);
        season = currentSubSeason.substring(currentSubSeason.length() - 3);
      }
      else {
        season = currentSeasonState.getSeason().toString();
      }
    }

    if(Common.terrafirmacraftLoaded()){
      season = Calendars.CLIENT.getCalendarMonthOfYear().getSeason().getSerializedName();
    }

    return season;
  }

  /**
   * Gets the current season's file name for the platform.
   *
   * @return The current season's file name for the platform.
   */
  public static String getSeasonFileName(Player player) {
    return getCurrentSeason(player).toLowerCase();
  }

  /**
   * Gets the current day of the season/sub-season.
   *
   * @return The current day of the season/sub-season.
   */
  public static int getDate(Player player) {
    int date = 0;
    int dayLength = Config.getDayLength();

    if (Common.fabricSeasonsLoaded()) {
      int seasonLength = FabricSeasons.CONFIG.getSpringLength();
      int worldTime = Math.toIntExact(player.level().getDayTime());

      date = ((worldTime - (worldTime / seasonLength * seasonLength)) % seasonLength / dayLength) + 1;

      // Get the current day of month from the system. Used with fabric seasons' system time tied with season option
      if (isSeasonTiedWithSystemTime()) {
        date = LocalDateTime.now().getDayOfMonth();
      }
    }

    if (Common.sereneSeasonsLoaded()) {
      ISeasonState currentSeasonState = SeasonHelper.getSeasonState(player.level());
      int seasonDay = currentSeasonState.getDay(); //Current day out of the year (Default 24 days * 4 = 96 days)
      int subSeasonDuration = ModConfig.seasons.subSeasonDuration; //In case the default duration is changed
      int subSeasonDate = (seasonDay % subSeasonDuration) + 1; //Default 8 days in each sub-season (1 week)
      int seasonDate = (seasonDay % (subSeasonDuration * 3)) + 1; //Default 24 days in a season (8 days * 3)

      if (Config.getShowSubSeason()) {
        if (isTropicalSeason(player)) {
          // Default 16 days in each tropical "sub-season".
          // Starts are "Early Dry" (Summer 1), so need to offset Spring 1 -> Summer 1 (subSeasonDuration * 3)
          subSeasonDate = ((seasonDay + (subSeasonDuration * 3)) % (subSeasonDuration * 2)) + 1;
        }
        date = subSeasonDate;
      }
      else {
        if (isTropicalSeason(player)) {
          // Default 48 days in each tropical season.
          // Starts are "Early Dry" (Summer 1), so need to offset Spring 1 -> Summer 1 (subSeasonDuration * 3)
          seasonDate = ((seasonDay + (subSeasonDuration * 3)) % (subSeasonDuration * 6)) + 1;
        }
        date = seasonDate;
      }
    }

    if(Common.terrafirmacraftLoaded()){
        //DECEMBER(-0.866F, Season.WINTER);
        //JANUARY(-1.0F, Season.WINTER),
        //FEBRUARY(-0.866F, Season.WINTER),
        //
        //MARCH(-0.5F, Season.SPRING),
        //APRIL(0.0F, Season.SPRING),
        //MAY(0.5F, Season.SPRING),
        //
        //JUNE(0.866F, Season.SUMMER),
        //JULY(1.0F, Season.SUMMER),
        //AUGUST(0.866F, Season.SUMMER),
        //
        //SEPTEMBER(0.5F, Season.FALL),
        //OCTOBER(0.0F, Season.FALL),
        //NOVEMBER(-0.5F, Season.FALL),

      Month currentMonth = Calendars.CLIENT.getCalendarMonthOfYear();
      int dayOfMonth = Calendars.CLIENT.getCalendarDayOfMonth();
      int daysInMonth = Calendars.CLIENT.getCalendarDaysInMonth();
      float percentMonth = Calendars.CLIENT.getCalendarFractionOfMonth();

      //Todo:
      // Month 1 -> Early, Month 2 -> Mid, Month 3 -> Early
      // Calculate the percentage based on current month and its order in the season


    }
    return date;
  }

  /**
   * Checks the duration of the current season/sub-season.
   *
   * @return The duration of the current season/sub-season.
   */
  public static int seasonDuration(Player player) {
    int duration = 0;
    int dayLength = Config.getDayLength();

    if (Common.fabricSeasonsLoaded()) {
      duration = FabricSeasons.CONFIG.getSpringLength() / dayLength;
    }

    if (Common.sereneSeasonsLoaded()) {
      duration = ModConfig.seasons.subSeasonDuration * 3;

      if (isTropicalSeason(player)) {
        duration *= 2; //Tropical seasons are twice as long (Default 48 days)
      }
      if (Config.getShowSubSeason() && Calendar.validDetailedMode()) {
        duration /= 3; //3 sub-seasons per season
      }
    }

    return duration;
  }

  /**
   * @return The calendar item for the loaded season mod.
   */
  public static Item calendar() {
    Item calendar = null;

    if (Common.fabricSeasonsLoaded() && Common.extrasLoaded()) {
      calendar = FabricSeasonsExtras.SEASON_CALENDAR_ITEM;
    }

    if (Common.sereneSeasonsLoaded()) {
      calendar = SSItems.CALENDAR;
    }

    return calendar;
  }

  /**
   * @param player The player whose Curios/Trinket inventory will be searched.
   * @param item   The item that is being searched for.
   * @return The int for the Curios/Trinket inventory location
   */
  public static boolean findCuriosCalendar(Player player, Item item) {
    Minecraft mc = Minecraft.getInstance();
    boolean curioEquipped = false;

    if (mc.level == null || mc.player == null || item == null) {
      return false;
    }

    if (Common.curiosLoaded() && !Common.accessoriesLoaded()) {
      List<SlotResult> curiosInventory = CuriosApi.getCuriosHelper().findCurios(player, item);
      curioEquipped = !curiosInventory.isEmpty();
    }

    if (Common.trinketsLoaded() && !Common.accessoriesLoaded()) {
      Optional<TrinketComponent> trinketInventory = TrinketsApi.getTrinketComponent(player);

      if (trinketInventory.isPresent()) {
        curioEquipped = trinketInventory.get().isEquipped(item);
      }
    }

    else if (Common.accessoriesLoaded()) {
      Optional<AccessoriesCapability> accessoriesInventory = AccessoriesCapability.getOptionally(player);
      if (accessoriesInventory.isPresent()) {
        curioEquipped = !accessoriesInventory.get().getEquipped(item).isEmpty();
      }
    }
    return curioEquipped;
  }
}

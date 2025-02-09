package club.iananderson.seasonhud.forge.platform;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.platform.services.IPlatformHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.SeasonsConfig;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class ForgePlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {
    return "Forge";
  }

  @Override
  public boolean isModLoaded(String modId) {
    return ModList.get().isLoaded(modId);
  }

  @Override
  public String getModVersion(String modId) {
    Optional<? extends ModContainer> mod = ModList.get().getModContainerById(modId);

    if (mod.isPresent()) {
      return mod.get().getModInfo().getVersion().toString();
    }
    else {
      return "Not Loaded";
    }
  }

  @Override
  public boolean isDevelopmentEnvironment() {
    return !FMLLoader.isProduction();
  }

  @Override
  public boolean curiosFound(Player player, Item item) {
    boolean curioEquipped = false;

    if (Common.curiosLoaded() && !Common.accessoriesLoaded()) {
      List<SlotResult> findCalendar = CuriosApi.getCuriosHelper().findCurios(player, item.getItem());

      curioEquipped = !findCalendar.isEmpty();
    }
    return curioEquipped;
  }

  @Override
  public String getCurrentSeason(Player player) {
    ISeasonState currentSeasonState = sereneseasons.api.season.SeasonHelper.getSeasonState(player.level);

    return currentSeasonState.getSeason().toString();
  }

  @Override
  public String getCurrentSubSeason(Player player) {
    ISeasonState currentSeasonState = sereneseasons.api.season.SeasonHelper.getSeasonState(player.level);

    return currentSeasonState.getSubSeason().toString();
  }

  @Override
  public long getSeasonDate(Player player) {
    ISeasonState currentSeasonState = SeasonHelper.getSeasonState(player.level);
    long seasonDay = currentSeasonState.getDay(); //Current day out of the year (Default 24 days * 4 = 96 days)
    long subSeasonDuration = SeasonsConfig.subSeasonDuration.get(); //In case the default duration is changed
    long subSeasonDate = (seasonDay % subSeasonDuration) + 1; //Default 8 days in each sub-season (1 week)
    long seasonDate = (seasonDay % (subSeasonDuration * 3)) + 1; //Default 24 days in a season (8 days * 3)

    if (Config.getShowSubSeason()) {
      return subSeasonDate;
    }
    else {
      return seasonDate;
    }
  }
}

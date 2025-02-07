package club.iananderson.seasonhud.fabric.platform;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.platform.services.IPlatformHelper;
import io.github.lucaargolo.seasonsextras.FabricSeasonsExtras;
import java.util.Optional;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.world.item.Item;

public class FabricPlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {
    return "Fabric";
  }

  @Override
  public boolean isModLoaded(String modId) {
    return FabricLoader.getInstance().isModLoaded(modId);
  }

  @Override
  public String getModVersion(String modId) {
    Optional<? extends ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);

    if (mod.isPresent()) {
      return mod.get().getMetadata().getVersion().toString();
    }
    else {
      return "Not Loaded";
    }
  }

  @Override
  public boolean isDevelopmentEnvironment() {
    return FabricLoader.getInstance().isDevelopmentEnvironment();
  }

  @Override
  public Item calendar() {
    if (Common.extrasLoaded()){
      return FabricSeasonsExtras.SEASON_CALENDAR_ITEM;
    }
    else return null;
  }
}

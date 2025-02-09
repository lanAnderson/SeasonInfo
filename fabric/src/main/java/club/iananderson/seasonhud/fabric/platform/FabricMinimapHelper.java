package club.iananderson.seasonhud.fabric.platform;

import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.platform.services.IMinimapHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import pepjebs.mapatlases.MapAtlasesMod;
import pepjebs.mapatlases.utils.MapAtlasesAccessUtils;

public class FabricMinimapHelper implements IMinimapHelper {
  // Needed for older versions. Makes it easier to port.
  @Override
  public boolean hideMapAtlases() {
    if (CurrentMinimap.mapAtlasesLoaded()) {
      Minecraft mc = Minecraft.getInstance();

      if (mc.level == null || mc.player == null || mc.player.level.dimension() != Level.OVERWORLD) {
        return true;
      }

      ItemStack atlas = MapAtlasesAccessUtils.getAtlasFromPlayerByConfig(mc.player.inventory);

      boolean drawMinimapHud = MapAtlasesMod.CONFIG.drawMiniMapHUD;

      boolean hasAtlas = atlas.getCount() > 0;

      return !drawMinimapHud || !hasAtlas;
    }
    else {
      return false;
    }
  }

  @Override
  public boolean hideHudInCurrentDimension() {
    return false;
  }
}
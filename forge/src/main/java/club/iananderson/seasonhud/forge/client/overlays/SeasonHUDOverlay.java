package club.iananderson.seasonhud.forge.client.overlays;

import club.iananderson.seasonhud.client.overlays.SeasonHUDOverlayCommon;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import org.jetbrains.annotations.NotNull;

public class SeasonHUDOverlay implements LayeredDraw.Layer {
  public static SeasonHUDOverlay HUD_INSTANCE;

  public static void init() {
    HUD_INSTANCE = new SeasonHUDOverlay();
  }

  @Override
  public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker deltaTracker) {
    SeasonHUDOverlayCommon.render(graphics);
  }
}

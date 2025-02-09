package club.iananderson.seasonhud.platform.services;

import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.Minimap;

public interface IMinimapHelper {
  /**
   * Needed to do differences in Forge and Fabric versions, depending on the Minecraft version.
   *
   * @return If the MapAtlases minimap is not displayed
   */
  boolean hideMapAtlases();

  /**
   * Checks if the provided minimap mod has the minimap hidden
   *
   * @return True if the current minimap mod has the minimap hidden
   */
  boolean hiddenMinimap(Minimap minimap);
}
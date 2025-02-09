package club.iananderson.seasonhud.platform.services;

public interface IMinimapHelper {
  /**
   * Needed to do differences in Forge and Fabric versions, depending on the Minecraft version.
   *
   * @return If the MapAtlases minimap is not displayed
   */
  boolean hideMapAtlases();

  boolean hideHudInCurrentDimension();
}
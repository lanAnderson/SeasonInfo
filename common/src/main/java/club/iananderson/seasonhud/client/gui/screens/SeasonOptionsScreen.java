package club.iananderson.seasonhud.client.gui.screens;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.client.gui.Location;
import club.iananderson.seasonhud.client.gui.ShowDay;
import club.iananderson.seasonhud.client.gui.components.buttons.CheckButton;
import club.iananderson.seasonhud.client.gui.components.sliders.BasicSlider;
import club.iananderson.seasonhud.client.gui.components.sliders.HudOffsetSlider;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.CurrentSeason;
import java.util.Arrays;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class SeasonOptionsScreen extends SeasonHudScreen {
  private static final Component SCREEN_TITLE = Component.translatable("menu.seasonhud.season.title");
  private Location hudLocation;
  private int xSliderInt;
  private int ySliderInt;
  private ShowDay showDay;
  private boolean seasonColor;
  private boolean showSubSeason;
  private boolean showTropicalSeason;
  private boolean needCalendar;
  private boolean enableCalanderDetail;
  private int dayLength;
  private int newDayLength;
  private CycleButton<Location> hudLocationButton;
  private HudOffsetSlider xSlider;
  private HudOffsetSlider ySlider;
  private EditBox dayLengthBox;
  private CycleButton<ShowDay> showDayButton;
  private CycleButton<Boolean> showSubSeasonButton;
  private CycleButton<Boolean> needCalendarButton;
  private CycleButton<Boolean> calanderDetailModeButton;

  public SeasonOptionsScreen(Screen parentScreen) {
    super(parentScreen, SCREEN_TITLE);
    loadConfig();
  }

  public static SeasonOptionsScreen getInstance(Screen parentScreen) {
    return new SeasonOptionsScreen(parentScreen);
  }

  public void loadConfig() {
    hudLocation = Config.getHudLocation();
    xSliderInt = Config.getHudX();
    ySliderInt = Config.getHudY();
    showDay = Config.getShowDay();
    seasonColor = Config.getEnableSeasonNameColor();
    showSubSeason = Config.getShowSubSeason();
    showTropicalSeason = Config.getShowTropicalSeason();
    needCalendar = Config.getNeedCalendar();
    enableCalanderDetail = Config.getCalanderDetailMode();
    dayLength = Config.getDayLength();
  }

  public void saveConfig() {
    Config.setHudX(xSlider.getValueInt());
    Config.setHudY(ySlider.getValueInt());
    Config.setNeedCalendar(needCalendar);
    if (Common.fabricSeasonsLoaded()) {
      Config.setDayLength(Integer.parseInt(dayLengthBox.getValue()));
    }
  }

  @Override
  public void onDone() {
    saveConfig();
    super.onDone();
  }

  @Override
  public void onClose() {
    Config.setHudLocation(hudLocation);
    Config.setShowDay(showDay);
    Config.setEnableSeasonNameColor(seasonColor);
    Config.setShowSubSeason(showSubSeason);
    Config.setShowTropicalSeason(showTropicalSeason);
    Config.setCalanderDetailMode(enableCalanderDetail);
    Config.setDayLength(dayLength);
    super.onClose();
  }

  @Override
  public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    xSlider.active = hudLocationButton.getValue() == Location.TOP_LEFT;
    ySlider.active = hudLocationButton.getValue() == Location.TOP_LEFT;

    if (Common.fabricSeasonsLoaded()) {
      graphics.drawCenteredString(font, "Day Length", leftButtonX + BUTTON_WIDTH / 2,
                                  MENU_PADDING + (3 * (BUTTON_HEIGHT + BUTTON_PADDING)) - (font.lineHeight
                                      + BUTTON_PADDING), 16777215);
    }

    MutableComponent seasonCombined = CurrentSeason.getInstance(this.minecraft).getSeasonHudText();

    int componentWidth = this.font.width(seasonCombined);
    int componentHeight = this.font.lineHeight;
    int DEFAULT_X_OFFSET = Config.DEFAULT_X_OFFSET;
    int DEFAULT_Y_OFFSET = Config.DEFAULT_Y_OFFSET;
    int x = 0;
    int y = 0;

    switch (Config.getHudLocation()) {
      case TOP_LEFT:
        x = xSlider.getValueInt();
        y = ySlider.getValueInt();
        break;

      case TOP_CENTER:
        x = (width / 2) - (componentWidth / 2);
        y = DEFAULT_Y_OFFSET;
        break;

      case TOP_RIGHT:
        x = width - componentWidth - 2;
        y = DEFAULT_Y_OFFSET;
        break;

      case BOTTOM_LEFT:
        x = DEFAULT_X_OFFSET;
        y = height - componentHeight - DEFAULT_Y_OFFSET;
        break;

      case BOTTOM_RIGHT:
        x = width - componentWidth - DEFAULT_X_OFFSET;
        y = height - componentHeight - DEFAULT_Y_OFFSET;
        break;
    }

    graphics.pose().pushPose();
    graphics.pose().translate(0, 0, 50);
    graphics.drawString(font, seasonCombined, x, y, 0xffffff);
    graphics.pose().popPose();

    super.render(graphics, mouseX, mouseY, partialTicks);
  }

  @Override
  public void init() {
    super.init();

    MutableComponent seasonCombined = CurrentSeason.getInstance(this.minecraft).getSeasonHudText();
    double componentWidth = this.font.width(seasonCombined);
    double componentHeight = this.font.lineHeight;

    row = 0;
    hudLocationButton = CycleButton.builder(Location::getLocationName)
        .withTooltip(t -> Tooltip.create(Component.translatable("menu.seasonhud.season.hudLocation.tooltip")))
        .withValues(Location.TOP_LEFT, Location.TOP_CENTER, Location.TOP_RIGHT, Location.BOTTOM_LEFT,
                    Location.BOTTOM_RIGHT)
        .withInitialValue(hudLocation)
        .create(leftButtonX, (buttonStartY + (row * yOffset)), BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("menu.seasonhud.season.hudLocation.button"),
                (b, val) -> Config.setHudLocation(val));

    xSlider = HudOffsetSlider.builder(Component.translatable("menu.seasonhud.season.xOffset.slider"))
        .withTooltip(Tooltip.create(Component.translatable("menu.seasonhud.season.xOffset.tooltip")))
        .withValueRange(0, this.width - componentWidth)
        .withInitialValue(xSliderInt)
        .withDefaultValue(Config.DEFAULT_X_OFFSET)
        .withBounds(rightButtonX, (buttonStartY + (row * yOffset)), BUTTON_WIDTH / 2 - BasicSlider.SLIDER_PADDING,
                    BUTTON_HEIGHT)
        .build();

    ySlider = HudOffsetSlider.builder(Component.translatable("menu.seasonhud.season.yOffset.slider"))
        .withTooltip(Tooltip.create(Component.translatable("menu.seasonhud.season.yOffset.tooltip")))
        .withValueRange(0, this.height - componentHeight)
        .withInitialValue(ySliderInt)
        .withDefaultValue(Config.DEFAULT_Y_OFFSET)
        .withBounds(rightButtonX + BUTTON_WIDTH / 2 + BasicSlider.SLIDER_PADDING, (buttonStartY + (row * yOffset)),
                    BUTTON_WIDTH / 2 - BasicSlider.SLIDER_PADDING, BUTTON_HEIGHT)
        .build();

    row = 1;
    showDayButton = CycleButton.builder(ShowDay::getDayDisplayName)
        .withTooltip(t -> Tooltip.create(Component.translatable("menu.seasonhud.season.showDay.tooltip")))
        .withValues(ShowDay.getValues())
        .withInitialValue(showDay)
        .create(leftButtonX, (buttonStartY + (row * yOffset)), BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("menu.seasonhud.season.showDay.button"), (b, val) -> Config.setShowDay(val));

    CycleButton<Boolean> seasonColorButton = CycleButton.onOffBuilder(seasonColor)
        .withTooltip(t -> Tooltip.create(Component.translatable("menu.seasonhud.color.enableSeasonNameColor.tooltip")))
        .create(rightButtonX, (buttonStartY + (row * yOffset)), BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.translatable("menu.seasonhud.color.enableSeasonNameColor.button"),
                (b, val) -> Config.setEnableSeasonNameColor(val));

    widgets.addAll(Arrays.asList(hudLocationButton, xSlider, ySlider, showDayButton, seasonColorButton));

    if (Common.sereneSeasonsLoaded() || Common.terrafirmacraftLoaded()) {
      row = 2;
      showSubSeasonButton = CycleButton.onOffBuilder(showSubSeason)
          .withTooltip(t -> Tooltip.create(Component.translatable("menu.seasonhud.season.showSubSeason.tooltip")))
          .create(leftButtonX, (buttonStartY + (row * yOffset)), BUTTON_WIDTH, BUTTON_HEIGHT,
                  Component.translatable("menu.seasonhud.season.showSubSeason.button"),
                  (b, val) -> Config.setShowSubSeason(val));

      CycleButton<Boolean> showTropicalSeasonButton = CycleButton.onOffBuilder(showTropicalSeason)
          .withTooltip(t -> Tooltip.create(Component.translatable("menu.seasonhud.season.showTropicalSeason.tooltip")))
          .create(rightButtonX, (buttonStartY + (row * yOffset)), BUTTON_WIDTH, BUTTON_HEIGHT,
                  Component.translatable("menu.seasonhud.season.showTropicalSeason.button"),
                  (b, val) -> Config.setShowTropicalSeason(val));

      widgets.addAll(Arrays.asList(showSubSeasonButton, showTropicalSeasonButton));
    }
    if (Common.fabricSeasonsLoaded()) {
      row = 3;
      dayLengthBox = new EditBox(this.font, leftButtonX + 1, (buttonStartY + (row * yOffset)), BUTTON_WIDTH - 2,
                                 BUTTON_HEIGHT, Component.literal(String.valueOf(dayLength)));
      dayLengthBox.setMaxLength(10);
      dayLengthBox.setValue(String.valueOf(dayLength));
      dayLengthBox.setResponder((lengthString) -> {
        if (validate(lengthString)) {
          dayLengthBox.setTextColor(0xffffff);
          int currentLength = Integer.parseInt(lengthString);

          if (currentLength != this.newDayLength) {
            this.newDayLength = currentLength;
            dayLengthBox.setValue(lengthString);
          }

          doneButton.active = true;
        }
        else {
          dayLengthBox.setTextColor(16733525);
          doneButton.active = false;
        }
      });
      dayLengthBox.setHint(Component.literal("" + dayLength).withStyle(ChatFormatting.DARK_GRAY));
      widgets.add(dayLengthBox);
    }
    if (Common.extrasLoaded()) {
      row = 5;
      needCalendarButton = CycleButton.onOffBuilder(needCalendar)
          .withTooltip(t -> Tooltip.create(Component.translatable("menu.seasonhud.main.needCalendar.tooltip")))
          .create(leftButtonX, (buttonStartY + (row * yOffset)), BUTTON_WIDTH, BUTTON_HEIGHT,
                  Component.translatable("menu.seasonhud.main.needCalendar.button"), (b, val) -> needCalendar = val);

      calanderDetailModeButton = CycleButton.onOffBuilder(Config.getCalanderDetailMode())
          .withTooltip(t -> Tooltip.create(Component.translatable("menu.seasonhud.main.calendarDetail.tooltip")))
          .create(rightButtonX, (buttonStartY + (row * yOffset)), BUTTON_WIDTH, BUTTON_HEIGHT,
                  Component.translatable("menu.seasonhud.main.calendarDetail.button"), (b, val) -> {
                Config.setCalanderDetailMode(val);
                rebuildWidgets();
              });

      float scale = 0.45F;

      CheckButton showTotal = new CheckButton((rightButtonX + BUTTON_WIDTH + BUTTON_PADDING),
                                              (buttonStartY + (row * yOffset)), Component.literal("Show Total Days"),
                                              scale, (b) -> {
        if (b.selected()) {
          Config.setShowDay(ShowDay.SHOW_WITH_TOTAL_DAYS);
        }
        else {
          Config.setShowDay((ShowDay.SHOW_DAY));
        }
      }, Config.getShowDay() == ShowDay.SHOW_WITH_TOTAL_DAYS);

      CheckButton showSubSeason = new CheckButton((rightButtonX + BUTTON_WIDTH + BUTTON_PADDING),
                                                  (int) (buttonStartY + (row * yOffset) + BUTTON_HEIGHT - (20 * scale)),
                                                  Component.literal(String.valueOf(showTotal.selected())), scale,
                                                  (b) -> Config.setShowSubSeason(b.selected()),
                                                  Config.getShowSubSeason());
      widgets.addAll(Arrays.asList(needCalendarButton, calanderDetailModeButton));
    }

    widgets.forEach(this::addRenderableWidget);
  }

  private boolean inBounds(int length) {
    int minInt = 0;

    return length >= minInt;
  }

  public boolean validate(String length) {
    try {
      int dayLength = Integer.parseInt(length);
      return this.inBounds(dayLength);
    } catch (NumberFormatException formatException) {
      return false;
    }
  }
}

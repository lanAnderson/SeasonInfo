package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.Location;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


import static club.iananderson.seasonhud.config.Config.*;

public class SeasonHUDScreen extends Screen{
    //private static final int COLUMNS = 2;
    private static final int MENU_PADDING_FULL = 50;
    private static final int MENU_PADDING_HALF = MENU_PADDING_FULL/2;
    private static final int PADDING = 4;
    private static final int BUTTON_WIDTH_FULL = 200;
    private static final int BUTTON_WIDTH_HALF = 180;
    private static final int BUTTON_HEIGHT = 20;
    public static Screen seasonScreen;

    private final Screen lastScreen;

    private static final Component TITLE = Component.translatable("menu.seasonhud.title");
    private static final Component JOURNEYMAP = Component.translatable("menu.seasonhud.journeymap");


    public SeasonHUDScreen(Screen seasonScreen){
        super(TITLE);
        this.lastScreen = seasonScreen;
    }
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks){
        this.renderDirtBackground(stack);
        drawCenteredString(stack, font, TITLE, this.width / 2, PADDING, 16777215);
        drawCenteredString(stack, font, JOURNEYMAP, this.width / 2, MENU_PADDING_FULL + (5 * (BUTTON_HEIGHT + PADDING)), 16777215);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void init() {
        super.init();
        Minecraft mc = Minecraft.getInstance();

        int BUTTON_START_X_LEFT = (this.width / 2) - (BUTTON_WIDTH_HALF + PADDING);
        int BUTTON_START_X_RIGHT = (this.width / 2) + PADDING;
        int BUTTON_START_Y = MENU_PADDING_FULL;
        int y_OFFSET = BUTTON_HEIGHT + PADDING;

        Location defaultLocation = hudLocation.get();

        //Buttons

        int row = 0;
        CycleButton<Boolean> enableModButton = CycleButton.onOffBuilder(enableMod.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.enableMod"),
                        (b, Off) -> Config.setEnableMod(Off));

        CycleButton<Location> hudLocationButton = CycleButton.builder(Location::getLocationName)
                .withValues(Location.TOP_LEFT, Location.TOP_CENTER, Location.TOP_RIGHT, Location.BOTTOM_LEFT, Location.BOTTOM_RIGHT)
                .withInitialValue(defaultLocation)
                .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.hudLocation"),
                        (b, location) -> Config.setHudLocation(location));


        row = 1;
        CycleButton<Boolean> showDayButton = CycleButton.onOffBuilder(showDay.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.showDay"),
                        (b, Off) -> Config.setShowDay(Off));

        CycleButton<Boolean> needCalendarButton = CycleButton.onOffBuilder(needCalendar.get())
                .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.needCalendar"),
                        (b, Off) -> Config.setNeedCalendar(Off));

        row = 2;
        CycleButton<Boolean> showMinimapHiddenButton = CycleButton.onOffBuilder(showMinimapHidden.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.showMinimapHidden"),
                        (b, Off) -> Config.setShowMinimapHidden(Off));

        row = 6;
        CycleButton<Boolean> journeyMapAboveMapButton = CycleButton.onOffBuilder(journeyMapAboveMap.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        Component.translatable("menu.seasonhud.button.journeyMapAboveMap"),
                        (b, Off) -> Config.setJourneyMapAboveMap(Off));

        Button doneButton = Button.builder(Component.translatable("gui.done"), button -> {
            mc.options.save();
            mc.setScreen(this.lastScreen);
        })
                .bounds(this.width / 2 - (BUTTON_WIDTH_FULL / 2), (this.height - BUTTON_HEIGHT - PADDING),BUTTON_WIDTH_FULL, BUTTON_HEIGHT)
                .build();

        addRenderableWidget(enableModButton);
        addRenderableWidget(needCalendarButton);
        addRenderableWidget(showDayButton);
        addRenderableWidget(hudLocationButton);
        addRenderableWidget(showMinimapHiddenButton);
        addRenderableWidget(journeyMapAboveMapButton);


        addRenderableWidget(doneButton);
    }

    public static void open(){
        Minecraft.getInstance().setScreen(new SeasonHUDScreen(seasonScreen));
    }

}
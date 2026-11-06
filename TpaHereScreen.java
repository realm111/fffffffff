package pl.tpahere.auto;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

public class TpaHereScreen extends Screen {
    private static final int W = 260;
    private static final int H = 210;
    private static final int ACCENT = 0xFF55FFAA;

    private int left, top;
    private ButtonWidget toggleButton;

    public TpaHereScreen() {
        super(Text.literal("TPAHere Auto"));
    }

    @Override
    protected void init() {
        left = (width - W) / 2;
        top = (height - H) / 2;
        Config c = TpaHereClient.CONFIG;
        int x = left + 15;
        int w = W - 30;

        toggleButton = addDrawableChild(ButtonWidget.builder(statusText(), b -> {
            c.enabled = !c.enabled;
            b.setMessage(statusText());
            c.save();
        }).dimensions(x, top + 32, w, 20).build());

        addDrawableChild(new ConfigSlider(x, top + 58, w, 20, 5, 300, c.cooldownSeconds,
                v -> "Cooldown na gracza: " + (int) v + " s",
                v -> { c.cooldownSeconds = (int) v; c.save(); }));

        addDrawableChild(new ConfigSlider(x, top + 82, w, 20, 1, 10, c.delayTicks / 20.0,
                v -> "Odstep miedzy komendami: " + String.format("%.1f", v) + " s",
                v -> { c.delayTicks = (int) Math.round(v * 20); c.save(); }));

        TextFieldWidget field = new TextFieldWidget(textRenderer, x, top + 124, w, 20, Text.literal("Komenda"));
        field.setMaxLength(100);
        field.setText(c.command);
        field.setChangedListener(s -> { c.command = s; c.save(); });
        addDrawableChild(field);

        addDrawableChild(ButtonWidget.builder(Text.literal("Wyczysc historie"), b -> TpaHereClient.clearState())
                .dimensions(x, top + 152, w / 2 - 2, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij"), b -> close())
                .dimensions(x + w / 2 + 2, top + 152, w / 2 - 2, 20).build());
    }

    private Text statusText() {
        return TpaHereClient.CONFIG.enabled
                ? Text.literal("Status: WLACZONY").styled(s -> s.withColor(0x55FF55))
                : Text.literal("Status: WYLACZONY").styled(s -> s.withColor(0xFF5555));
    }

    // Panel rysowany w tle, zeby widgety byly na wierzchu
    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderBackground(ctx, mouseX, mouseY, delta);
        ctx.fill(left - 2, top - 2, left + W + 2, top + H + 2, ACCENT);
        ctx.fill(left, top, left + W, top + H, 0xF0101418);
        ctx.fill(left, top, left + W, top + 24, 0xFF1B2229);
        ctx.fill(left, top + 24, left + W, top + 25, ACCENT);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("TPAHere Auto"), width / 2, top + 8, ACCENT);
        ctx.drawTextWithShadow(textRenderer, "Komenda ({player} = nick):", left + 15, top + 112, 0xAAAAAA);
        ctx.drawTextWithShadow(textRenderer, "Wyslano komend: " + TpaHereClient.sentCount, left + 15, top + 180, 0xFFFFFF);
        ctx.drawTextWithShadow(textRenderer, "Klawisz K - otwiera to menu", left + 15, top + 193, 0x777777);
    }

    private static class ConfigSlider extends SliderWidget {
        private final double min, max;
        private final DoubleFunction<String> label;
        private final DoubleConsumer onChange;

        ConfigSlider(int x, int y, int w, int h, double min, double max, double current,
                     DoubleFunction<String> label, DoubleConsumer onChange) {
            super(x, y, w, h, Text.empty(), (current - min) / (max - min));
            this.min = min;
            this.max = max;
            this.label = label;
            this.onChange = onChange;
            updateMessage();
        }

        private double real() { return min + value * (max - min); }

        @Override
        protected void updateMessage() { setMessage(Text.literal(label.apply(real()))); }

        @Override
        protected void applyValue() { onChange.accept(real()); }
    }
}

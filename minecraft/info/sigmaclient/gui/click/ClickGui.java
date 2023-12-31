package info.sigmaclient.gui.click;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;
import info.sigmaclient.Client;
import info.sigmaclient.gui.click.components.MainPanel;
import info.sigmaclient.gui.click.ui.Sigma;
import info.sigmaclient.gui.click.ui.Menu;
import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.management.animate.Expand;
import info.sigmaclient.management.animate.Opacity;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.optifine.IFileDownloadListener;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClickGui extends GuiScreen {

    private MainPanel mainPanel;
    public static Menu menu;
    public Expand expand = new Expand(0, 0, 0, 0);

    public List<UI> getThemes() {
        return themes;
    }

    private List<UI> themes;

    public ClickGui() {
        (themes = new CopyOnWriteArrayList<>()).add(new Sigma());
        mainPanel = new MainPanel("Sigma", 50, 50, themes.get(0));
        themes.get(0).mainConstructor(this, mainPanel);
    }

    private Opacity opacity = new Opacity(0);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        opacity.interpolate(100);
        
        
//        RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0, (int) opacity.getOpacity()));
   
        mainPanel.draw(mouseX, mouseY);
        double width = 220;
        double height = 40;
        int c = Colors.getColor(0, 0, 0, 200);
        double x = res.getScaledWidth_double();
        double y = 0;
        int s = res.getScaleFactor();
        expand.interpolate((int)width + 10, (int)height*2, 1, 1);
        glPushMatrix();
        glScissor((int) (x - expand.getExpandX() / 2) * s, (int) (y - expand.getExpandY() / 2) * s, (int) (expand.getExpandX()) * s, (int) (expand.getExpandY()) * s);
        glEnable(GL_SCISSOR_TEST);
        

        int hovered = Colors.getColor(255,100,100,255);
        

        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        mainPanel.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        try {
            ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

            mainPanel.mouseClicked(mouseX, mouseY, clickedButton);
            super.mouseClicked(mouseX, mouseY, clickedButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        if (Keyboard.getEventKeyState()) {
            mainPanel.keyPressed(Keyboard.getEventKey());
        }
    }

    @Override
    public void onGuiClosed() {
        expand.setExpandX(0);
        expand.setExpandY(0);
        opacity.setOpacity(0);
        themes.get(0).onClose();
    }

}

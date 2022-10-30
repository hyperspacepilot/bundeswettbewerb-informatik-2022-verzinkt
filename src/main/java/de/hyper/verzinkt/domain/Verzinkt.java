package de.hyper.verzinkt.domain;

import de.hyper.verzinkt.common.GLKeyListener;
import de.hyper.verzinkt.common.GLWindow;
import lombok.Getter;

import static org.lwjgl.glfw.GLFW.*;

public class Verzinkt {

    @Getter
    private static Verzinkt instance;

    private GLWindow glWindow;

    public Verzinkt() {
        startASync(() -> {
            this.glWindow = new GLWindow("Verzinkt | 1.0.0-SNAPSHOT");
            this.glWindow.loop();
        });
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.glWindow.registerGLKeyListener(new GLKeyListener(this.glWindow.getWindowData(), GLFW_KEY_ESCAPE, GLFW_RELEASE) {
            @Override
            public boolean performAction() {
                glfwSetWindowShouldClose(getWindowData(), true);
                return true;
            }
        });
        instance = this;
    }

    public static void startASync(final Runnable run) {
        new Thread(() -> {
            try {
                run.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
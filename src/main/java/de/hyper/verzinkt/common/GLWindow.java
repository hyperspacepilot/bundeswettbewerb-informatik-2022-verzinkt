package de.hyper.verzinkt.common;

import lombok.Getter;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;

@Getter
public class GLWindow {

    private long windowData;
    private CopyOnWriteArrayList<GLKeyListener> glKeyListenerList;

    public GLWindow(String title) {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        windowData = glfwCreateWindow(300, 300, title, 0, 0);
        if (windowData == 0) {
            throw new RuntimeException("Failed to create GLWF window");
        }

        this.glKeyListenerList = new CopyOnWriteArrayList<>();
        glfwSetKeyCallback(windowData, (window, key, scancode, action, mods) -> {
            for (GLKeyListener glKeyListener : glKeyListenerList) {
                if (glKeyListener.isAccurate(windowData, key, action)) {
                    glKeyListener.performAction();
                }
            }
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(windowData, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    windowData,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
        glfwMakeContextCurrent(windowData);
        glfwSwapInterval(1);
        glfwShowWindow(windowData);
    }

    public void loop() {
        GL.createCapabilities();
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        while (!glfwWindowShouldClose(windowData)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(windowData);
            glfwPollEvents();
        }
    }

    public boolean registerGLKeyListener(GLKeyListener glKeyListener) {
        if (!this.glKeyListenerList.contains(glKeyListener)) {
            this.glKeyListenerList.add(glKeyListener);
            return true;
        }
        return false;
    }
}

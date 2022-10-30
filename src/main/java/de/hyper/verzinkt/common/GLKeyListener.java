package de.hyper.verzinkt.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public abstract class GLKeyListener {

    private final long windowData;
    private final long glKey;
    private final long glAction;

    public GLKeyListener(GLWindow glWindow, long glKey, long glAction) {
        this.windowData = glWindow.getWindowData();
        this.glKey = glKey;
        this.glAction = glAction;
    }

    public boolean isAccurate(long windowData, long glKey, long glAction) {
        return (this.windowData == windowData && this.glKey == glKey && this.glAction == glAction);
    }

    public abstract boolean performAction();
}
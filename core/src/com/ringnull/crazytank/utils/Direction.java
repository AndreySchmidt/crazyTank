package com.ringnull.crazytank.utils;

import org.omg.CORBA.PRIVATE_MEMBER;

public enum Direction {
    UP(0, 1, 90.0f), DOWN(0, -1, 270.0f), LEFT(-1, 0, 180.0f), RIGHT(1, 0, 0.0f);

    private int vx;
    private int vy;
    private float angle;

    public float getAngle() {
        return angle;
    }

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }

    Direction(int vx, int vy, float angle) {
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
    }
}

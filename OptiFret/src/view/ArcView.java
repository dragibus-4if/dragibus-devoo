/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Sylvain
 */
public class ArcView {

    private static final int STROKE = 2;
    private static final int ARR_SIZE = 6;
    private static final int NB_LINES_MAX = 5;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int nbLines;
    BasicStroke myStroke;

    public ArcView(int x1, int y1, int x2, int y2, int nbLines) {
        this.nbLines = nbLines;
        this.myStroke = new BasicStroke(STROKE);
        float vx = x2 - x1;
        float vy = y2 - y1;
        float norm = (float) Math.sqrt(vx * vx + vy * vy);
        vx /= norm;
        vy /= norm;

        this.x1 = (int) (x1 + NodeView.DIAMETER / 2 * vx);
        this.y1 = (int) (y1 + NodeView.DIAMETER / 2 * vy);
        this.x2 = (int) (x2 - NodeView.DIAMETER / 2 * vx);
        this.y2 = (int) (y2 - NodeView.DIAMETER / 2 * vy);
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof ArcView) {
            if (this.x1 == ((ArcView) object).x1 && this.x2 == ((ArcView) object).x2//
                    && this.y1 == ((ArcView) object).y1 && this.y2 == ((ArcView) object).y2) {
                sameSame = true;
            }
        }
        return sameSame;
    }

    public void incrementNbLines(){
        nbLines++;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.x1;
        hash = 41 * hash + this.y1;
        hash = 47 * hash + this.x2;
        hash = 41 * hash + this.y2;
        return hash;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(myStroke);
        drawArrow(g2d, x1, y1, x2, y2);
    }

    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        g.drawLine(0, 0, len, 0);
        if (nbLines > 0) {

            for (int i = 0; i < nbLines ; i++) {
                g.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
                        new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
                g.translate(-ARR_SIZE, 0);

            }
        }
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
}
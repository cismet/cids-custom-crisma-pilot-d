/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.worldstate;

import org.openide.util.ImageUtilities;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import de.cismet.cids.custom.crisma.worldstate.editor.NotEditableEditor;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class IconPanel extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    private final transient BufferedImage image;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form IconPanel.
     *
     * @param  imageResource  DOCUMENT ME!
     * @param  wImg           DOCUMENT ME!
     * @param  hImg           DOCUMENT ME!
     */
    public IconPanel(final String imageResource, final int wImg, final int hImg) {
        initComponents();

        final Image i = ImageUtilities.loadImage(imageResource);
        image = new BufferedImage(wImg, hImg, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = image.createGraphics();
        g2.drawImage(i, 0, 0, this);
        g2.dispose();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new BorderLayout());
    } // </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void paintComponent(final Graphics g) {
        final int iw = getSize().width * 2 / 3;
        final int ih = getSize().height * 2 / 3;

        final int scale = Math.min(iw, ih);

        final int h = (getSize().height - scale) / 2;
        final int w = (getSize().width - scale) / 2;
        g.drawImage(NotEditableEditor.getScaledInstance(
                image,
                scale,
                scale,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                true),
            w,
            h,
            this);
        g.dispose();
    }
}

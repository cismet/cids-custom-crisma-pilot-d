/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.model;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import org.geotools.geometry.jts.LiteShape;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.swingx.JXList;

import org.openide.util.NbBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.cismet.cids.custom.crisma.pilotD.model.AreaEditorWizardPanel.Area;
import de.cismet.cids.custom.crisma.pilotD.model.AreaEditorWizardPanel.AreaEditor;
import de.cismet.cids.custom.crisma.pilotD.worldstate.ShakemapView;
import de.cismet.cids.custom.crisma.pilotD.worldstate.ShakemapView.WFSRequestListener;

import de.cismet.cismap.commons.features.Feature;
import de.cismet.cismap.commons.features.FeatureCollectionEvent;
import de.cismet.cismap.commons.features.FeatureCollectionListener;
import de.cismet.cismap.commons.gui.MappingComponent;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class AreaEditorVisualPanel extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = LoggerFactory.getLogger(AreaEditorVisualPanel.class);

    //~ Instance fields --------------------------------------------------------

    private final transient AreaEditorWizardPanel model;
    private boolean init;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private de.cismet.cismap.commons.gui.MappingComponent mappingComponent1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ChooseEvacTimeVisualPanel.
     *
     * @param  model  DOCUMENT ME!
     */
    public AreaEditorVisualPanel(final AreaEditorWizardPanel model) {
        this.model = model;

        initComponents();

        setName(model.getStepName());
        jList1.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(final ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting() && (jList1.getSelectedValue() != null)) {
                        mappingComponent1.getFeatureCollection().select(((Area)jList1.getSelectedValue()).f);
                    }
                }
            });
        mappingComponent1.getFeatureCollection().addFeatureCollectionListener(new FeatureCollectionListener() {

                @Override
                public void featuresAdded(final FeatureCollectionEvent fce) {
                    for (final Feature feature : fce.getEventFeatures()) {
                        if (!init) {
                            final Area area = new Area(feature);
                            model.addArea(area);
                            ((DefaultListModel)jList1.getModel()).addElement(area);
                        }
                    }
                }

                @Override
                public void allFeaturesRemoved(final FeatureCollectionEvent fce) {
                }

                @Override
                public void featuresRemoved(final FeatureCollectionEvent fce) {
                }

                @Override
                public void featuresChanged(final FeatureCollectionEvent fce) {
                }

                @Override
                public void featureSelectionChanged(final FeatureCollectionEvent fce) {
                }

                @Override
                public void featureReconsiderationRequested(final FeatureCollectionEvent fce) {
                }

                @Override
                public void featureCollectionChanged() {
                }
            });
        mappingComponent1.setInteractionMode(MappingComponent.NEW_POLYGON);
        jList1.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        final Object[] o = jList1.getSelectedValues();
                        if (jList1.getSelectedValues().length == 1) {
                            final Area a = (Area)o[0];
                            EventQueue.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        doEdit(a);
                                    }
                                });

                            e.consume();
                        }
                    }
                }
            });
        jList1.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(final KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        final Object[] o = jList1.getSelectedValues();
                        if (jList1.getSelectedValues().length == 1) {
                            final Area a = (Area)o[0];
                            EventQueue.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        doEdit(a);
                                    }
                                });

                            e.consume();
                        }
                    }
                }
            });

        jList1.setCellRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList<?> arg0,
                        final Object arg1,
                        final int arg2,
                        final boolean arg3,
                        final boolean arg4) {
                    final JLabel l = (JLabel)super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4); // To change body of generated methods, choose Tools | Templates.
                    l.setText(((Area)arg1).name);

                    try {
                        final Geometry geom = ((Area)arg1).f.getGeometry();

                        final Envelope env = geom.getEnvelopeInternal();
                        final double scale = Math.min(16 / env.getWidth(), 16 / env.getHeight());
                        final double xoff = 0 - (scale * env.getMinX());
                        final double yoff = env.getMaxY() * scale;
                        final AffineTransform at = new AffineTransform(scale, 0, 0, -scale, xoff, yoff);

                        final LiteShape shape = new LiteShape(geom, at, false);

                        final int dw = (int)Math.ceil(geom.getEnvelopeInternal().getWidth() * scale);
                        final int dh = (int)Math.ceil(geom.getEnvelopeInternal().getHeight() * scale);

                        final BufferedImage biShape = new BufferedImage(dw, dh, BufferedImage.TYPE_INT_ARGB);
                        final Graphics2D g2dShape = (Graphics2D)biShape.getGraphics();

                        final Paint paint = new Color(153, 153, 255);
                        g2dShape.setPaint(paint);
                        g2dShape.fill(shape);

                        g2dShape.setPaint(Color.BLACK);
                        g2dShape.draw(shape);

                        final BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

                        final Graphics2D g2d = (Graphics2D)bi.getGraphics();
                        final int x = (bi.getWidth() - biShape.getWidth()) / 2;
                        final int y = (bi.getHeight() - biShape.getHeight()) / 2;
                        g2d.drawImage(biShape, x, y, null);

                        l.setIcon(new ImageIcon(bi));
                    } catch (final Exception e) {
// skip
                    }

                    return l;
                }
            });

        init = false;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  a  DOCUMENT ME!
     */
    private void doEdit(final Area a) {
        final AreaEditor ae = model.getAreaEditor();
        ae.setArea(a);
        final int answer = JOptionPane.showOptionDialog(
                this,
                ae,
                "Edit Area",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                null,
                null);
        if (JOptionPane.OK_OPTION == answer) {
            // do sth
        }
    }

    /**
     * f DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public AreaEditorWizardPanel getModel() {
        return model;
    }

    /**
     * DOCUMENT ME!
     */
    void init() {
        init = true;
        ((DefaultListModel)jList1.getModel()).clear();
        for (final Area a : model.getAreas()) {
            ((DefaultListModel)jList1.getModel()).addElement(a);
            mappingComponent1.getFeatureCollection().addFeature(a.f);
        }
        try {
            ShakemapView.initPilotDMap(
                mappingComponent1,
                "buildVulCl",
                model.getWorldstate(),
                0.8f,
                new WFSRequestListener());
            mappingComponent1.setInteractionMode(MappingComponent.NEW_POLYGON);
            mappingComponent1.setReadOnly(false);
            ShakemapView.activateLayerWidget(mappingComponent1);
        } catch (IOException ex) {
            LOG.error("error initialising", ex);
        }
        init = false;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new JXList(new DefaultListModel());
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        mappingComponent1 = new de.cismet.cismap.commons.gui.MappingComponent();
        jToolBar2 = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(AreaEditorVisualPanel.class, "AreaEditorVisualPanel.jPanel1.border.title"))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jList1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jList1.setModel(new DefaultListModel());
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setPreferredSize(new java.awt.Dimension(120, 85));
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jToolBar1.setRollover(true);

        jButton2.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/crisma/pilotD/model/edit_area_22.png")));           // NOI18N
        jButton2.setText(NbBundle.getMessage(AreaEditorVisualPanel.class, "AreaEditorVisualPanel.jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(38, 38));
        jButton2.setMinimumSize(new java.awt.Dimension(38, 38));
        jButton2.setPreferredSize(new java.awt.Dimension(38, 38));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton2);

        jButton1.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/crisma/pilotD/model/del_area_22.png")));            // NOI18N
        jButton1.setText(NbBundle.getMessage(AreaEditorVisualPanel.class, "AreaEditorVisualPanel.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(38, 38));
        jButton1.setMinimumSize(new java.awt.Dimension(38, 38));
        jButton1.setPreferredSize(new java.awt.Dimension(38, 38));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jToolBar1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(AreaEditorVisualPanel.class, "AreaEditorVisualPanel.jPanel2.border.title"))); // NOI18N
        jPanel2.setLayout(new java.awt.GridBagLayout());

        mappingComponent1.setBorder(javax.swing.BorderFactory.createBevelBorder(
                javax.swing.border.BevelBorder.LOWERED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(mappingComponent1, gridBagConstraints);

        jToolBar2.setRollover(true);

        buttonGroup1.add(jToggleButton1);
        jToggleButton1.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/crisma/pilotD/model/newPolygon.png"))); // NOI18N
        jToggleButton1.setSelected(true);
        jToggleButton1.setText(NbBundle.getMessage(
                AreaEditorVisualPanel.class,
                "AreaEditorVisualPanel.jToggleButton1.text"));                                         // NOI18N
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton1ActionPerformed(evt);
                }
            });
        jToolBar2.add(jToggleButton1);

        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/crisma/pilotD/model/zoom.gif"))); // NOI18N
        jToggleButton2.setText(NbBundle.getMessage(
                AreaEditorVisualPanel.class,
                "AreaEditorVisualPanel.jToggleButton2.text"));                                   // NOI18N
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton2ActionPerformed(evt);
                }
            });
        jToolBar2.add(jToggleButton2);

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/crisma/pilotD/model/pan.gif"))); // NOI18N
        jToggleButton3.setText(NbBundle.getMessage(
                AreaEditorVisualPanel.class,
                "AreaEditorVisualPanel.jToggleButton3.text"));                                  // NOI18N
        jToggleButton3.setFocusable(false);
        jToggleButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton3ActionPerformed(evt);
                }
            });
        jToolBar2.add(jToggleButton3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jToolBar2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_jButton1ActionPerformed
    {                                                                          //GEN-HEADEREND:event_jButton1ActionPerformed
        final Area a = (Area)jList1.getSelectedValue();
        mappingComponent1.getFeatureCollection().removeFeature(a.f);
        ((DefaultListModel)jList1.getModel()).removeElement(a);
    }                                                                          //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_jButton2ActionPerformed
    {                                                                          //GEN-HEADEREND:event_jButton2ActionPerformed
        final Area a = (Area)jList1.getSelectedValue();
        if (a != null) {
            doEdit(a);
        }
    }                                                                          //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton1ActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_jToggleButton1ActionPerformed
    {                                                                                //GEN-HEADEREND:event_jToggleButton1ActionPerformed
        mappingComponent1.setInteractionMode(MappingComponent.NEW_POLYGON);
    }                                                                                //GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton2ActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_jToggleButton2ActionPerformed
    {                                                                                //GEN-HEADEREND:event_jToggleButton2ActionPerformed
        mappingComponent1.setInteractionMode(MappingComponent.ZOOM);
    }                                                                                //GEN-LAST:event_jToggleButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton3ActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_jToggleButton3ActionPerformed
    {                                                                                //GEN-HEADEREND:event_jToggleButton3ActionPerformed
        mappingComponent1.setInteractionMode(MappingComponent.PAN);
    }                                                                                //GEN-LAST:event_jToggleButton3ActionPerformed

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static final class Conv extends Converter<Object, Object> {

        //~ Methods ------------------------------------------------------------

        @Override
        public Object convertForward(final Object value) {
            return Integer.valueOf(value.toString()) + 100;
        }

        @Override
        public Object convertReverse(final Object value) {
            return Integer.valueOf(value.toString()) - 100;
        }
    }
}

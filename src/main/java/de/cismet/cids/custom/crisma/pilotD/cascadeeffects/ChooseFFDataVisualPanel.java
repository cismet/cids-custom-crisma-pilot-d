/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.cascadeeffects;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import edu.umd.cs.piccolo.event.PInputEvent;

import org.jdesktop.beansbinding.Converter;

import org.openide.util.NbBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.EventQueue;

import java.io.IOException;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.cismet.cids.custom.crisma.pilotD.worldstate.ShakemapView;
import de.cismet.cids.custom.crisma.pilotD.worldstate.ShakemapView.WFSRequestListener;

import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.BackgroundRefreshingPanEventListener;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.RubberBandZoomListener;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWMS;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWmsGetMapUrl;
import de.cismet.cismap.commons.rasterservice.MapService;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class ChooseFFDataVisualPanel extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = LoggerFactory.getLogger(ChooseFFDataVisualPanel.class);

    //~ Instance fields --------------------------------------------------------

    private final transient ChooseFFDataWizardPanel model;
    private final transient DocL docL;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private de.cismet.cismap.commons.gui.MappingComponent mappingComponent1;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ChooseFFDataVisualPanel.
     *
     * @param  model  DOCUMENT ME!
     */
    public ChooseFFDataVisualPanel(final ChooseFFDataWizardPanel model) {
        this.model = model;
        this.docL = new DocL();

        initComponents();

        jTextField1.getDocument().addDocumentListener(docL);
        jTextField2.getDocument().addDocumentListener(docL);

        setName("Forest Fire: Electrical discharge ignition");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ChooseFFDataWizardPanel getModel() {
        return model;
    }

    /**
     * DOCUMENT ME!
     */
    void init() {
        jSpinner1.setValue(model.getWindSpeed());
        jSlider1.setValue(Math.round(model.getWindDirection()));
        try {
            initMap();
        } catch (final Exception e) {
            LOG.error("cannot initialise epi map", e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    private void initMap() throws IOException {
        // FIXME: proper map
        if (!EventQueue.isDispatchThread()) {
            LOG.error("not called in EDT");
        }
        ShakemapView.initPilotDMap(
            mappingComponent1,
            "comune_aq",
            model.getWorldstate(),
            1f,
            new WFSRequestListener());
        final SimpleWMS propLayer = new SimpleWMS(new SimpleWmsGetMapUrl(
                    "http://sudplan.cismet.de/geoserver251/crisma/wms?service=WMS&version=1.1.0&request=GetMap&layers=crisma:power_tower_cc_demo2&bbox=<cismap:boundingBox>&width=<cismap:width>&height=<cismap:height>&srs=EPSG:32633&format=image/png&transparent=true"));
        propLayer.setName("Ignition Probability");
        ((SimpleWMS)mappingComponent1.getMappingModel().getRasterServices().firstEntry().getValue()).setTranslucency(
            0.1f);
        ((SimpleWMS)mappingComponent1.getMappingModel().getRasterServices().firstEntry().getValue()).getPNode()
                .setTransparency(0.1f);
        ((SimpleWMS)mappingComponent1.getMappingModel().getRasterServices().firstEntry().getValue()).getPNode()
                .repaint();
        mappingComponent1.getMappingModel().addLayer(propLayer);
        mappingComponent1.addInputListener("eq", new AddFFIgnitionListener());
        mappingComponent1.setInteractionMode("eq");
        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    mappingComponent1.repaint();
                }
            });
        ShakemapView.activateLayerWidget(mappingComponent1);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jPanel1 = new javax.swing.JPanel();
        mappingComponent1 = new de.cismet.cismap.commons.gui.MappingComponent();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));

        setPreferredSize(new java.awt.Dimension(1024, 768));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(ChooseFFDataVisualPanel.class, "ChooseFFDataVisualPanel.jPanel1.border.title"))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(mappingComponent1, gridBagConstraints);

        jLabel5.setText(NbBundle.getMessage(ChooseFFDataVisualPanel.class, "ChooseFFDataVisualPanel.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jTextField1.setText(NbBundle.getMessage(
                ChooseFFDataVisualPanel.class,
                "ChooseFFDataVisualPanel.jTextField1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField1, gridBagConstraints);

        jTextField2.setText(NbBundle.getMessage(
                ChooseFFDataVisualPanel.class,
                "ChooseFFDataVisualPanel.jTextField2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(NbBundle.getMessage(ChooseFFDataVisualPanel.class, "ChooseFFDataVisualPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel2.setText(NbBundle.getMessage(ChooseFFDataVisualPanel.class, "ChooseFFDataVisualPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jLabel2, gridBagConstraints);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(
                Float.valueOf(0.0f),
                Float.valueOf(0.0f),
                null,
                Float.valueOf(1.0f)));
        jSpinner1.setPreferredSize(new java.awt.Dimension(82, 28));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${model.windSpeed}"),
                jSpinner1,
                org.jdesktop.beansbinding.BeanProperty.create("value"),
                "ws");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 5);
        jPanel2.add(jSpinner1, gridBagConstraints);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ,
                jSlider1,
                org.jdesktop.beansbinding.ELProperty.create("${value}"),
                jLabel3,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "mag_bind_text");
        binding.setConverter(new Conv());
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        jSlider1.setMajorTickSpacing(60);
        jSlider1.setMaximum(360);
        jSlider1.setMinorTickSpacing(30);
        jSlider1.setPaintTicks(true);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${model.magnitude}"),
                jSlider1,
                org.jdesktop.beansbinding.BeanProperty.create("value"),
                "mag_bind_float");
        binding.setConverter(new Conv2());
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jSlider1, gridBagConstraints);

        jLabel4.setText(NbBundle.getMessage(ChooseFFDataVisualPanel.class, "ChooseFFDataVisualPanel.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel6.setText(NbBundle.getMessage(ChooseFFDataVisualPanel.class, "ChooseFFDataVisualPanel.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel6, gridBagConstraints);

        jLabel7.setText(NbBundle.getMessage(ChooseFFDataVisualPanel.class, "ChooseFFDataVisualPanel.jLabel7.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel7, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stable", "Neutral", "Unstable" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jComboBox1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(filler1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        add(jPanel2, gridBagConstraints);

        bindingGroup.bind();
    } // </editor-fold>//GEN-END:initComponents

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class DocL implements DocumentListener {

        //~ Instance fields ----------------------------------------------------

        private final transient GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FIXED),
                32633);

        //~ Methods ------------------------------------------------------------

        @Override
        public void insertUpdate(final DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void removeUpdate(final DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void changedUpdate(final DocumentEvent e) {
            try {
                final double x = Double.parseDouble(jTextField1.getText());
                final double y = Double.parseDouble(jTextField2.getText());

                final Point p = factory.createPoint(new Coordinate(x, y));
                mappingComponent1.getFeatureCollection().removeAllFeatures();
                final IgnitionFeature feature = new IgnitionFeature();
                feature.setGeometry(p);
                mappingComponent1.getFeatureCollection().addFeature(feature);
                model.setSource(p);
            } catch (final Exception ex) {
                LOG.error("cannot set epicenter", ex);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class AddFFIgnitionListener extends BackgroundRefreshingPanEventListener {

        //~ Instance fields ----------------------------------------------------

        private final RubberBandZoomListener zoomDelegate;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new PanAndMousewheelZoomListener object.
         */
        public AddFFIgnitionListener() {
            zoomDelegate = new RubberBandZoomListener();
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void mouseWheelRotated(final PInputEvent pInputEvent) {
            zoomDelegate.mouseWheelRotated(pInputEvent);
        }

        @Override
        public void mouseClicked(final PInputEvent event) {
            final MappingComponent mc = (MappingComponent)event.getComponent();
            final double xCoord = mc.getWtst().getSourceX(event.getPosition().getX() - mc.getClip_offset_x());
            final double yCoord = mc.getWtst().getSourceY(event.getPosition().getY() - mc.getClip_offset_y());
            final GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FIXED), 32633);
            final Point p = factory.createPoint(new Coordinate(xCoord, yCoord));
            mc.getFeatureCollection().removeAllFeatures();
            final IgnitionFeature feature = new IgnitionFeature();
            feature.setGeometry(p);
            mc.getFeatureCollection().addFeature(feature);
            jTextField1.getDocument().removeDocumentListener(docL);
            jTextField2.getDocument().removeDocumentListener(docL);
            jTextField1.setText(String.valueOf(xCoord));
            jTextField2.setText(String.valueOf(yCoord));
            jTextField1.getDocument().addDocumentListener(docL);
            jTextField2.getDocument().addDocumentListener(docL);

            model.setSource(p);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class Conv extends Converter<Integer, String> {

        //~ Methods ------------------------------------------------------------

        @Override
        public String convertForward(final Integer value) {
            return String.valueOf(value);
        }

        @Override
        public Integer convertReverse(final String value) {
            throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods,
                                                                           // choose Tools | Templates.
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class Conv2 extends Converter<Float, Integer> {

        //~ Methods ------------------------------------------------------------

        @Override
        public Integer convertForward(final Float value) {
            if (value == null) {
                return 0;
            }
            return Math.round(value * 10);
        }

        @Override
        public Float convertReverse(final Integer value) {
            if (value == null) {
                return 0f;
            }
            return value / 10f;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class Conv3 extends Converter<Integer, Object> {

        //~ Methods ------------------------------------------------------------

        @Override
        public Object convertForward(final Integer value) {
            return value;
        }

        @Override
        public Integer convertReverse(final Object value) {
            try {
                return Integer.parseInt(value.toString());
            } catch (final Exception e) {
                return 0;
            }
        }
    }
}

/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import edu.umd.cs.piccolo.event.PInputEvent;

import org.jdesktop.beansbinding.Converter;

import org.openide.util.NbBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.Map;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cismap.commons.Crs;
import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.BackgroundRefreshingPanEventListener;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.RubberBandZoomListener;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWMS;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWmsGetMapUrl;

import static de.cismet.cids.custom.crisma.pilotD.worldstate.ShakemapView.getOrthoLayer;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class ChooseEQDataVisualPanel extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = LoggerFactory.getLogger(ChooseEQDataVisualPanel.class);

    //~ Instance fields --------------------------------------------------------

    private final transient ChooseEQDataWizardPanel model;
    private final transient DocL docL;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
     * Creates new form ChooseEQDataVisualPanel.
     *
     * @param  model  DOCUMENT ME!
     */
    public ChooseEQDataVisualPanel(final ChooseEQDataWizardPanel model) {
        this.model = model;
        this.docL = new DocL();

        initComponents();

        jTextField1.getDocument().addDocumentListener(docL);
        jTextField2.getDocument().addDocumentListener(docL);

        setName("Building Hazard [Model]: Choose EQ Data");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ChooseEQDataWizardPanel getModel() {
        return model;
    }

    /**
     * DOCUMENT ME!
     */
    void init() {
        jSpinner1.setValue(model.getDepth());
        jSlider1.setValue(Math.round(model.getMagnitude() * 10));
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
        for (final CidsBean b : model.getWorldstate().getBeanCollectionProperty("worldstatedata")) {
            if ("boundaries".equals(b.getProperty("name"))) {
                final Geometry g = (Geometry)b.getProperty("spatialcoverage.geo_field");
                g.setSRID(4326);
                final Geometry gt = CrsTransformer.transformToGivenCrs(g, "EPSG:32633");
                final XBoundingBox bbox = new XBoundingBox(gt);
                final ActiveLayerModel mappingModel = new ActiveLayerModel();
                mappingModel.setSrs(new Crs("EPSG:32633", "EPSG:32633", "EPSG:32633", true, true));
                mappingModel.addHome(bbox);

                final ObjectMapper m = new ObjectMapper(new JsonFactory());
                final TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>() {
                    };

                final SimpleWMS ortho = getOrthoLayer(model.getWorldstate());
                if (ortho != null) {
                    mappingModel.addLayer(ortho);
                }

                final String jsonString = (String)b.getProperty("actualaccessinfo");
                final Map<String, String> json = m.readValue(jsonString, ref);
                final SimpleWMS layer = new SimpleWMS(new SimpleWmsGetMapUrl(
                            "http://crisma.cismet.de/geoserver/crisma/wms?service=WMS&version=1.1.0&request=GetMap&layers="
                                    + json.get("layername")
                                    + "&bbox=<cismap:boundingBox>&width=<cismap:width>&height=<cismap:height>&srs=EPSG:32633&format=image/png&transparent=true"));
                mappingModel.addLayer(layer);

                mappingComponent1.setMappingModel(mappingModel);
                mappingComponent1.addInputListener("epi", new AddEpicenterListener());
                mappingComponent1.setInteractionMode("epi");
                mappingComponent1.unlock();
                final Point p = model.getEpicenter();
                mappingComponent1.getFeatureCollection().removeAllFeatures();
                if (p == null) {
                    mappingComponent1.gotoInitialBoundingBox();
                } else {
                    final EpicenterFeature feature = new EpicenterFeature();
                    feature.setGeometry(p);
                    mappingComponent1.getFeatureCollection().addFeature(feature);
                    mappingComponent1.zoomToFeatureCollection(true);
                }
            }
        }
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

        setPreferredSize(new java.awt.Dimension(1024, 768));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(ChooseEQDataVisualPanel.class, "ChooseEQDataVisualPanel.jPanel1.border.title"))); // NOI18N
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

        jLabel5.setText(NbBundle.getMessage(ChooseEQDataVisualPanel.class, "ChooseEQDataVisualPanel.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel1.add(jLabel5, gridBagConstraints);

        jTextField1.setText(NbBundle.getMessage(
                ChooseEQDataVisualPanel.class,
                "ChooseEQDataVisualPanel.jTextField1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField1, gridBagConstraints);

        jTextField2.setText(NbBundle.getMessage(
                ChooseEQDataVisualPanel.class,
                "ChooseEQDataVisualPanel.jTextField2.text")); // NOI18N
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

        jLabel1.setText(NbBundle.getMessage(ChooseEQDataVisualPanel.class, "ChooseEQDataVisualPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel2.setText(NbBundle.getMessage(ChooseEQDataVisualPanel.class, "ChooseEQDataVisualPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jLabel2, gridBagConstraints);

        jSpinner1.setPreferredSize(new java.awt.Dimension(82, 28));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${model.depth}"),
                jSpinner1,
                org.jdesktop.beansbinding.BeanProperty.create("value"),
                "");
        binding.setConverter(new Conv3());
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        jSlider1.setMajorTickSpacing(10);
        jSlider1.setMaximum(120);
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jSlider1, gridBagConstraints);

        jLabel4.setText(NbBundle.getMessage(ChooseEQDataVisualPanel.class, "ChooseEQDataVisualPanel.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel6.setText(NbBundle.getMessage(ChooseEQDataVisualPanel.class, "ChooseEQDataVisualPanel.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabel6, gridBagConstraints);

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
                final EpicenterFeature feature = new EpicenterFeature();
                feature.setGeometry(p);
                mappingComponent1.getFeatureCollection().addFeature(feature);
                model.setEpicenter(p);
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
    private final class AddEpicenterListener extends BackgroundRefreshingPanEventListener {

        //~ Instance fields ----------------------------------------------------

        private final RubberBandZoomListener zoomDelegate;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new PanAndMousewheelZoomListener object.
         */
        public AddEpicenterListener() {
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
            final EpicenterFeature feature = new EpicenterFeature();
            feature.setGeometry(p);
            mc.getFeatureCollection().addFeature(feature);
            jTextField1.getDocument().removeDocumentListener(docL);
            jTextField2.getDocument().removeDocumentListener(docL);
            jTextField1.setText(String.valueOf(xCoord));
            jTextField2.setText(String.valueOf(yCoord));
            jTextField1.getDocument().addDocumentListener(docL);
            jTextField2.getDocument().addDocumentListener(docL);

            model.setEpicenter(p);
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
            return String.valueOf(value / 10.0);
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

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

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.data.BufferWrapperRaster;
import gov.nasa.worldwind.data.DataRaster;
import gov.nasa.worldwind.data.DataRasterReader;
import gov.nasa.worldwind.data.DataRasterReaderFactory;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.formats.shapefile.Shapefile;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecordPoint;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Matrix;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Cone;
import gov.nasa.worldwind.render.Cylinder;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.BufferWrapper;
import gov.nasa.worldwind.util.WWBufferUtil;
import gov.nasa.worldwind.util.WWXML;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface.GridPointAttributes;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceAttributes;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceLegend;
import gov.nasa.worldwindx.examples.util.ExampleUtil;
import gov.nasa.worldwindx.examples.util.LayerManagerLayer;

import org.jdesktop.beansbinding.Converter;

import org.openide.util.NbBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.Format;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.cismet.cids.custom.crisma.pilotD.worldstate.FuelMapView;

import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.BackgroundRefreshingPanEventListener;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.RubberBandZoomListener;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

import static de.cismet.cids.custom.crisma.pilotD.worldstate.FuelMapView.HUE_BLUE;
import static de.cismet.cids.custom.crisma.pilotD.worldstate.FuelMapView.HUE_RED;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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

    private Sector demSector;

    private Cone windDirCone;
    private RenderableLayer featureLayer;
    private WorldWindowGLCanvas c;
    private PointPlacemark ignitionPP;

    private final Object lock = new Object();

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
    private javax.swing.JPanel pnl3d;
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
        try
        {
            this.texture = ImageIO.read(getClass().getResourceAsStream("/" + getClass().getPackage().getName().replaceAll("\\.", "/") + "/smoke_texture.jpeg"));
        }catch(final IOException e)
        {
            LOG.warn("cannot load smoke texture", e);
            this.texture = null;
        }

        initComponents();

//        jTextField1.getDocument().addDocumentListener(docL);
//        jTextField2.getDocument().addDocumentListener(docL);

        setName("Forest Fire: Electrical discharge ignition");
    }

    private BufferedImage texture;
    
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
            jSlider1.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(final ChangeEvent e) {
                        if (!jSlider1.getValueIsAdjusting()) {
                            createSmokeCone();
                        }
                    }
                });
            jSpinner1.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(final ChangeEvent e) {
                        createSmokeCone();
                    }
                });
        } catch (final Exception e) {
            LOG.error("cannot initialise epi map", e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void createSmokeCone() {
        if (ignitionPP != null) {
            final int direction = jSlider1.getValue();
            final int speed = ((Float)jSpinner1.getValue()).intValue();
            final int vradius = (speed == 0) ? 400 : (speed * 800);
            final Position pos;

            synchronized (lock) {
                pos = ignitionPP.getPosition();
            }

            if (windDirCone != null) {
                featureLayer.removeRenderable(windDirCone);
            }

            final Globe globe = c.getModel().getGlobe();
            final Vec4 posVec = globe.computePointFromPosition(pos);
            final double angle = Math.toRadians(-direction);
            final Vec4 normPos = posVec.normalize3();
            final double cos = Math.cos(angle);
            final double cos1 = 1 - cos;
            final double sin = Math.sin(angle);
            final double x = normPos.x;
            final double y = normPos.y;
            final double z = normPos.z;

            //J-
            final Matrix directRotate = new Matrix(
                    x * x * cos1 + cos    , x * y * cos1 - z * sin, x * z * cos1 + y * sin, 0,
                    y * x * cos1 + z * sin, y * y * cos1 + cos    , y * z * cos1 - x * sin, 0,
                    z * x * cos1 - y * sin, z * y * cos1 + x * sin, z * z * cos1 + cos    , 0,
                    0                     , 0                     , 0                     , 1
            );

            final Vec4 normNorthVec = new Vec4(
                    -posVec.x,
                    posVec.getLength3() / Math.cos(posVec.y) - posVec.y,
                    -posVec.z
            ).normalize3();
            //J+
            final Vec4 windVec = normNorthVec.transformBy3(directRotate).multiply3(vradius);
            final Position pos5 = globe.computePositionFromPoint(posVec.add3(windVec));
            final Position pos6 = new Position(pos5.latitude, pos5.longitude, pos.elevation);

            Vec4 distance = globe.computePointFromPosition(pos6).subtract3(posVec);
            if (distance.getLength3() != vradius) {
                distance = distance.normalize3().multiply3(vradius);
            }

            System.out.println(posVec);
            System.out.println(globe.computePointFromPosition(pos5));
            System.out.println(globe.computePointFromPosition(pos6));
            System.out.println(globe.computePointFromPosition(pos6).subtract3(posVec).getLength3());
            System.out.println(distance.getLength3());
            System.out.println(vradius);
            final Position pos7 = globe.computePositionFromPoint(posVec.add3(distance));
            final Vec4 posVec7 = globe.computePointFromPosition(pos7);
            final Vec4 posVec7_2 = posVec.add3(distance);
            System.out.println(posVec7);
            System.out.println(posVec7_2);
            System.out.println(posVec7.subtract3(posVec7_2));

            windDirCone = new Cone(
                    globe.computePositionFromPoint(posVec.add3(distance)),
                    1000,
                    vradius,
                    2000,
                    Angle.fromDegrees(90),
                    Angle.fromDegrees(direction - 90), // wind direction angle
                    Angle.fromDegrees(90));
            windDirCone.setAltitudeMode(WorldWind.ABSOLUTE);
            windDirCone.setImageSources(texture);
            final BasicShapeAttributes attrs = new BasicShapeAttributes();
            attrs.setDrawOutline(false);
            attrs.setDrawInterior(true);
            windDirCone.setAttributes(attrs);
            featureLayer.addRenderable(windDirCone);
            c.redrawNow();
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

        c = new WorldWindowGLCanvas();

        // Create the default model as described in the current worldwind properties.
        final Model m = (Model)WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        c.setModel(m);
        final ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        // Insert the layer into the layer list just before the compass.
        int pos = 0;
        LayerList layers = c.getModel().getLayers();
        for (final Layer l : layers) {
            if (l instanceof CompassLayer) {
                pos = layers.indexOf(l);
            }
        }
        layers.add(pos, viewControlsLayer);

        final ArrayList<Layer> toRemove = new ArrayList<Layer>();
        final ListIterator<Layer> li = layers.listIterator();
        while (li.hasNext()) {
            final Layer l = li.next();
            if ((l instanceof WorldMapLayer)
                        || l.getName().contains("USDA")
                        || l.getName().contains("USGS")
                        || l.getName().equalsIgnoreCase("MS Virtual Earth Aerial")
                        || l.getName().equalsIgnoreCase("Bing Imagery")
                        || l.getName().equalsIgnoreCase("Political Boundaries")
                        || l.getName().equalsIgnoreCase("Open Street Map")
                        || l.getName().equalsIgnoreCase("Earth at Night")) {
                toRemove.add(l);
            }
        }
        for (final Layer l : toRemove) {
            layers.remove(l);
        }

        final LayerManagerLayer lml = new LayerManagerLayer(c);
        lml.setPosition(AVKey.NORTHWEST);
        lml.setEnabled(false);
        // Insert the layer into the layer list just before the compass.
        pos = 0;
        layers = c.getModel().getLayers();
        for (final Layer l : layers) {
            if (l instanceof CompassLayer) {
                pos = layers.indexOf(l);
            }
        }
        layers.add(pos, lml);

        featureLayer = new RenderableLayer();
        featureLayer.setName("Features");
        c.getInputHandler().addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    final Position p = c.getCurrentPosition();
                    if (p != null) {
//                    e.consume();
                        synchronized (lock) {
                            ignitionPP = new PointPlacemark(p);
                            final PointPlacemarkAttributes attr = new PointPlacemarkAttributes();
                            attr.setImageAddress("de/cismet/cids/custom/crisma/pilotD/cascadeeffects/fire_32.png");
                            attr.setImageOffset(Offset.BOTTOM_CENTER);
                            ignitionPP.setAttributes(attr);
                        }
                        featureLayer.removeAllRenderables();
                        featureLayer.addRenderable(ignitionPP);
                        jTextField1.setText(p.getLatitude().toDMString());
                        jTextField2.setText(p.getLongitude().toDMString());

                        createSmokeCone();
                    }
                }
            });
        // Insert the layer into the layer list just before the compass.
        pos = 0;
        layers = c.getModel().getLayers();
        for (final Layer l : layers) {
            if (l instanceof CompassLayer) {
                pos = layers.indexOf(l);
            }
        }
        layers.add(pos, featureLayer);

        c.addSelectListener(new ViewControlsSelectListener(c, viewControlsLayer));

        c.addRenderingExceptionListener(new RenderingExceptionListener() {

                @Override
                public void exceptionThrown(final Throwable thrwbl) {
                    LOG.error("rendering exception", thrwbl);
                }
            });

        try {
            final Document d = FuelMapView.addElevation(
                    c,
                    new File("/Users/mscholl/projects/crisma/SP5/WP55/layers/dtm_and_fuel.rar_folder/dem_wgs84.asc"));

            // Set the view to look at the imported elevations, although they might be hard to detect. To
            // make them easier to detect, replace the globe's CompoundElevationModel with the new elevation
            // model rather than adding it.
            demSector = WWXML.getSector(d.getDocumentElement(), "Sector", null);
        } catch (final Exception ex) {
            LOG.error("cannot add elevation model", ex);
        }

        final Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // ugly winning :)
                        FuelMapView.added = false;
                        FuelMapView.addLayer(
                            c,
                            "http://geoportale2.regione.abruzzo.it/ecwp/ecw_wms.dll",
                            "IMAGES_ORTO_AQUILA_2010.ECW");
                        for (;;) {
                            if (FuelMapView.added) {
                                break;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                // noop
                            }
                        }
                        FuelMapView.added = false;
                        FuelMapView.addLayer(c, "http://crisma.cismet.de/geoserver/ows", "crisma:planet_osm_line");
                        for (;;) {
                            if (FuelMapView.added) {
                                break;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                // noop
                            }
                        }

                        FuelMapView.added = false;
                        FuelMapView.addLayer(
                            c,
                            "http://crisma.cismet.de/geoserver251/ows",
                            "crisma:CRISMA_PilotD_FuelMap",
                            "fuelmap_style");
                        for (;;) {
                            if (FuelMapView.added) {
                                break;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                // noop
                            }
                        }

                        FuelMapView.added = false;
                        FuelMapView.addLayer(
                            c,
                            "http://crisma.cismet.de/geoserver/ows",
                            "crisma:comune_aq",
                            "pilot_d_boundaries_alt");
                        for (;;) {
                            if (FuelMapView.added) {
                                break;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                // noop
                            }
                        }
//                        addEq(c);
                        try {
                            addEq(
                                c,
                                new File(
                                    "/Users/mscholl/projects/crisma/SP5/WP55/layers/Intensity_MainEvent_NEzone_Mw5.6/Shakemap_MainEvent_300px.tif"));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }

                        addProb(c);
                    }
                });
        t.setPriority(Thread.MIN_PRIORITY);
//        t.start();

        pnl3d.add(c, BorderLayout.CENTER);
        final Thread t2 = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.currentThread().sleep(1000);
                        } catch (final Exception e) {
                            // noop
                        }
                        EventQueue.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    ExampleUtil.goTo(c, demSector);
                                }
                            });
                    }
                });
//        t2.start();
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
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        pnl3d = new javax.swing.JPanel();
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

        jLabel5.setText(NbBundle.getMessage(ChooseFFDataVisualPanel.class, "ChooseFFDataVisualPanel.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jTextField1.setEditable(false);
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

        jTextField2.setEditable(false);
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

        pnl3d.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(pnl3d, gridBagConstraints);

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
                org.jdesktop.beansbinding.ELProperty.create("${model.windDirection}"),
                jSlider1,
                org.jdesktop.beansbinding.BeanProperty.create("value"),
                "mag_bind_float");
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

    /**
     * DOCUMENT ME!
     *
     * @param   intensity  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Material getMaterial(final double intensity) {
        if (intensity < 2.0) {
            return new Material(new Color(255, 255, 255, 0));
        } else if (intensity < 3.0) {
            return new Material(new Color(Integer.decode("0xBFCCFF")));
        } else if (intensity < 4.0) {
            return new Material(new Color(Integer.decode("0x9999FF")));
        } else if (intensity < 5.0) {
            return new Material(new Color(Integer.decode("0x88FFFF")));
        } else if (intensity < 6.0) {
            return new Material(new Color(Integer.decode("0x7DF894")));
        } else if (intensity < 7.0) {
            return new Material(new Color(Integer.decode("0xFFFF00")));
        } else if (intensity < 8.0) {
            return new Material(new Color(Integer.decode("0xFFDD00")));
        } else if (intensity < 9.0) {
            return new Material(new Color(Integer.decode("0xFF9100")));
        } else if (intensity < 10.0) {
            return new Material(new Color(Integer.decode("0xFF0000")));
        } else if (intensity < 11.0) {
            return new Material(new Color(Integer.decode("0xDD0000")));
        } else if (intensity < 12.0) {
            return new Material(new Color(Integer.decode("0x880000")));
        } else {
            return new Material(new Color(Integer.decode("0x440000")));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ww    DOCUMENT ME!
     * @param   file  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void addEq(final WorldWindow ww, final File file) throws Exception {
        final RenderableLayer surfaceLayer = new RenderableLayer();
        surfaceLayer.setPickEnabled(false);
        surfaceLayer.setName("Earthquake");
        // Insert the layer into the layer list just before the placenames.
        int pos = 0;
        final LayerList layers = ww.getModel().getLayers();
        for (final Layer l : layers) {
            if (l instanceof PlaceNameLayer) {
                pos = layers.indexOf(l);
            }
        }
        final int insert = pos;
        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    layers.add(insert, surfaceLayer);
                }
            });

//            createRandomAltitudeSurface(HUE_BLUE, HUE_RED, 40, 40, surfaceLayer);
//            createRandomColorSurface(HUE_BLUE, HUE_RED, 40, 40, surfaceLayer);
        final DataRasterReaderFactory readerFactory = (DataRasterReaderFactory)WorldWind.createConfigurationComponent(
                AVKey.DATA_RASTER_READER_FACTORY_CLASS_NAME);
        final DataRasterReader reader = readerFactory.findReaderFor(file, null);
        final DataRaster[] rasters = reader.read(file, null);
        final Sector sector = (Sector)rasters[0].getValue(AVKey.SECTOR);
        final int width = rasters[0].getWidth();
        final int height = rasters[0].getHeight();

        final BufferWrapperRaster raster = (BufferWrapperRaster)rasters[0].getSubRaster(
                width,
                height,
                sector,
                rasters[0]);
        final double[] extremes = WWBufferUtil.computeExtremeValues(raster.getBuffer(), raster.getTransparentValue());
        final AnalyticSurface surface = new AnalyticSurface();
        surface.setSector(raster.getSector());
        surface.setDimensions(raster.getWidth(), raster.getHeight());
//        surface.setValues(AnalyticSurface.createColorGradientValues(raster.getBuffer(), -9999, 0, 13, HUE_BLUE, HUE_RED));
        surface.setValues(createMMIValues(raster.getBuffer()));
        surface.setVerticalScale(0);
        surface.setAltitude(3000);

        final AnalyticSurfaceAttributes attr = new AnalyticSurfaceAttributes();
        attr.setDrawOutline(false);
        attr.setDrawShadow(false);
//        attr.setInteriorOpacity(0.6);
        attr.setInteriorOpacity(1);
        surface.setSurfaceAttributes(attr);

        final Format legendLabelFormat = new DecimalFormat("MMI");

        final AnalyticSurfaceLegend legend = AnalyticSurfaceLegend.fromColorGradient(
                extremes[0],
                extremes[1],
                HUE_BLUE,
                HUE_RED,
                AnalyticSurfaceLegend.createDefaultColorGradientLabels(extremes[0], extremes[1], legendLabelFormat),
                AnalyticSurfaceLegend.createDefaultTitle("Intensity"));
        legend.setOpacity(0.8);
        legend.setScreenLocation(new java.awt.Point(100, 300));

        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    surface.setClientLayer(surfaceLayer);
                    surfaceLayer.addRenderable(surface);
//                    surfaceLayer.addRenderable(new Renderable() {
//
//                            @Override
//                            public void render(final DrawContext dc) {
//                                final Extent extent = surface.getExtent(dc);
//                                if (!extent.intersects(dc.getView().getFrustumInModelCoordinates())) {
//                                    return;
//                                }
//
//                                if (WWMath.computeSizeInWindowCoordinates(dc, extent) < 300) {
//                                    return;
//                                }
//
//                                legend.render(dc);
//                            }
//                        });
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param   values  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Iterable<? extends AnalyticSurface.GridPointAttributes> createMMIValues(final BufferWrapper values) {
        final ArrayList<GridPointAttributes> l = new ArrayList<GridPointAttributes>();
        for (int i = 0; i < values.length(); i++) {
            final double value = values.getDouble(i);
            final Material m = getMaterial(value);
            final GPA gpa = new GPA(value, m.getDiffuse());

            l.add(gpa);
        }

        return l;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ww  DOCUMENT ME!
     */
    private void addProb(final WorldWindow ww) {
//        final AirspaceLayer eqLayer = new AirspaceLayer();
//        eqLayer.setName("Earthquake");
//        final Shapefile eqFile = new Shapefile(new File(
//                    "/Users/mscholl/projects/crisma/SP5/WP55/layers/Intensity_MainEvent_NEzone_Mw5.6/Shakemap_MainEvent.shp"));
//        final Polygon pol = new Polygon();
//
//        final GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
//        final Map<Material, ArrayList<Geometry>> mp = new HashMap<Material, ArrayList<Geometry>>(14);
//
//        while (eqFile.hasNext()) {
//            final ShapefileRecord record = eqFile.nextRecord();
//            final double[] bbox = ((ShapefileRecordPolygon)record).getBoundingRectangle();
//            final Sector sector = Sector.fromDegrees(bbox);
//            final Coordinate[] coords = new Coordinate[5];
//            final List<LatLon> llList = sector.asList();
//            for (int i = 0; i < llList.size(); ++i) {
//                final LatLon ll = llList.get(i);
//                final Coordinate coord = new Coordinate(ll.longitude.degrees, ll.latitude.degrees);
//                coords[i] = coord;
//            }
//            coords[4] = new Coordinate(llList.get(0).longitude.degrees, llList.get(0).latitude.degrees);
//            final com.vividsolutions.jts.geom.Polygon p = new com.vividsolutions.jts.geom.Polygon(new LinearRing(
//                        new CoordinateArraySequence(coords),
//                        gf),
//                    null,
//                    gf);
//
//            Material mat = null;
//            for (final Entry<String, Object> entry : record.getAttributes().getEntries()) {
//                if (entry.getKey().equalsIgnoreCase("intensity")) {
//                    final Double intensity = (Double)entry.getValue();
//                    mat = getMaterial(intensity);
//                }
//            }
//
//            ArrayList<Geometry> coll = mp.get(mat);
//            if (coll == null) {
//                coll = new ArrayList<Geometry>();
//                mp.put(mat, coll);
//            }
//            coll.add(p);
//        }
//
//        for (final Entry<Material, ArrayList<Geometry>> entry : mp.entrySet()) {
//            final BasicAirspaceAttributes baa = new BasicAirspaceAttributes();
//            baa.setDrawInterior(true);
//            baa.setDrawOutline(true);
//            baa.setMaterial(entry.getKey());
//            baa.setOutlineMaterial(entry.getKey());
//            baa.setOutlineWidth(1d);
//            final ArrayList<Geometry> list = entry.getValue();
//            final GeometryCollection g = new GeometryCollection(list.toArray(new Geometry[list.size()]), gf);
//            final Geometry geom = g.buffer(0);
//
//            final Iterable<? extends LatLon> it = new Iterable<LatLon>() {
//
//                    final Coordinate[] coords = geom.getCoordinates();
//
//                    @Override
//                    public Iterator<LatLon> iterator() {
//                        return new Iterator<LatLon>() {
//
//                                int index = 0;
//
//                                @Override
//                                public boolean hasNext() {
//                                    return index < (coords.length - 1);
//                                }
//
//                                @Override
//                                public LatLon next() {
//                                    index++;
//                                    return new LatLon(Angle.fromDegreesLatitude(coords[index].y),
//                                            Angle.fromDegreesLongitude(coords[index].x));
//                                }
//
//                                @Override
//                                public void remove() {
//                                    throw new UnsupportedOperationException("Not supported yet."); // To change body of
//                                                                                                   // generated methods,
//                                                                                                   // choose Tools |
//                                                                                                   // Templates.
//                                }
//                            };
//                    }
//                };
//
//            final Polygon p = new Polygon(it);
//            p.setAttributes(pol.getAttributes());
//            p.setAltitude(3000);
//
//            eqLayer.addAirspace(p);
//        }

        final RenderableLayer poleLayer = new RenderableLayer();
        poleLayer.setName("Poles");
        final Shapefile poleFile = new Shapefile(new File(
                    "/Users/mscholl/projects/crisma/SP5/WP55/layers/elec_poles_prob_shape.rar_folder/ElecPoles_probability.shp"));
        while (poleFile.hasNext()) {
            final ShapefileRecord record = poleFile.nextRecord();
            final ShapeAttributes attrs = new BasicShapeAttributes();

            final double[] point = ((ShapefileRecordPoint)record).getPoint();
            final Position pos = Position.fromDegrees(point[1], point[0], 2000);
            Double prob = 0d;
            for (final Entry<String, Object> entry : record.getAttributes().getEntries()) {
                if (entry.getKey().equalsIgnoreCase("Pig_EQ")) {
                    prob = (Double)entry.getValue();
                }
            }
            final Cylinder cyl = new Cylinder(pos, 5000 * prob, 30);
            attrs.setInteriorMaterial(getMaterial(prob * 15));
            attrs.setOutlineMaterial(getMaterial(prob * 15));
            cyl.setAttributes(attrs);

//            final PointPlacemark placemark = new PointPlacemark();
//            placemark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
//            placemark.setApplyVerticalExaggeration(true);
//            placemark.setAttributes(attrs);
            poleLayer.addRenderable(cyl);
        }

        // Insert the layer into the layer list just before the compass.
        int pos = 0;
        final LayerList layers = ww.getModel().getLayers();
        for (final Layer l : layers) {
            if (l instanceof CompassLayer) {
                pos = layers.indexOf(l);
            }
        }
        layers.add(pos, poleLayer);
//        layers.add(pos, eqLayer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        final JFrame frame = new JFrame("wwtest");
                        frame.setLayout(new BorderLayout());
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        final ChooseFFDataVisualPanel p = new ChooseFFDataVisualPanel(new ChooseFFDataWizardPanel());
                        p.init();
                        frame.add(p);
                        frame.pack();
                        frame.setSize(800, 600);
                        frame.setVisible(true);
                        frame.toFront();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class GPA implements GridPointAttributes {

        //~ Instance fields ----------------------------------------------------

        private final double value;
        private final Color color;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new GPA object.
         *
         * @param  value  DOCUMENT ME!
         * @param  color  DOCUMENT ME!
         */
        public GPA(final double value, final Color color) {
            this.value = value;
            this.color = color;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public double getValue() {
            return value;
        }

        @Override
        public Color getColor() {
            return color;
        }
    }

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

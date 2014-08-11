/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.worldstate;

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
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.util.BufferWrapper;
import gov.nasa.worldwind.util.WWXML;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface.GridPointAttributes;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceAttributes;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceLegend;
import gov.nasa.worldwindx.examples.util.ExampleUtil;
import gov.nasa.worldwindx.examples.util.LayerManagerLayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import de.cismet.cids.custom.crisma.pilotD.cascadeeffects.ChooseFFDataVisualPanel;
import de.cismet.cids.custom.crisma.pilotD.cascadeeffects.ChooseFFDataVisualPanel.GPA;
import de.cismet.cids.custom.crisma.worldstate.viewer.AbstractDetailView;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class ForestFireView extends AbstractDetailView {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = LoggerFactory.getLogger(ForestFireView.class);

    //~ Instance fields --------------------------------------------------------

    private WorldWindowGLCanvas c;
    private Sector demSector;

    private AnalyticSurfaceLegend legend;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ForestFireView.
     */
    public ForestFireView() {
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

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
        c.addSelectListener(new ViewControlsSelectListener(c, viewControlsLayer));

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

//                        FuelMapView.added = false;
//                        FuelMapView.addLayer(
//                            c,
//                            "http://crisma.cismet.de/geoserver251/ows",
//                            "crisma:CRISMA_PilotD_FuelMap",
//                            "fuelmap_style");
//                        for (;;) {
//                            if (FuelMapView.added) {
//                                break;
//                            }
//                            try {
//                                Thread.sleep(50);
//                            } catch (InterruptedException ex) {
//                                // noop
//                            }
//                        }

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

                        try {
                            addFF(
                                c,
                                new File("/Users/mscholl/projects/crisma/SP5/WP55/ff_sim_data/FF_sim_INTENSITY.tif"),
                                "Forest Fire - Intensity (kW / m\u00b2)",
                                "Fire intensity",
                                true,
                                true);
                            addFF(
                                c,
                                new File("/Users/mscholl/projects/crisma/SP5/WP55/ff_sim_data/FF_sim_ROS.tif"),
                                "Forest Fire - Rate of Spread (m/min)",
                                "Fire rate of spread",
                                false,
                                true);
                            addFF(
                                c,
                                new File(
                                    "/Users/mscholl/projects/crisma/SP5/WP55/layers/Shakemap_mainEvent_Mw5.6_NE_LAquila/smoke_v2.tif"),
                                "Forest Fire - Smoke (CO, \u00b5/m\u00b3)",
                                "Smoke CO concentration",
                                true,
                                false);
                            ChooseFFDataVisualPanel.addEq(
                                c,
                                new File(
                                    "/Users/mscholl/projects/crisma/SP5/WP55/layers/Shakemap_mainEvent_Mw5.6_NE_LAquila/shakemap_mainevent_newgrid_clipped_300px.tif"),
                                false);
                        } catch (final Exception e) {
                            LOG.error("cannot add ff", e);
                        }
                    }
                });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

        this.add(c, BorderLayout.CENTER);
        this.addAncestorListener(new AncestorListener() {

                @Override
                public void ancestorAdded(final AncestorEvent event) {
                    final Thread t = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        Thread.currentThread().sleep(1500);
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
                    t.start();
                }

                @Override
                public void ancestorRemoved(final AncestorEvent event) {
                }

                @Override
                public void ancestorMoved(final AncestorEvent event) {
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ww             DOCUMENT ME!
     * @param   file           DOCUMENT ME!
     * @param   layerName      DOCUMENT ME!
     * @param   legendTitle    DOCUMENT ME!
     * @param   showInitially  DOCUMENT ME!
     * @param   colored        DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void addFF(final WorldWindow ww,
            final File file,
            final String layerName,
            final String legendTitle,
            final boolean showInitially,
            final boolean colored) throws Exception {
        final RenderableLayer surfaceLayer = new RenderableLayer();
        surfaceLayer.setPickEnabled(false);
        surfaceLayer.setName(layerName);
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
        final double lowerBound = raster.getExtremes()[0];
        final double upperBound = raster.getExtremes()[1];
        final AnalyticSurface surface = new AnalyticSurface();
        surface.setSector(raster.getSector());
        surface.setDimensions(raster.getWidth(), raster.getHeight());
        final AnalyticSurfaceAttributes attr = new AnalyticSurfaceAttributes();
        if (colored) {
            surface.setValues(AnalyticSurface.createColorGradientValues(
                    raster.getBuffer(),
                    0,
                    lowerBound,
                    upperBound,
                    FuelMapView.HUE_BLUE,
                    FuelMapView.HUE_RED));
            attr.setInteriorOpacity(1);
            surface.setVerticalScale(2500 / upperBound);
            surface.setAltitude(3000);
        } else {
            surface.setValues(createSmokeIntensityValues(
                    raster.getBuffer(),
                    lowerBound,
                    upperBound,
                    raster.getTransparentValue()));
            attr.setInteriorOpacity(0.8);
            surface.setVerticalScale(0);
            surface.setAltitude(2500);
        }

        attr.setDrawOutline(false);
        attr.setDrawShadow(false);
        surface.setSurfaceAttributes(attr);

        legend = AnalyticSurfaceLegend.fromColorGradient(
                lowerBound,
                upperBound,
                FuelMapView.HUE_BLUE,
                FuelMapView.HUE_RED,
                AnalyticSurfaceLegend.createDefaultColorGradientLabels(
                    lowerBound,
                    upperBound,
                    NumberFormat.getNumberInstance()),
                AnalyticSurfaceLegend.createDefaultTitle(legendTitle));
//        legend.setScreenLocation(new Point(getWidth() - 100, getHeight() / 2));

//        final ScreenImage legend = new ScreenImage();
//        legend.setImageSource(mmiLegend);
        legend.setOpacity(1);
//        legend.setImageOffset(Offset.RIGHT_CENTER);
//        legend.setScreenOffset(Offset.RIGHT_CENTER);

//        legend.setScreenLocation(new java.awt.Point(pnl3d.getWidth() - 300, 250));

        surfaceLayer.setEnabled(showInitially);
        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    surface.setClientLayer(surfaceLayer);
                    surfaceLayer.addRenderable(surface);
//                    surfaceLayer.addRenderable(legend);
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param   bw         DOCUMENT ME!
     * @param   min        DOCUMENT ME!
     * @param   max        DOCUMENT ME!
     * @param   nullValue  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Iterable<? extends GridPointAttributes> createSmokeIntensityValues(final BufferWrapper bw,
            final double min,
            final double max,
            final double nullValue) {
        final ArrayList<AnalyticSurface.GridPointAttributes> attributesList =
            new ArrayList<AnalyticSurface.GridPointAttributes>();

        for (int i = 0; i < bw.length(); ++i) {
            final double value = bw.getDouble(i);
            if (Double.compare(value, nullValue) == 0) {
                attributesList.add(new GPA(value, new Color(0, 0, 0, 0)));
            } else {
                int grey = Double.valueOf(255 - (255 * (value / (max - min)))).intValue();
                if (grey < 0) {
                    grey = 0;
                }
                attributesList.add(new GPA(value, new Color(grey, grey, grey, 255)));
            }
        }

        return attributesList;
    }

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
    public JComponent getView() {
        if (new File("/Users/mscholl/nocontent").exists()) {
            this.add(new NoContentPanel(), BorderLayout.CENTER);
        } else {
            try {
                initMap();
            } catch (IOException ex) {
                LOG.error("cannot init map", ex);
            }
        }
        return this;
    }

    @Override
    public JComponent getMiniatureView() {
        if (new File("/Users/mscholl/nocontent").exists()) {
            return new NoContentPanel();
        } else {
            return new IconPanel(FuelMapView.class.getPackage().getName().replaceAll("\\.", "/") + "/world_256.png",
                    256,
                    256);
        }
    }

    @Override
    public String getId() {
        return "forestFireView";
    }

    @Override
    public String getDisplayName() {
        return "Forest Fire";
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
                        final ForestFireView view = new ForestFireView();
                        frame.add(view.getView());
                        frame.pack();
                        frame.setSize(1024, 768);
                        frame.setVisible(true);
                        frame.toFront();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}

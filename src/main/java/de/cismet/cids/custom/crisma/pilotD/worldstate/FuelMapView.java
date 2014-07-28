/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.worldstate;

import gov.nasa.worldwind.Factory;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.data.BufferWrapperRaster;
import gov.nasa.worldwind.data.DataRaster;
import gov.nasa.worldwind.data.DataRasterReader;
import gov.nasa.worldwind.data.DataRasterReaderFactory;
import gov.nasa.worldwind.data.TiledElevationProducer;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.geom.Extent;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.ogc.wms.WMSCapabilities;
import gov.nasa.worldwind.ogc.wms.WMSLayerCapabilities;
import gov.nasa.worldwind.ogc.wms.WMSLayerStyle;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.terrain.CompoundElevationModel;
import gov.nasa.worldwind.util.WWBufferUtil;
import gov.nasa.worldwind.util.WWMath;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwind.util.WWXML;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceAttributes;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceLegend;
import gov.nasa.worldwindx.examples.dataimport.DataInstallUtil;
import gov.nasa.worldwindx.examples.util.ExampleUtil;
import gov.nasa.worldwindx.examples.util.LayerManagerLayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;

import java.io.File;

import java.net.URI;

import java.text.DecimalFormat;
import java.text.Format;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import de.cismet.cids.custom.crisma.worldstate.viewer.AbstractDetailView;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class FuelMapView extends AbstractDetailView {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = LoggerFactory.getLogger(FuelMapView.class);

    protected static final double HUE_BLUE = 240d / 360d;
    protected static final double HUE_RED = 0d / 360d;

    //~ Instance fields --------------------------------------------------------

    private Sector demSector;

    private volatile boolean added;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DEMView.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public FuelMapView() throws Exception {
        initComponents();

        final WorldWindowGLCanvas c = new WorldWindowGLCanvas();

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

        c.addSelectListener(new ViewControlsSelectListener(c, viewControlsLayer));

        c.addRenderingExceptionListener(new RenderingExceptionListener() {

                @Override
                public void exceptionThrown(final Throwable thrwbl) {
                    LOG.error("rendering exception", thrwbl);
                }
            });

        try {
            final Document d = addElevation(
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
                        added = false;
                        addLayer(
                            c,
                            "http://geoportale2.regione.abruzzo.it/ecwp/ecw_wms.dll",
                            "IMAGES_ORTO_AQUILA_2010.ECW");
                        for (;;) {
                            if (added) {
                                break;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                // noop
                            }
                        }
                        added = false;
                        addLayer(c, "http://crisma.cismet.de/geoserver/ows", "crisma:planet_osm_line");
                        for (;;) {
                            if (added) {
                                break;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                // noop
                            }
                        }

                        added = false;
                        addLayer(
                            c,
                            "http://crisma.cismet.de/geoserver251/ows",
                            "crisma:CRISMA_PilotD_FuelMap",
                            "fuelmap_style");
                        for (;;) {
                            if (added) {
                                break;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                // noop
                            }
                        }

                        added = false;
                        addLayer(
                            c,
                            "http://crisma.cismet.de/geoserver/ows",
                            "crisma:comune_aq",
                            "pilot_d_boundaries_alt");
                        for (;;) {
                            if (added) {
                                break;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                // noop
                            }
                        }
                    }
                });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

        this.add(c);
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

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   ww    DOCUMENT ME!
     * @param   file  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private Document addElevation(final WorldWindow ww, final File file) throws Exception {
        final TiledElevationProducer producer = new TiledElevationProducer();
        Document doc = null;

        try {
            final String datasetName = "belmonte_DEM";

            final AVList params = new AVListImpl();
            params.setValue(AVKey.DATASET_NAME, datasetName);
            params.setValue(AVKey.DATA_CACHE_NAME, datasetName);
            params.setValue(
                AVKey.FILE_STORE_LOCATION,
                DataInstallUtil.getDefaultInstallLocation(WorldWind.getDataFileStore()).getAbsolutePath());
            params.setValue(AVKey.PRODUCER_ENABLE_FULL_PYRAMID, true);

            producer.setStoreParameters(params);
            producer.offerDataSource(file, null);
            producer.startProduction();

            doc = (Document)producer.getProductionResults().iterator().next();

            final Factory factory = (Factory)WorldWind.createConfigurationComponent(AVKey.ELEVATION_MODEL_FACTORY);
            final ElevationModel em = (ElevationModel)factory.createFromConfigSource(doc.getDocumentElement(), params);

            final ElevationModel defaultElevationModel = ww.getModel().getGlobe().getElevationModel();

            if (defaultElevationModel instanceof CompoundElevationModel) {
                if (!((CompoundElevationModel)defaultElevationModel).containsElevationModel(em)) {
                    ((CompoundElevationModel)defaultElevationModel).addElevationModel(em);
                }
            } else {
                final CompoundElevationModel cm = new CompoundElevationModel();
                cm.addElevationModel(defaultElevationModel);
                cm.addElevationModel(em);
                ww.getModel().getGlobe().setElevationModel(cm);
            }
        } catch (final Exception e) {
            LOG.error("cannot create elevation from file: " + file, e);
        } finally {
            producer.removeAllDataSources();
        }

        return doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ww    DOCUMENT ME!
     * @param   uri   DOCUMENT ME!
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Layer addLayer(final WorldWindow ww, final String uri, final String name) {
        return addLayer(ww, uri, name, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ww         DOCUMENT ME!
     * @param   uri        DOCUMENT ME!
     * @param   name       DOCUMENT ME!
     * @param   styleName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Layer addLayer(final WorldWindow ww, final String uri, final String name, final String styleName) {
        Layer layer = null;
        try {
            final WMSCapabilities caps = WMSCapabilities.retrieve(URI.create(uri));
            caps.parse();
            final List<WMSLayerCapabilities> namedLayerCaps = caps.getNamedLayers();

            LayerInfo li = null;
            for (final WMSLayerCapabilities lc : namedLayerCaps) {
                if (name.equals(lc.getName())) {
                    if (styleName == null) {
                        li = createLayerInfo(
                                caps,
                                lc,
                                lc.getStyles().isEmpty() ? null : lc.getStyles().iterator().next());
                    } else {
                        for (final WMSLayerStyle ls : lc.getStyles()) {
                            if (ls.getName().equalsIgnoreCase(styleName)) {
                                li = createLayerInfo(caps, lc, ls);
                                break;
                            }
                        }
                    }
                }
            }

            final AVList configParams = li.params.copy();

            // Some wms servers are slow, so increase the timeouts and limits used by world wind's retrievers.
            configParams.setValue(AVKey.URL_CONNECT_TIMEOUT, 30000);
            configParams.setValue(AVKey.URL_READ_TIMEOUT, 30000);
            configParams.setValue(AVKey.RETRIEVAL_QUEUE_STALE_REQUEST_LIMIT, 60000);

            final Factory factory = (Factory)WorldWind.createConfigurationComponent(AVKey.LAYER_FACTORY);
            layer = (Layer)factory.createFromConfigSource(caps, configParams);
            final LayerList layers = ww.getModel().getLayers();

            layer.setEnabled(true);

            int pos = 0;
            for (final Layer l : layers) {
                if (l instanceof PlaceNameLayer) {
                    pos = layers.indexOf(l);
                }
            }

            final int insert = pos;
            final Layer insertLayer = layer;
            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        layers.add(insert, insertLayer);
                        added = true;
                    }
                });
        } catch (Exception e) {
            LOG.error("cannot add layer texture", e);
        }

        return layer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ww    DOCUMENT ME!
     * @param   file  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void addFuelMap(final WorldWindow ww, final File file) throws Exception {
        final RenderableLayer surfaceLayer = new RenderableLayer();
        surfaceLayer.setPickEnabled(false);
        surfaceLayer.setName("Fuelmap");
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
        surface.setValues(AnalyticSurface.createColorGradientValues(
                raster.getBuffer(),
                raster.getTransparentValue(),
                extremes[0],
                extremes[1],
                HUE_BLUE,
                HUE_RED));
        surface.setVerticalScale(1);
        surface.setAltitude(1000);

        final AnalyticSurfaceAttributes attr = new AnalyticSurfaceAttributes();
        attr.setDrawOutline(false);
        attr.setDrawShadow(false);
        attr.setInteriorOpacity(0.6);
        surface.setSurfaceAttributes(attr);

        final Format legendLabelFormat = new DecimalFormat("# kg/m\u00b2");

        final AnalyticSurfaceLegend legend = AnalyticSurfaceLegend.fromColorGradient(
                extremes[0],
                extremes[1],
                HUE_BLUE,
                HUE_RED,
                AnalyticSurfaceLegend.createDefaultColorGradientLabels(extremes[0], extremes[1], legendLabelFormat),
                AnalyticSurfaceLegend.createDefaultTitle("Fuel"));
        legend.setOpacity(0.8);
        legend.setScreenLocation(new Point(100, 300));

        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    surface.setClientLayer(surfaceLayer);
                    surfaceLayer.addRenderable(surface);
                    surfaceLayer.addRenderable(new Renderable() {

                            @Override
                            public void render(final DrawContext dc) {
                                final Extent extent = surface.getExtent(dc);
                                if (!extent.intersects(dc.getView().getFrustumInModelCoordinates())) {
                                    return;
                                }

                                if (WWMath.computeSizeInWindowCoordinates(dc, extent) < 300) {
                                    return;
                                }

                                legend.render(dc);
                            }
                        });
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ww    DOCUMENT ME!
     * @param   file  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void addTextureFuelMap(final WorldWindow ww, final File file) throws Exception {
        final RenderableLayer surfaceLayer = new RenderableLayer();
        surfaceLayer.setPickEnabled(false);
        surfaceLayer.setName("Fuelmap");
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
        surface.setValues(AnalyticSurface.createColorGradientValues(
                raster.getBuffer(),
                raster.getTransparentValue(),
                extremes[0],
                extremes[1],
                HUE_BLUE,
                HUE_RED));
        surface.setVerticalScale(1);
        surface.setAltitude(1000);

        final AnalyticSurfaceAttributes attr = new AnalyticSurfaceAttributes();
        attr.setDrawOutline(false);
        attr.setDrawShadow(false);
        attr.setInteriorOpacity(0.6);
        surface.setSurfaceAttributes(attr);

        final Format legendLabelFormat = new DecimalFormat("# kg/m\u00b2");

        final AnalyticSurfaceLegend legend = AnalyticSurfaceLegend.fromColorGradient(
                extremes[0],
                extremes[1],
                HUE_BLUE,
                HUE_RED,
                AnalyticSurfaceLegend.createDefaultColorGradientLabels(extremes[0], extremes[1], legendLabelFormat),
                AnalyticSurfaceLegend.createDefaultTitle("Fuel"));
        legend.setOpacity(0.8);
        legend.setScreenLocation(new Point(100, 300));

        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    surface.setClientLayer(surfaceLayer);
                    surfaceLayer.addRenderable(surface);
                    surfaceLayer.addRenderable(new Renderable() {

                            @Override
                            public void render(final DrawContext dc) {
                                final Extent extent = surface.getExtent(dc);
                                if (!extent.intersects(dc.getView().getFrustumInModelCoordinates())) {
                                    return;
                                }

                                if (WWMath.computeSizeInWindowCoordinates(dc, extent) < 300) {
                                    return;
                                }

                                legend.render(dc);
                            }
                        });
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param   caps       DOCUMENT ME!
     * @param   layerCaps  DOCUMENT ME!
     * @param   style      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected LayerInfo createLayerInfo(final WMSCapabilities caps,
            final WMSLayerCapabilities layerCaps,
            final WMSLayerStyle style) {
        // Create the layer info specified by the layer's capabilities entry and the selected style.

        final LayerInfo linfo = new LayerInfo();
        linfo.caps = caps;
        linfo.params = new AVListImpl();
        linfo.params.setValue(AVKey.LAYER_NAMES, layerCaps.getName());
        if (style != null) {
            linfo.params.setValue(AVKey.STYLE_NAMES, style.getName());
        }
        final String abs = layerCaps.getLayerAbstract();
        if (!WWUtil.isEmpty(abs)) {
            linfo.params.setValue(AVKey.LAYER_ABSTRACT, abs);
        }

        linfo.params.setValue(AVKey.DISPLAY_NAME, makeTitle(caps, linfo));

        return linfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   caps       DOCUMENT ME!
     * @param   layerInfo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected static String makeTitle(final WMSCapabilities caps, final LayerInfo layerInfo) {
        final String layerNames = layerInfo.params.getStringValue(AVKey.LAYER_NAMES);
        final String styleNames = layerInfo.params.getStringValue(AVKey.STYLE_NAMES);
        final String[] lNames = layerNames.split(",");
        final String[] sNames = (styleNames != null) ? styleNames.split(",") : null;

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lNames.length; i++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            final String layerName = lNames[i];
            final WMSLayerCapabilities lc = caps.getLayerByName(layerName);
            final String layerTitle = lc.getTitle();
            sb.append((layerTitle != null) ? layerTitle : layerName);

            if ((sNames == null) || (sNames.length <= i)) {
                continue;
            }

            final String styleName = sNames[i];
            final WMSLayerStyle style = lc.getStyleByName(styleName);
            if (style == null) {
                continue;
            }

            sb.append(" : ");
            final String styleTitle = style.getTitle();
            sb.append((styleTitle != null) ? styleTitle : styleName);
        }

        return sb.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setMinimumSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BorderLayout());
    } // </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public JComponent getView() {
        return this;
    }

    @Override
    public JComponent getMiniatureView() {
        return new IconPanel(FuelMapView.class.getPackage().getName().replaceAll("\\.", "/") + "/world_256.png",
                256,
                256);
    }

    @Override
    public String getId() {
        return "fuelmap_view";
    }

    @Override
    public String getDisplayName() {
        return "Fuel Map";
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
                        final FuelMapView view = new FuelMapView();
                        frame.add(view);
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
    protected static class LayerInfo {

        //~ Instance fields ----------------------------------------------------

        protected WMSCapabilities caps;
        protected AVListImpl params = new AVListImpl();

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        protected String getTitle() {
            return params.getStringValue(AVKey.DISPLAY_NAME);
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        protected String getName() {
            return params.getStringValue(AVKey.LAYER_NAMES);
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        protected String getAbstract() {
            return params.getStringValue(AVKey.LAYER_ABSTRACT);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class FireOpacityLayer extends RenderableLayer {
    }
}

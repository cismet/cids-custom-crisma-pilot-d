/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.worldstate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

import org.deegree.datatypes.QualifiedName;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.feature.GMLFeatureCollectionDocument;
import org.deegree.ogcwebservices.wfs.capabilities.WFSFeatureType;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;

import java.net.URI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.cismet.cids.custom.crisma.worldstate.viewer.AbstractDetailView;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cismap.commons.Crs;
import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.features.DefaultStyledFeature;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.gui.piccolo.FeatureAnnotationSymbol;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.BackgroundRefreshingPanEventListener;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.RubberBandZoomListener;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWMS;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWmsGetMapUrl;
import de.cismet.cismap.commons.wfs.capabilities.FeatureType;
import de.cismet.cismap.commons.wfs.capabilities.WFSCapabilities;
import de.cismet.cismap.commons.wfs.capabilities.WFSCapabilitiesFactory;
import de.cismet.cismap.commons.wfs.capabilities.deegree.DeegreeFeatureType;

import de.cismet.security.AccessHandler.ACCESS_METHODS;

import de.cismet.security.WebAccessManager;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class ShakemapView extends AbstractDetailView {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = LoggerFactory.getLogger(ShakemapView.class);

    //~ Instance fields --------------------------------------------------------

    private final transient ShakemapMiniatureView miniatureView = new ShakemapMiniatureView();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private de.cismet.cismap.commons.gui.MappingComponent mappingComponent1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ShakemapView.
     */
    public ShakemapView() {
        initComponents();

        final JPanel p = new JPanel() {

                final Image i = ImageUtilities.loadImage(ShakemapView.class.getPackage().getName().replaceAll(
                            "\\.",
                            "/") + "/legend.png");

                @Override
                protected void paintComponent(final Graphics g) {
                    g.drawImage(i, 0, 0, this);
                    g.dispose();
                }
            };

        final Dimension d = new Dimension(129, 239);
        p.setMaximumSize(d);
        p.setMinimumSize(d);
        p.setSize(d);
        p.setPreferredSize(d);
        jPanel2.add(p, BorderLayout.CENTER);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        mappingComponent1 = new de.cismet.cismap.commons.gui.MappingComponent();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(mappingComponent1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(ShakemapView.class, "ShakemapView.jPanel2.border.title"))); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(ShakemapView.class, "ShakemapView.jPanel3.border.title"))); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(NbBundle.getMessage(ShakemapView.class, "ShakemapView.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel1, gridBagConstraints);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(NbBundle.getMessage(ShakemapView.class, "ShakemapView.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        jLabel3.setText(NbBundle.getMessage(ShakemapView.class, "ShakemapView.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel3, gridBagConstraints);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(NbBundle.getMessage(ShakemapView.class, "ShakemapView.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jPanel1, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public JComponent getView() {
        return this;
    }

    @Override
    public JComponent getMiniatureView() {
        return miniatureView;
    }

    @Override
    public String getId() {
        return "shakemaps_detailview";
    }

    @Override
    public String getDisplayName() {
        return "Shakemaps";
    }

    @Override
    public void setWorldstate(final CidsBean worldstate) {
        super.setWorldstate(worldstate);

        miniatureView.setWorldstate(worldstate);

        init();
    }

    /**
     * DOCUMENT ME!
     */
    private void init() {
        try {
            initPilotDMap(
                mappingComponent1,
                "shakemap",
                getWorldstate(),
                0.7f,
                new WFSRequestListener(
                    "http://crisma.cismet.de/geoserver/ows?service=wfs&version=1.1.0&request=GetCapabilities",
                    new QualifiedName("crisma", "shakem_1", new URI("de:cismet:cids:custom:crisma")),
                    new WFSCall()));
            activateLayerWidget(mappingComponent1);
        } catch (final Exception e) {
            LOG.error("cannot initialise shakemap miniature view", e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mappingComponent1  DOCUMENT ME!
     */
    public static void activateLayerWidget(final MappingComponent mappingComponent1) {
        mappingComponent1.setInternalLayerWidgetAvailable(true);
        mappingComponent1.addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(final MouseEvent e) {
                    // noop
                }

                @Override
                public void mouseMoved(final MouseEvent e) {
                    if ((System.currentTimeMillis() - dontDispatch) < 1300) {
                        return;
                    }

                    dontDispatch = -1;

                    final int h = mappingComponent1.getHeight();
                    final int w = mappingComponent1.getWidth();
                    final int w10 = w / 10;

                    final Polygon hotArea = new Polygon();
                    hotArea.addPoint(w - w10, h);
                    hotArea.addPoint(w, h);
                    hotArea.addPoint(w, h - w10);
                    hotArea.addPoint(w - w10, h);

                    if (hotArea.contains(e.getPoint()) && !mappingComponent1.isInternalLayerWidgetVisible()) {
                        EventQueue.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    mappingComponent1.showInternalLayerWidget(true, 300);
                                }
                            });
                        dontDispatch = System.currentTimeMillis();
                    } else if (mappingComponent1.isInternalLayerWidgetVisible()) {
                        EventQueue.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    mappingComponent1.showInternalLayerWidget(false, 300);
                                }
                            });
                        dontDispatch = System.currentTimeMillis();
                    }
                }

                private long dontDispatch = -1;
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mappingComponent1  DOCUMENT ME!
     * @param   dataitem           DOCUMENT ME!
     * @param   worldstate         DOCUMENT ME!
     * @param   translucency       DOCUMENT ME!
     * @param   listener           DOCUMENT ME!
     *
     * @throws  IOException            DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    public static void initPilotDMap(final MappingComponent mappingComponent1,
            final String dataitem,
            final CidsBean worldstate,
            final float translucency,
            final PBasicInputEventHandler listener) throws IOException {
        for (final CidsBean b : worldstate.getBeanCollectionProperty("worldstatedata")) {
            if (dataitem.equals(b.getProperty("name"))) {
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

                final TypeReference<Map<String, String>> refDesc = new TypeReference<Map<String, String>>() {
                    };

                final String jsonString = (String)b.getProperty("actualaccessinfo");
                final Map<String, String> json = m.readValue(jsonString, ref);
                final String jsonStringDesc = (String)b.getProperty("datadescriptor.defaultaccessinfo");
                final Map<String, String> jsonDesc = m.readValue(jsonStringDesc, refDesc);
                final SimpleWMS layer = new SimpleWMS(new SimpleWmsGetMapUrl(
                            jsonDesc.get("simplewms_getmap").replace("<cismap:layers>", json.get("layername"))));
                if (json.get("displayName") != null) {
                    layer.setName(json.get("displayName"));
                }

                final SimpleWMS ortho = getOrthoLayer(worldstate);
                if (ortho != null) {
                    ortho.setName("L'Aquila Orthophoto");
                    mappingModel.addLayer(ortho);
                    layer.setTranslucency(translucency);
                }

                for (final SimpleWMS s : getSupportiveLayers(worldstate)) {
                    mappingModel.addLayer(s);
                }

                if (json.get("additionalStyle") != null) {
                    final SimpleWMS layera = new SimpleWMS(new SimpleWmsGetMapUrl(
                                jsonDesc.get("simplewms_getmap").replace("<cismap:layers>", json.get("layername"))
                                            .concat("&styles=").concat(json.get("additionalStyle"))));
                    layera.setName(json.get("additionalStyleLName"));
                    layera.setEnabled(false);
                    layera.setVisible(false);
                    mappingModel.addLayer(layera);
                }

                mappingModel.addLayer(layer);

                mappingComponent1.setMappingModel(mappingModel);
                mappingComponent1.addInputListener("wfsclick", listener);
                mappingComponent1.setInteractionMode("wfsclick");
                mappingComponent1.unlock();
                mappingComponent1.gotoInitialBoundingBox();

                return;
            }
        }

        throw new IllegalStateException("no shakemap dataitem present: " + worldstate);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   worldstate  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public static SimpleWMS getOrthoLayer(final CidsBean worldstate) throws IOException {
        for (final CidsBean be : worldstate.getBeanCollectionProperty("worldstatedata")) {
            if ("ortho".equals(be.getProperty("name"))) {
                final ObjectMapper m = new ObjectMapper(new JsonFactory());
                final TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>() {
                    };

                final TypeReference<Map<String, String>> refDesc = new TypeReference<Map<String, String>>() {
                    };

                final String jsonString = (String)be.getProperty("actualaccessinfo");
                final Map<String, String> json = m.readValue(jsonString, ref);
                final String jsonStringDesc = (String)be.getProperty("datadescriptor.defaultaccessinfo");
                final Map<String, String> jsonDesc = m.readValue(jsonStringDesc, refDesc);
                final SimpleWMS layer = new SimpleWMS(new SimpleWmsGetMapUrl(
                            jsonDesc.get("simplewms_getmap").replace("<cismap:layers>", json.get("layername"))));

                layer.setTranslucency(1f);

                return layer;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   worldstate  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public static List<SimpleWMS> getSupportiveLayers(final CidsBean worldstate) throws IOException {
        final List<SimpleWMS> ret = new ArrayList<SimpleWMS>();
        final List<CidsBean> cbs = worldstate.getBeanCollectionProperty("worldstatedata");
        Collections.sort(cbs, new Comparator<CidsBean>() {

                @Override
                public int compare(final CidsBean o1, final CidsBean o2) {
                    return o1.getProperty("name").toString().compareTo(o2.getProperty("name").toString());
                }
            });
        for (final CidsBean be : cbs) {
            for (final CidsBean cat : (Collection<CidsBean>)be.getProperty("datadescriptor.categories")) {
                if ("SUPPORTIVE_WMS".equals(cat.getProperty("key"))) {
                    final ObjectMapper m = new ObjectMapper(new JsonFactory());
                    final TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>() {
                        };

                    final TypeReference<Map<String, String>> refDesc = new TypeReference<Map<String, String>>() {
                        };

                    final String jsonString = (String)be.getProperty("actualaccessinfo");
                    final Map<String, String> json = m.readValue(jsonString, ref);
                    final String jsonStringDesc = (String)be.getProperty("datadescriptor.defaultaccessinfo");
                    final Map<String, String> jsonDesc = m.readValue(jsonStringDesc, refDesc);
                    final SimpleWMS layer = new SimpleWMS(new SimpleWmsGetMapUrl(
                                jsonDesc.get("simplewms_getmap").replace("<cismap:layers>", json.get("layername"))));

                    if (json.get("displayName") == null) {
                        layer.setName(json.get("layername"));
                    } else {
                        layer.setName(json.get("displayName"));
                    }
                    layer.setTranslucency(1f);
                    layer.setVisible(false);
                    layer.setEnabled(false);

                    ret.add(layer);
                }
            }
        }

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        try {
            final FeatureType t = WFSRequestListener.getFeatureType(
                    "http://crisma.cismet.de/geoserver/ows?service=wfs&version=1.1.0&request=GetCapabilities",
                    new QualifiedName("crisma", "abcd_cell", new URI("de:cismet:cids:custom:crisma")));
            System.out.println(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //~ Inner Interfaces -------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static interface WFSCallback {

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  fc  DOCUMENT ME!
         */
        void callback(final FeatureCollection fc);

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        MappingComponent getMap();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class WFSCall implements WFSCallback {

        //~ Methods ------------------------------------------------------------

        @Override
        public void callback(final FeatureCollection fc) {
            final Feature f = fc.iterator().next();
            String a;
            String b;
            a = b = null;
            try {
                for (final FeatureProperty fp : f.getProperties()) {
                    if (fp.getName().equals(
                                    new QualifiedName("crisma", "pga", new URI("de:cismet:cids:custom:crisma")))) {
                        a = fp.getValue().toString();
                    } else if (fp.getName().equals(
                                    new QualifiedName("crisma", "mmi", new URI("de:cismet:cids:custom:crisma")))) {
                        b = fp.getValue().toString();
                    }
                }

                final String aa = a;
                final String bb = b;

                EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            jLabel2.setText((Math.round(Double.parseDouble(aa) * 100) / 100d) + " g");
                            jLabel4.setText((Math.round(Double.parseDouble(bb) * 10) / 10d) + " MMI");
                        }
                    });
            } catch (Exception ex) {
                throw new IllegalStateException("cannot get feature property", ex);
            }
        }

        @Override
        public MappingComponent getMap() {
            return mappingComponent1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static final class WFSRequestListener extends BackgroundRefreshingPanEventListener {

        //~ Instance fields ----------------------------------------------------

        private final RubberBandZoomListener zoomDelegate;
        private final String capUrl;
        private final QualifiedName qname;
        private final WFSCallback cb;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new WFSRequestListener object.
         */
        public WFSRequestListener() {
            this(null, null, null);
        }

        /**
         * Creates a new PanAndMousewheelZoomListener object.
         *
         * @param  capUrl  DOCUMENT ME!
         * @param  qname   DOCUMENT ME!
         * @param  cb      DOCUMENT ME!
         */
        public WFSRequestListener(final String capUrl, final QualifiedName qname, final WFSCallback cb) {
            zoomDelegate = new RubberBandZoomListener();
            this.capUrl = capUrl;
            this.qname = qname;
            this.cb = cb;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void mouseWheelRotated(final PInputEvent pInputEvent) {
            zoomDelegate.mouseWheelRotated(pInputEvent);
        }

        @Override
        public void mouseClicked(final PInputEvent event) {
            if (capUrl == null) {
                return;
            }

            final MappingComponent mc = (MappingComponent)event.getComponent();
            final double xCoord = mc.getWtst().getSourceX(event.getPosition().getX() - mc.getClip_offset_x());
            final double yCoord = mc.getWtst().getSourceY(event.getPosition().getY() - mc.getClip_offset_y());

            final Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                final FeatureType ft = getFeatureType(capUrl, qname);
                                final Element root = ft.getWFSCapabilities().getServiceFacade().getGetFeatureQuery(ft);
                                root.setAttribute("maxFeatures", "1");
                                root.setAttribute("resultType", "results");
                                final Namespace wfsNs = Namespace.getNamespace("http://www.opengis.net/wfs"); // NOI18N
                                final Namespace ogcNs = Namespace.getNamespace("http://www.opengis.net/ogc"); // NOI18N
                                final Namespace gmlNs = Namespace.getNamespace("http://www.opengis.net/gml"); // NOI18N
                                final Element query = root.getChild("Query", wfsNs);                          // NOI18N
                                query.setAttribute("srsName", "EPSG:32633");                                  // NOI18N
                                final Element filter = query.getChild("Filter", ogcNs);                       // NOI18N
                                filter.removeChildren("BBOX", ogcNs);                                         // NOI18N
                                final Element intersects = new Element("Intersects", ogcNs);                  // NOI18N
                                filter.addContent(intersects);
                                final Element propName = new Element("PropertyName", ogcNs);                  // NOI18N
                                propName.setText("crisma:the_geom");                                          // NOI18N
                                intersects.addContent(propName);
                                final Element pointElement = new Element("Point", gmlNs);                     // NOI18N
                                intersects.addContent(pointElement);

                                // coordinates
                                final Element pos = new Element("coordinates", gmlNs); // NOI18N
                                pointElement.addContent(pos);
                                // coordinates
                                pos.setText(xCoord + "," + yCoord); // NOI18N

                                final XMLOutputter raw = new XMLOutputter(Format.getRawFormat());

                                if (LOG.isDebugEnabled()) {
                                    final XMLOutputter pretty = new XMLOutputter(Format.getPrettyFormat());
                                    LOG.debug("created feature query: " + pretty.outputString(root)); // NOI18N
                                }

                                final String req = raw.outputString(root);
                                final InputStream resp = WebAccessManager.getInstance()
                                            .doRequest(
                                                ft.getWFSCapabilities().getURL(),
                                                req,
                                                ACCESS_METHODS.POST_REQUEST);
                                final GMLFeatureCollectionDocument gmlDoc = new GMLFeatureCollectionDocument();
                                gmlDoc.load(
                                    resp,
                                    "http://crisma");
                                final FeatureCollection fc = gmlDoc.parse();
                                if (fc.size() == 1) {
                                    cb.callback(fc);
                                    cb.getMap().getFeatureCollection().removeAllFeatures();
                                    cb.getMap().getFeatureCollection().addFeature(new ClickFeature(xCoord, yCoord));
                                } else {
                                    LOG.warn("no feature at point");
                                }
                            } catch (final Exception ex) {
                                LOG.error("cannot fetch feature", ex);
                            }
                        }
                    });
            t.start();
        }

        /**
         * DOCUMENT ME!
         *
         * @param   capabilitiesUrl  DOCUMENT ME!
         * @param   qname            DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         *
         * @throws  IOException  DOCUMENT ME!
         */
        public static FeatureType getFeatureType(final String capabilitiesUrl, final QualifiedName qname)
                throws IOException {
            try {
                final WFSCapabilitiesFactory factory = new WFSCapabilitiesFactory();

                final WFSCapabilities wfsCapabilities = factory.createCapabilities(capabilitiesUrl);
                // FIXME: evil actions lead to the cake... the feature types without fetching their description, normal
                // facilities will do getFeatureInfo for every feature type, which is very inefficient and slow
                final Field field = wfsCapabilities.getClass().getDeclaredField("cap"); // NOI18N
                field.setAccessible(true);
                final org.deegree.ogcwebservices.wfs.capabilities.WFSCapabilities dCaps =
                    (org.deegree.ogcwebservices.wfs.capabilities.WFSCapabilities)field.get(
                        wfsCapabilities);
                final WFSFeatureType basinType = dCaps.getFeatureTypeList().getFeatureType(qname);

                if (basinType == null) {
                    throw new IllegalStateException("WFS does not serve feature with given qname: " + qname); // NOI18N
                }

                return new DeegreeFeatureType(basinType, wfsCapabilities);
            } catch (final Exception e) {
                final String message = "cannot fetch feature type for capabilities url and qname: [" // NOI18N
                            + capabilitiesUrl
                            + "|"                                                                    // NOI18N
                            + qname + "]";                                                           // NOI18N
                LOG.error(message, e);

                throw new IOException(message, e);
            }
        }

        //~ Inner Classes ------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @version  $Revision$, $Date$
         */
        private static final class ClickFeature extends DefaultStyledFeature {

            //~ Constructors ---------------------------------------------------

            /**
             * Creates a new ClickFeature object.
             *
             * @param  x  DOCUMENT ME!
             * @param  y  DOCUMENT ME!
             */
            public ClickFeature(final double x, final double y) {
                final GeometryFactory gf = new GeometryFactory(new PrecisionModel(PrecisionModel.FIXED), 32633);
                setGeometry(gf.createPoint(new Coordinate(x, y)));
            }

            //~ Methods --------------------------------------------------------

            @Override
            public FeatureAnnotationSymbol getPointAnnotationSymbol() {
                return super.getPointAnnotationSymbol(); // To change body of generated methods, choose Tools |
                                                         // Templates.
            }
        }
    }
}

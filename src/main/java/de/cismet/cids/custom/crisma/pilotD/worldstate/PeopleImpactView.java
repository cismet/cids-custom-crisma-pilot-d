/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.worldstate;

import org.deegree.datatypes.QualifiedName;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureProperty;

import org.openide.util.NbBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;

import java.net.URI;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.cismet.cids.custom.crisma.MapSync;
import de.cismet.cids.custom.crisma.pilotD.worldstate.ShakemapView.WFSCallback;
import de.cismet.cids.custom.crisma.pilotD.worldstate.ShakemapView.WFSRequestListener;
import de.cismet.cids.custom.crisma.worldstate.viewer.AbstractDetailView;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cismap.commons.gui.MappingComponent;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class PeopleImpactView extends AbstractDetailView implements MapSync {

    //~ Instance fields --------------------------------------------------------

    private final transient PeopleImpactMiniatureView miniatureView = new PeopleImpactMiniatureView();

    private final transient Logger LOG = LoggerFactory.getLogger(PeopleImpactView.class);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private de.cismet.cismap.commons.gui.MappingComponent mappingComponent1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ShakemapView.
     */
    public PeopleImpactView() {
        initComponents();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new ColorLabel(new Color(9023451));
        jLabel14 = new ColorLabel(new Color(13033627));
        jLabel15 = new ColorLabel(new Color(15979335));
        jLabel16 = new ColorLabel(new Color(14110278));

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(mappingComponent1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jPanel2.border.title"))); // NOI18N
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel5.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel5, gridBagConstraints);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel6, gridBagConstraints);

        jLabel7.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel7.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel7, gridBagConstraints);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel8.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jPanel3.border.title"))); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel9.setText(NbBundle.getMessage(PeopleImpactView.class, "BuildingsVulnerabilityClassesView.jLabel9.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel9, gridBagConstraints);

        jLabel10.setText(NbBundle.getMessage(
                PeopleImpactView.class,
                "BuildingsVulnerabilityClassesView.jLabel10.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel10, gridBagConstraints);

        jLabel11.setText(NbBundle.getMessage(
                PeopleImpactView.class,
                "BuildingsVulnerabilityClassesView.jLabel11.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel11, gridBagConstraints);

        jLabel12.setText(NbBundle.getMessage(
                PeopleImpactView.class,
                "BuildingsVulnerabilityClassesView.jLabel12.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel12, gridBagConstraints);

        jLabel13.setText(NbBundle.getMessage(
                PeopleImpactView.class,
                "BuildingsVulnerabilityClassesView.jLabel13.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jLabel13, gridBagConstraints);

        jLabel14.setText(NbBundle.getMessage(
                PeopleImpactView.class,
                "BuildingsVulnerabilityClassesView.jLabel14.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel3.add(jLabel14, gridBagConstraints);

        jLabel15.setText(NbBundle.getMessage(
                PeopleImpactView.class,
                "BuildingsVulnerabilityClassesView.jLabel15.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel3.add(jLabel15, gridBagConstraints);

        jLabel16.setText(NbBundle.getMessage(
                PeopleImpactView.class,
                "BuildingsVulnerabilityClassesView.jLabel16.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel3.add(jLabel16, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
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
        return "people_impact_detailview";
    }

    @Override
    public String getDisplayName() {
        return "People Impact";
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
            ShakemapView.initPilotDMap(
                mappingComponent1,
                "buildVulCl",
                getWorldstate(),
                0.8f,
                new WFSRequestListener(
                    "http://crisma.cismet.de/geoserver/ows?service=wfs&version=1.1.0&request=GetCapabilities",
                    new QualifiedName("crisma", "abcd_cell", new URI("de:cismet:cids:custom:crisma")),
                    new WFSCall()));
            ShakemapView.activateLayerWidget(mappingComponent1);
        } catch (final Exception e) {
            LOG.error("cannot initialise building impact view", e);
        }
    }

    @Override
    public MappingComponent getMap() {
        return mappingComponent1;
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
            String c;
            String d;
            a = b = c = d = null;
            try {
                for (final FeatureProperty fp : f.getProperties()) {
                    if (fp.getName().equals(
                                    new QualifiedName("crisma", "a", new URI("de:cismet:cids:custom:crisma")))) {
                        a = fp.getValue().toString();
                    } else if (fp.getName().equals(
                                    new QualifiedName("crisma", "b", new URI("de:cismet:cids:custom:crisma")))) {
                        b = fp.getValue().toString();
                    } else if (fp.getName().equals(
                                    new QualifiedName("crisma", "c", new URI("de:cismet:cids:custom:crisma")))) {
                        c = fp.getValue().toString();
                    } else if (fp.getName().equals(
                                    new QualifiedName("crisma", "d", new URI("de:cismet:cids:custom:crisma")))) {
                        d = fp.getValue().toString();
                    }
                }

                final String aa;
                final String bb;
                final String cc;
                final String dd;
                aa = String.valueOf(Math.round(Double.parseDouble(a) * 100) / 100d);
                bb = String.valueOf(Math.round(Double.parseDouble(b) * 100) / 100d);
                cc = String.valueOf(Math.round(Double.parseDouble(c) * 100) / 100d);
                dd = String.valueOf(Math.round(Double.parseDouble(d) * 100) / 100d);

                EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            jLabel2.setText(aa + " Buildings");
                            jLabel4.setText(bb + " Buildings");
                            jLabel6.setText(cc + " Buildings");
                            jLabel8.setText(dd + " Buildings");
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
    public static final class ColorLabel extends JLabel {

        //~ Instance fields ----------------------------------------------------

        private final Color c;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ColorLabel object.
         *
         * @param  c  DOCUMENT ME!
         */
        public ColorLabel(final Color c) {
            this.c = c;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void paint(final Graphics g) {
            g.setColor(c);
            g.drawRect(0, 0, getWidth(), getHeight());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

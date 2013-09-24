/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.model;

import com.vividsolutions.jts.geom.Point;

import org.openide.WizardDescriptor;

import java.awt.Component;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.commons.gui.wizard.AbstractWizardPanel;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class ChooseEQDataWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_DEPTH = "__prop_depth__";
    public static final String PROP_MAGNITUDE = "__prop_magnitude__";
    public static final String PROP_EPICENTER = "__prop_epicenter__";

    //~ Instance fields --------------------------------------------------------

    private transient int depth;
    private transient float magnitude;
    private transient Point epicenter;
    private transient CidsBean worldstate;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsBean getWorldstate() {
        return worldstate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  worldstate  DOCUMENT ME!
     */
    public void setWorldstate(final CidsBean worldstate) {
        this.worldstate = worldstate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getDepth() {
        return depth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  depth  DOCUMENT ME!
     */
    public void setDepth(final int depth) {
        this.depth = depth;

        changeSupport.fireChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getMagnitude() {
        return magnitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  magnitude  DOCUMENT ME!
     */
    public void setMagnitude(final float magnitude) {
        this.magnitude = magnitude;

        changeSupport.fireChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Point getEpicenter() {
        return epicenter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  epicenter  DOCUMENT ME!
     */
    public void setEpicenter(final Point epicenter) {
        this.epicenter = epicenter;

        changeSupport.fireChange();
    }

    @Override
    protected Component createComponent() {
        return new ChooseEQDataVisualPanel(this);
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        final Integer d = (Integer)wizard.getProperty(PROP_DEPTH);
        setDepth((d == null) ? 0 : d);
        final Float f = (Float)wizard.getProperty(PROP_MAGNITUDE);
        setMagnitude((f == null) ? 0f : f);
        setEpicenter((Point)wizard.getProperty(PROP_EPICENTER));
        setWorldstate((CidsBean)wizard.getProperty(ExecModelWizardAction.PROP_WORLDSTATE));

        ((ChooseEQDataVisualPanel)getComponent()).init();
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_DEPTH, getDepth());
        wizard.putProperty(PROP_MAGNITUDE, getMagnitude());
        wizard.putProperty(PROP_EPICENTER, getEpicenter());
    }
}

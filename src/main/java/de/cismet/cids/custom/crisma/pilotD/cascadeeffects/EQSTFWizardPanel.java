/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.cascadeeffects;

import com.vividsolutions.jts.geom.Polygon;

import org.openide.WizardDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Component;

import java.io.IOException;

import java.util.Date;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.commons.gui.wizard.AbstractWizardPanel;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class EQSTFWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    /** LOGGER. */
    private static final transient Logger LOG = LoggerFactory.getLogger(EQSTFWizardPanel.class);

    public static final String PROP_A_VALUE = "__prop_a_value__";
    public static final String PROP_B_VALUE = "__prop_b_value__";
    public static final String PROP_NO_OF_EVENTS = "__prop_no_of_events__";
    public static final String PROP_EVENTS_TIMESPAN = "__prop_events_timespan__";
    public static final String PROP_EVENTS_RANGE = "__prop_events_range__";
    public static final String PROP_EVENTS_DEPTH = "__prop_events_depth__";

    //~ Instance fields --------------------------------------------------------

    private int noOfOccurStart;
    private int noOfOccurEnd;
    private Date timespanStart;
    private Date timespanEnd;
    private int aValue;
    private int bValue;
    private Polygon range;
    private int depthStart;
    private int depthEnd;
    private CidsBean worldstate;

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Component createComponent() {
        return new EQSTFVisualPanel(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getNoOfOccurStart() {
        return noOfOccurStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  noOfOccurStart  DOCUMENT ME!
     */
    public void setNoOfOccurStart(final int noOfOccurStart) {
        this.noOfOccurStart = noOfOccurStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getNoOfOccurEnd() {
        return noOfOccurEnd;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  noOfOccurEnd  DOCUMENT ME!
     */
    public void setNoOfOccurEnd(final int noOfOccurEnd) {
        this.noOfOccurEnd = noOfOccurEnd;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Date getTimespanStart() {
        return timespanStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  timespanStart  DOCUMENT ME!
     */
    public void setTimespanStart(final Date timespanStart) {
        this.timespanStart = timespanStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Date getTimespanEnd() {
        return timespanEnd;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  timespanEnd  DOCUMENT ME!
     */
    public void setTimespanEnd(final Date timespanEnd) {
        this.timespanEnd = timespanEnd;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getaValue() {
        return aValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  aValue  DOCUMENT ME!
     */
    public void setaValue(final int aValue) {
        this.aValue = aValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getbValue() {
        return bValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bValue  DOCUMENT ME!
     */
    public void setbValue(final int bValue) {
        this.bValue = bValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Polygon getRange() {
        return range;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  range  DOCUMENT ME!
     */
    public void setRange(final Polygon range) {
        this.range = range;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getDepthStart() {
        return depthStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  depthStart  DOCUMENT ME!
     */
    public void setDepthStart(final int depthStart) {
        this.depthStart = depthStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getDepthEnd() {
        return depthEnd;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  depthEnd  DOCUMENT ME!
     */
    public void setDepthEnd(final int depthEnd) {
        this.depthEnd = depthEnd;
    }

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

    @Override
    protected void read(final WizardDescriptor wizard) {
        final int[] noOfOccur = (int[])wizard.getProperty(PROP_NO_OF_EVENTS);
        if ((noOfOccur == null) || (noOfOccur.length != 2)) {
            setNoOfOccurStart(0);
            setNoOfOccurEnd(0);
        } else {
            setNoOfOccurStart(noOfOccur[0]);
            setNoOfOccurEnd(noOfOccur[1]);
        }

        final Date[] timespan = (Date[])wizard.getProperty(PROP_EVENTS_TIMESPAN);
        if ((timespan == null) || (timespan.length != 2)) {
            setTimespanStart(null);
            setTimespanEnd(null);
        } else {
            setTimespanStart(timespan[0]);
            setTimespanEnd(timespan[1]);
        }

        setaValue((wizard.getProperty(PROP_A_VALUE) == null) ? 0 : (Integer)wizard.getProperty(PROP_A_VALUE));
        setbValue((wizard.getProperty(PROP_B_VALUE) == null) ? 0 : (Integer)wizard.getProperty(PROP_B_VALUE));

        setRange((Polygon)wizard.getProperty(PROP_EVENTS_RANGE));

        final int[] depth = (int[])wizard.getProperty(PROP_EVENTS_DEPTH);
        if ((depth == null) || (depth.length != 2)) {
            setDepthStart(0);
            setDepthEnd(0);
        } else {
            setDepthStart(depth[0]);
            setDepthEnd(depth[1]);
        }

        setWorldstate((CidsBean)wizard.getProperty(CascadeEffectsWizardAction.PROP_WORLDSTATE));

        try {
            ((EQSTFVisualPanel)getComponent()).init();
        } catch (IOException ex) {
            LOG.error("cannot init", ex);
        }
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_NO_OF_EVENTS, new int[] { getNoOfOccurStart(), getNoOfOccurEnd() });
        wizard.putProperty(PROP_EVENTS_TIMESPAN, new Date[] { getTimespanStart(), getTimespanEnd() });
        wizard.putProperty(PROP_A_VALUE, aValue);
        wizard.putProperty(PROP_B_VALUE, bValue);
        wizard.putProperty(PROP_EVENTS_RANGE, getRange());
        wizard.putProperty(PROP_EVENTS_DEPTH, new int[] { getDepthStart(), getDepthEnd() });
    }
}

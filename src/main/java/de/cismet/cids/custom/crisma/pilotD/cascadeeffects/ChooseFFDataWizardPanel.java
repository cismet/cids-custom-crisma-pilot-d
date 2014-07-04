/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.cascadeeffects;

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
public final class ChooseFFDataWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_WINDDIRECTION = "__prop_winddirection__";
    public static final String PROP_WINDSPEED = "__prop_windspeed__";
    public static final String PROP_ATMOS_STABILITY = "__prop_atmos_stability__";
    public static final String PROP_SOURCE = "__prop_source__";

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum AtmosStability {

        //~ Enum constants -----------------------------------------------------

        STABLE, NEUTRAL, UNSTABLE
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum FuelMoisture {

        //~ Enum constants -----------------------------------------------------

        HER, MAT, EUC, PPIN, FOLC, RESE, CUSTOM
    }

    //~ Instance fields --------------------------------------------------------

    private transient float windSpeed;
    private transient float windDirection;
    private transient AtmosStability atmosStability;
    private transient Point source;
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
    public float getWindSpeed() {
        return windSpeed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  windSpeed  DOCUMENT ME!
     */
    public void setWindSpeed(final float windSpeed) {
        this.windSpeed = windSpeed;

        changeSupport.fireChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Point getSource() {
        return source;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  source  DOCUMENT ME!
     */
    public void setSource(final Point source) {
        this.source = source;

        changeSupport.fireChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getWindDirection() {
        return windDirection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  windDirection  DOCUMENT ME!
     */
    public void setWindDirection(final float windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public AtmosStability getAtmosStability() {
        return atmosStability;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  atmosStability  DOCUMENT ME!
     */
    public void setAtmosStability(final AtmosStability atmosStability) {
        this.atmosStability = atmosStability;
    }

    @Override
    protected Component createComponent() {
        return new ChooseFFDataVisualPanel(this);
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        Float f = (Float)wizard.getProperty(PROP_WINDSPEED);
        setWindSpeed((f == null) ? 0f : f);
        f = (Float)wizard.getProperty(PROP_WINDDIRECTION);
        setWindDirection((f == null) ? 0f : f);
        setSource((Point)wizard.getProperty(PROP_SOURCE));
        setAtmosStability((AtmosStability)wizard.getProperty(PROP_ATMOS_STABILITY));
        setWorldstate((CidsBean)wizard.getProperty(CascadeEffectsWizardAction.PROP_WORLDSTATE));

        ((ChooseFFDataVisualPanel)getComponent()).init();
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_SOURCE, getSource());
        wizard.putProperty(PROP_ATMOS_STABILITY, getAtmosStability());
        wizard.putProperty(PROP_WINDDIRECTION, getWindDirection());
        wizard.putProperty(PROP_WINDSPEED, getWindSpeed());
    }
}

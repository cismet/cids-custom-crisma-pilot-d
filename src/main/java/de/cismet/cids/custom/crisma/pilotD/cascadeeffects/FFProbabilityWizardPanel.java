/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.cascadeeffects;

import com.vividsolutions.jts.geom.Geometry;

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
public final class FFProbabilityWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_FF_IGNITON_POINT = "__prop_ff_ignition_point__"; // NOI18N

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum AtmosphericStability {

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

    private CidsBean worldstate;
    private Geometry geometry;
    private double windSpeed;
    private double windDirection;
    private AtmosphericStability atmosphericStability;
    private FuelMoisture fuelMoistureType;
    private double fuilMoisturePercentage;

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Component createComponent() {
        return new FFProbabilityVisualPanel(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  geometry  DOCUMENT ME!
     */
    public void setGeometry(final Geometry geometry) {
        this.geometry = geometry;
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

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  windSpeed  DOCUMENT ME!
     */
    public void setWindSpeed(final double windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public double getWindDirection() {
        return windDirection;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  windDirection  DOCUMENT ME!
     */
    public void setWindDirection(final double windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public AtmosphericStability getAtmosphericStability() {
        return atmosphericStability;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  atmosphericStability  DOCUMENT ME!
     */
    public void setAtmosphericStability(final AtmosphericStability atmosphericStability) {
        this.atmosphericStability = atmosphericStability;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FuelMoisture getFuelMoistureType() {
        return fuelMoistureType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fuelMoistureType  DOCUMENT ME!
     */
    public void setFuelMoistureType(final FuelMoisture fuelMoistureType) {
        this.fuelMoistureType = fuelMoistureType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public double getFuilMoisturePercentage() {
        return fuilMoisturePercentage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fuilMoisturePercentage  DOCUMENT ME!
     */
    public void setFuilMoisturePercentage(final double fuilMoisturePercentage) {
        this.fuilMoisturePercentage = fuilMoisturePercentage;
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        setWorldstate((CidsBean)wizard.getProperty(CascadeEffectsWizardAction.PROP_WORLDSTATE));
        setGeometry((Geometry)wizard.getProperty(PROP_FF_IGNITON_POINT));
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_FF_IGNITON_POINT, getGeometry());
    }
}

/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.model;

import org.openide.WizardDescriptor;

import java.awt.Component;

import de.cismet.commons.gui.wizard.AbstractWizardPanel;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class ChooseEvacTimeWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final String PROP_EVAC_TIME_MINUTES = "__prop_evac_time_minutes__";

    //~ Instance fields --------------------------------------------------------

    private transient int evacTimeMinutes;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getEvacTimeMinutes() {
        return evacTimeMinutes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evacTimeMinutes  DOCUMENT ME!
     */
    public void setEvacTimeMinutes(final int evacTimeMinutes) {
        this.evacTimeMinutes = evacTimeMinutes;

        changeSupport.fireChange();
    }

    @Override
    protected Component createComponent() {
        return new ChooseEvacTimeVisualPanel(this);
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        final Integer min = (Integer)wizard.getProperty(PROP_EVAC_TIME_MINUTES);
        setEvacTimeMinutes((min == null) ? 0 : min);

        ((ChooseEvacTimeVisualPanel)getComponent()).init();
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_EVAC_TIME_MINUTES, getEvacTimeMinutes());
    }
}

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

import java.util.Date;

import de.cismet.commons.gui.wizard.AbstractWizardPanel;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class ChooseEQDateWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_EQ_DATE = "__prop_eq_date__";

    //~ Instance fields --------------------------------------------------------

    private transient Date date;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  date  DOCUMENT ME!
     */
    public void setDate(final Date date) {
        this.date = date;

        changeSupport.fireChange();
    }

    @Override
    protected Component createComponent() {
        return new ChooseEQDateVisualPanel(this);
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        setDate((Date)wizard.getProperty(PROP_EQ_DATE));

        ((ChooseEQDateVisualPanel)getComponent()).init();
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_EQ_DATE, getDate());
    }
}

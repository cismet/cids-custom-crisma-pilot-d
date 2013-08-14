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
public final class EnterMetadataWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_NAME = "__prop_name__";
    public static final String PROP_DESCRIPTION = "__prop_description__";

    //~ Instance fields --------------------------------------------------------

    private transient String name;
    private transient String description;

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Component createComponent() {
        return new EnterMetadataVisualPanel(this);
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        setName((String)wizard.getProperty(PROP_NAME));
        setDescription((String)wizard.getProperty(PROP_DESCRIPTION));
        ((EnterMetadataVisualPanel)getComponent()).init();
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_NAME, getName());
        wizard.putProperty(PROP_DESCRIPTION, getDescription());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    public void setName(final String name) {
        this.name = name;

        changeSupport.fireChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  description  DOCUMENT ME!
     */
    public void setDescription(final String description) {
        this.description = description;

        changeSupport.fireChange();
    }
}

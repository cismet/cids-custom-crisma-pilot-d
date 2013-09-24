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

import java.util.ArrayList;
import java.util.List;

import de.cismet.commons.gui.wizard.AbstractWizardPanel;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class ChooseModelWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_SELECTED_TRANSITIONS = "__prop_selected_transitions__";

    //~ Instance fields --------------------------------------------------------

    private transient List selectedModels;

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Component createComponent() {
        return new ChooseModelVisualPanel(this);
    }

    @Override
    public boolean isValid() {
        return getSelectedModels().size() > 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List getSelectedModels() {
        return selectedModels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectedModels  DOCUMENT ME!
     */
    public void setSelectedModels(final List selectedModels) {
        if (selectedModels == null) {
            this.selectedModels = new ArrayList();
        } else {
            this.selectedModels = selectedModels;
        }

        changeSupport.fireChange();
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        setSelectedModels((List)wizard.getProperty(PROP_SELECTED_TRANSITIONS));
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_SELECTED_TRANSITIONS, getSelectedModels());
    }
}

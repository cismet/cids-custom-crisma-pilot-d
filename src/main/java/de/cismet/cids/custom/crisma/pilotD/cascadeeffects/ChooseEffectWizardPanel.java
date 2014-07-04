/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.cascadeeffects;

import org.openide.WizardDescriptor;

import java.awt.Component;

import de.cismet.commons.gui.wizard.AbstractWizardPanel;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class ChooseEffectWizardPanel extends AbstractWizardPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_SELECTED_EFFECT = "__prop_selected_effect__";

    //~ Instance fields --------------------------------------------------------

    private transient String selectedEffect;

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Component createComponent() {
        return new ChooseEffectVisualPanel(this);
    }

    @Override
    public boolean isValid() {
        return getSelectedEffect() != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSelectedEffect() {
        return selectedEffect;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectedEffect  DOCUMENT ME!
     */
    public void setSelectedEffect(final String selectedEffect) {
        this.selectedEffect = selectedEffect;

        changeSupport.fireChange();
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        setSelectedEffect((String)wizard.getProperty(PROP_SELECTED_EFFECT));
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_SELECTED_EFFECT, getSelectedEffect());
    }
}

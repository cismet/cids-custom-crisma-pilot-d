/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.model;

import Sirius.navigator.ui.ComponentRegistry;

import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

import de.cismet.cids.utils.abstracts.AbstractCidsBeanAction;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class ExecModelWizardAction extends AbstractCidsBeanAction {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_WORLDSTATE = "__prop_worldstate__";

    //~ Instance fields --------------------------------------------------------

    private transient ExecModelWizardIterator it;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ExecModelWizardAction object.
     */
    public ExecModelWizardAction() {
        super("Execute Model");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final ExecModelWizardIterator it = getIterator();
        final WizardDescriptor wizard = new WizardDescriptor(it);
        it.setWizard(wizard);

        wizard.setTitleFormat(new MessageFormat("{0}"));
        wizard.setTitle("Execute Model Wizard");
        wizard.putProperty(PROP_WORLDSTATE, getCidsBean());

        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizard);
        dialog.pack();
        dialog.setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
        dialog.setVisible(true);
        dialog.toFront();

        final boolean cancelled = wizard.getValue() != WizardDescriptor.FINISH_OPTION;

        if (!cancelled) {
            System.out.println(wizard);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ExecModelWizardIterator getIterator() {
        if (it == null) {
            it = new ExecModelWizardIterator();
        }

        return it;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class ExecModelWizardIterator implements WizardDescriptor.Iterator {

        //~ Instance fields ----------------------------------------------------

        private WizardDescriptor.Panel[] allPanels;
        private WizardDescriptor.Panel[] currentPanels;
        private WizardDescriptor wizard;

        private int currentIndex;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ExecModelWizardIterator object.
         */
        public ExecModelWizardIterator() {
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  wizard  DOCUMENT ME!
         */
        public void setWizard(final WizardDescriptor wizard) {
            this.wizard = wizard;

            initPanels();

            currentPanels = new Panel[1];
            currentPanels[0] = allPanels[0];
            currentIndex = 0;
            wizard.putProperty(
                WizardDescriptor.PROP_CONTENT_DATA,
                new String[] { currentPanels[0].getComponent().getName() });
            wizard.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, currentIndex);
        }

        /**
         * DOCUMENT ME!
         */
        private void initPanels() {
            if (allPanels == null) {
                allPanels = new WizardDescriptor.Panel[] {
                        new ChooseModelWizardPanel(),
                        new ChooseEQDataWizardPanel(),
                        new ChooseEQDateWizardPanel(),
                        new ChooseEvacTimeWizardPanel(),
                        new EnterMetadataWizardPanel()
                    };

                final String[] steps = new String[allPanels.length];
                for (int i = 0; i < allPanels.length; i++) {
                    final Component c = allPanels[i].getComponent();
                    // Default step name to component name of panel. Mainly useful
                    // for getting the name of the target chooser to appear in the
                    // list of steps.
                    steps[i] = c.getName();
                    if (c instanceof JComponent) {
                        // assume Swing components
                        final JComponent jc = (JComponent)c;
                        // Sets step number of a component
                        jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(i));
                        // Sets steps names for a panel
                        jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                        // Turn on subtitle creation on each step
                        jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
                        // Show steps on the left side with the image on the
                        // background
                        jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
                        // Turn on numbering of all steps
                        jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
                    }
                }
            }
        }

        @Override
        public Panel current() {
            initPanels();

            return currentPanels[currentIndex];
        }

        @Override
        public String name() {
            if (currentIndex == 0) {
                return currentIndex + 1 + " of ... steps";
            } else {
                return currentIndex + 1 + " of " + currentPanels.length + "steps";
            }
        }

        @Override
        public boolean hasNext() {
            initPanels();

            if (currentIndex == 0) {
                return true;
            }

            return currentIndex < (currentPanels.length - 1);
        }

        @Override
        public boolean hasPrevious() {
            return currentIndex > 0;
        }

        @Override
        public void nextPanel() {
            if (!hasNext()) {
                throw new NoSuchElementException("no next panel available");
            }

            if (currentIndex == 0) {
                final List<String> l = (List<String>)wizard.getProperty(ChooseModelWizardPanel.PROP_SELECTED_MODELS);
                final List<String> steps = new ArrayList<String>();
                final List<Panel> panels = new ArrayList<Panel>();
                steps.add(allPanels[0].getComponent().getName());
                panels.add(allPanels[0]);

                for (final String s : l) {
                    if ("Building Impact Model".equals(s)) {
                        steps.add(allPanels[1].getComponent().getName());
                        panels.add(allPanels[1]);
                    } else if ("Evacuation Model".equals(s)) {
                        steps.add(allPanels[3].getComponent().getName());
                        panels.add(allPanels[3]);
                    } else if ("Population Impact Model".equals(s)) {
                        steps.add(allPanels[2].getComponent().getName());
                        panels.add(allPanels[2]);
                    }
                }

                steps.add(allPanels[4].getComponent().getName());
                panels.add(allPanels[4]);
                currentPanels = panels.toArray(new Panel[panels.size()]);
                wizard.putProperty(WizardDescriptor.PROP_CONTENT_DATA, steps.toArray(new String[steps.size()]));
            }

            currentIndex++;
            wizard.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, currentIndex);
        }

        @Override
        public void previousPanel() {
            if (!hasPrevious()) {
                throw new NoSuchElementException("no previous panel available");
            }

            currentIndex--;
            wizard.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, currentIndex);
        }

        @Override
        public void addChangeListener(final ChangeListener l) {
        }

        @Override
        public void removeChangeListener(final ChangeListener l) {
        }
    }
}

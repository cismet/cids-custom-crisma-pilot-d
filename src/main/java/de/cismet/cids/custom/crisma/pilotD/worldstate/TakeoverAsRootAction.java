/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD.worldstate;

import org.openide.util.ImageUtilities;

import java.awt.event.ActionEvent;

import de.cismet.cids.utils.abstracts.AbstractCidsBeanAction;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class TakeoverAsRootAction extends AbstractCidsBeanAction {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TakeoverAsRootAction object.
     */
    public TakeoverAsRootAction() {
        super(
            "Add as new root",
            ImageUtilities.loadImageIcon(
                TakeoverAsRootAction.class.getPackage().getName().replaceAll(
                    "\\.",
                    "/")
                        + "/world_to_root_16.png",
                false));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
    }
}

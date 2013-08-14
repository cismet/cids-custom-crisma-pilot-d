/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD;

import org.openide.util.lookup.ServiceProvider;

import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.tools.configuration.StartupHook;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = StartupHook.class)
public final class PilotDStartupHook implements StartupHook {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void applicationStarted() {
        CismapBroker.getInstance().setMappingComponent(new MappingComponent(true));
    }
}

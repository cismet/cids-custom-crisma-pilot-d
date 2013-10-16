/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.crisma.pilotD;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import org.openide.util.lookup.ServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.cismet.cids.custom.crisma.pilotD.model.ExecModelWizardAction;
import de.cismet.cids.custom.crisma.pilotD.worldstate.TakeoverAsRootAction;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.utils.interfaces.CidsBeanAction;

import de.cismet.ext.CExtContext;
import de.cismet.ext.CExtProvider;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0, 2013/08/13
 */
@ServiceProvider(service = CExtProvider.class)
public final class WorldstateActionCExtProvider implements CExtProvider<CidsBeanAction> {

    //~ Instance fields --------------------------------------------------------

    private final String ifaceClass;
    private final String concreteClass1;
    private final String concreteClass2;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RainfallActionCExtProvider object.
     */
    public WorldstateActionCExtProvider() {
        ifaceClass = "de.cismet.cids.utils.interfaces.CidsBeanAction";                          // NOI18N
        concreteClass1 = "de.cismet.cids.custom.crisma.pilotD.model.ExecModelWizardAction";     // NOI18N
        concreteClass2 = "de.cismet.cids.custom.crisma.pilotD.worldstate.TakeoverAsRootAction"; // NOI18N
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection<? extends CidsBeanAction> provideExtensions(final CExtContext context) {
        final List<CidsBeanAction> actions = new ArrayList<CidsBeanAction>(1);

        if (context != null) {
            final Object ctxReference = context.getProperty(CExtContext.CTX_REFERENCE);

            final Object ctxObject;
            if (ctxReference instanceof Collection) {
                final Collection ctxCollection = (Collection)ctxReference;

                if (ctxCollection.size() == 1) {
                    ctxObject = ctxCollection.iterator().next();
                } else {
                    ctxObject = null;
                }
            } else if (ctxReference instanceof Object[]) {
                final Object[] ctxArray = (Object[])ctxReference;

                if (ctxArray.length == 1) {
                    ctxObject = ctxArray[0];
                } else {
                    ctxObject = null;
                }
            } else {
                ctxObject = ctxReference;
            }

            final MetaClass mc;
            final CidsBean ctxBean;
            if (ctxObject instanceof CidsBean) {
                ctxBean = (CidsBean)ctxObject;
                mc = ctxBean.getMetaObject().getMetaClass();
            } else if (ctxObject instanceof MetaObject) {
                final MetaObject mo = (MetaObject)ctxObject;
                ctxBean = mo.getBean();
                mc = mo.getMetaClass();
            } else {
                ctxBean = null;
                mc = null;
            }

            if (((mc != null) && (ctxBean != null))
                        && ("worldstates".equals(mc.getTableName()))) {
                final CidsBeanAction action1 = new ExecModelWizardAction();
                final CidsBeanAction action2 = new TakeoverAsRootAction();
                action1.setCidsBean(ctxBean);
                action2.setCidsBean(ctxBean);
                actions.add(action1);
                actions.add(action2);
            }
        }

        return actions;
    }

    @Override
    public Class<? extends CidsBeanAction> getType() {
        return CidsBeanAction.class;
    }

    @Override
    public boolean canProvide(final Class<?> c) {
        final String cName = c.getCanonicalName();

        return (cName == null)
            ? false : (ifaceClass.equals(cName) || concreteClass1.equals(cName) || concreteClass2.equals(cName));
    }
}

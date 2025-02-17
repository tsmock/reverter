// License: GPL. For details, see LICENSE file.
package reverter;

import static org.openstreetmap.josm.tools.I18n.marktr;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.tools.Shortcut;

import reverter.ChangesetReverter.RevertType;

/**
 * An action for reverting changesets
 */
public class RevertChangesetAction extends JosmAction {
    private static final String REVERT_CHANGESET = marktr("Revert changeset");

    /**
     * Create a new action for reverting changesets
     */
    public RevertChangesetAction() {
        super(tr(REVERT_CHANGESET), "revert-changeset", tr(REVERT_CHANGESET),
            Shortcut.registerShortcut("tool:revert", tr("Data: {0}", tr(REVERT_CHANGESET)), KeyEvent.VK_T, Shortcut.CTRL_SHIFT),
                true, false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final ChangesetIdQuery dlg = new ChangesetIdQuery();
        if (dlg.showDialog().getValue() != 1) return;
        final Collection<Integer> changesetIds = dlg.getIdsInReverseOrder();
        final RevertType revertType = dlg.getRevertType();
        if (revertType == null) return;

        boolean newLayer = dlg.isNewLayerRequired();
        final boolean autoConfirmDownload = newLayer || changesetIds.size() > 1;
        MainApplication.worker.submit(new RevertChangesetTask(changesetIds, revertType, autoConfirmDownload, newLayer));
    }
}
